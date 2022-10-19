package com.microel.speedtest.services.acpparser;

import com.microel.speedtest.repositories.SessionRepositoryDispatcher;
import com.microel.speedtest.repositories.entities.AcpHouse;
import com.microel.speedtest.repositories.entities.AcpSession;
import com.microel.speedtest.services.acpparser.interfaces.MeasureSessionCallback;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;

@Slf4j
@Component
public class Acp {
    private final SessionRepositoryDispatcher sessionRepositoryDispatcher;
    private Map<String, String> cookies = new HashMap<>();
    private final Set<AcpHouse> houses = new HashSet<>();
    private final Set<AcpSession> sessions = new HashSet<>();

    private final Set<String> retryIp = new HashSet<>();

    private final AcpHouse emptyHouse = new AcpHouse(0L, Short.valueOf("0"), "Не определен");

    public Acp(SessionRepositoryDispatcher sessionRepositoryDispatcher) {
        this.sessionRepositoryDispatcher = sessionRepositoryDispatcher;
        logIn();
        updateHouses();
        updateSessions();
    }

    public void logIn(){
        try {
            Response res = Jsoup
                    .connect("http://10.50.3.27/index.php/")
                    .data("logon", "1")
                    .data("login", "admin")
                    .data("password", "abjktnjdsq")
                    .method(Method.POST)
                    .execute();
            Document doc = res.parse();
            if (doc.title().equals("ACP"))
                throw new RuntimeException("ACP Login failed.");
            // Save cookie
            cookies = res.cookies();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateHouses() {
        try {
            Document doc = Jsoup.connect("http://10.50.3.27/index.php/networks/view").cookies(cookies).get();
            Elements rows = doc.select("#vlans_base_content tr.color_fill:not(.beetwen)");
            rows.forEach((element) -> {
                AcpHouse house = new AcpHouse();
                Elements cols = element.select("td");
                house.setVlan(Short.valueOf(cols.get(0).text().substring(8)));
                house.setAddress(cols.get(5).text());
                if (houses.add(house)) {
                    log.debug("Добавлен дом: {}", house);
                }
            });
            log.info("Загружено {} домов", houses.size());
        } catch (IOException e) {
            log.info("Ошибка загрузки домов", e);
        }
    }

    public void updateSessions() {
        try {
            Document doc = Jsoup.connect("http://10.50.3.27/index.php/sessions/active/auth").maxBodySize(0)
                    .timeout(60000).cookies(cookies).get();
            Elements rows = doc.select(".acp_data_tables tbody tr");
            sessions.clear();
            rows.forEach((element) -> {
                AcpSession session = new AcpSession();
                Elements cols = element.select("td");
                session.setMac(cols.get(1).text());
                session.setLogin(cols.get(2).text());
                session.setVlan(Short.valueOf(cols.get(3).text()));
                session.setIp(cols.get(4).text());
                if (sessions.add(session)) {
                    log.debug("Добавлена сессия: {}", session);
                }
            });
            log.info("Загружено {} сессий", sessions.size());
        } catch (Exception e) {
            log.info("Ошибка загрузки сессий", e);
        }
    }

    public AcpHouse getHouse(Short vlan) {
        return houses.stream().filter((house) -> house.getVlan().equals(vlan)).findFirst().orElse(null);
    }

    public AcpSession getSession(String ip) {
//         FIXMEs Выбор случайной сессии для тестирования
        return sessions.stream().filter((session) -> session.getIp().equals(ip)).findFirst().orElse(null);
//        return sessions.stream().toArray(AcpSession[]::new)[Long.valueOf(Math.round((sessions.size() - 1) * Math.random())).intValue()];
    }

    public void takeMeasureSession(String ip, MeasureSessionCallback callback) {
        Executors.newSingleThreadScheduledExecutor().execute(() -> {
            try {
                callback.handle(generateSessionByIP(ip));
            } catch (IOException e) {
                log.warn(e.getMessage());
                AcpSession empty = emptySessionByIP(ip);
                callback.handle(empty);
                if(!retryIp.add(ip)) return;
                try {
                    Thread.sleep(1000*60*10);
                    generateSessionByIP(ip,empty.getSessionId());
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                } catch (IOException ex) {
                    log.warn("Повторное получение сессии не удалось");
                }finally {
                    retryIp.remove(ip);
                }
            }
        });
    }

    private AcpSession generateSessionByIP(String ip) throws IOException {
        return generateSessionByIP(ip, null);
    }

    private AcpSession generateSessionByIP(String ip, Long existedId) throws IOException{
        log.info("Пытаемся получить сессию для IP: {}", ip);
        AcpSession session = getSession(ip);
        logIn();
        if (session == null) {
            updateSessions();
            log.info("Обновление сессий");
            session = getSession(ip);
        }
        if (session != null) {
            session.setHouse(getHouse(session.getVlan()));
            if (session.getHouse() == null) {
                updateHouses();
                log.info("Обновление домов");
                session.setHouse(getHouse(session.getVlan()));
            }
            if (session.getHouse() == null) {
                log.info("Дом не найден");
            }
            log.info("Получена сессия: {}", session);
            session.setSessionId(existedId);
            return sessionRepositoryDispatcher.save(session);
        }else{
            throw new IOException("Сессия в ацп не найдена");
        }
    }

    private AcpSession emptySessionByIP(String ip){
        AcpSession session = new AcpSession();
        session.setLogin(ip);
        session.setIp(ip);
        session.setHouse(emptyHouse);
        log.debug("Сессия не найдена");
        log.info("Получена пустая сессия: {}", session);
        return sessionRepositoryDispatcher.save(session);
    }
}
