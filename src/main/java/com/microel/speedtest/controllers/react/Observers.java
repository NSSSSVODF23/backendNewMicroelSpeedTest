package com.microel.speedtest.controllers.react;

import com.microel.speedtest.common.models.updateprovides.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.microel.speedtest.controllers.performance.PerformanceInfo;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Configuration
public class Observers {

    /**
     * Сток новых замеров
     */
    @Bean
    public Sinks.Many<MeasureUpdateProvider> newMeasureSink() {
        return Sinks.many().multicast().directBestEffort();
    }

    /**
     * Исток новых замеров
     */
    @Bean
    public Flux<MeasureUpdateProvider> newMeasureFlux() {
        return newMeasureSink().asFlux();
    }

    /**
     * Сток запущенных замеров
     */
    @Bean
    public Sinks.Many<BeginningMeasureUpdateProvider> beginningMeasureSink() {
        return Sinks.many().multicast().directBestEffort();
    }

    /**
     * Исток новых замеров
     */
    @Bean
    public Flux<BeginningMeasureUpdateProvider> beginningMeasureFlux() {
        return beginningMeasureSink().asFlux();
    }

    /**
     * Сток обновлений списка пользователей
     */
    @Bean
    public Sinks.Many<UserUpdateProvider> updateUserSink() {
        return Sinks.many().multicast().directBestEffort();
    }

    /**
     * Исток обновлений списка пользователей
     */
    @Bean
    public Flux<UserUpdateProvider> updateUserFlux() {
        return updateUserSink().asFlux();
    }

    /**
     * Сток показателей загруженности сервера
     */
    @Bean
    public Sinks.Many<PerformanceInfo> updatePerformanceSink() {
        return Sinks.many().multicast().directBestEffort();
    }

    /**
     * Исток показателей загруженности сервера
     */
    @Bean
    public Flux<PerformanceInfo> updatePerformanceFlux() {
        return updatePerformanceSink().asFlux();
    }

    /**
     * Сток обновления списка сессий пользователей подключенных к сайту
     */
    @Bean
    public Sinks.Many<ActiveSessionsUpdateProvider> updateActiveSessionSink(){
        return Sinks.many().multicast().directBestEffort();
    }

    /**
     * Исток обновления списка сессий пользователей подключенных к сайту
     */
    @Bean
    public Flux<ActiveSessionsUpdateProvider> updateActiveSessionFlux(){
        return updateActiveSessionSink().asFlux();
    }

    /**
     * Сток обновления списка жалоб
     */
    @Bean
    public Sinks.Many<ComplaintUpdateProvider> updateComplaintSink(){
        return Sinks.many().multicast().directBestEffort();
    }

    /**
     * Исток обновления списка жалоб
     */
    @Bean
    public Flux<ComplaintUpdateProvider> updateComplaintFlux(){ return updateComplaintSink().asFlux();}

    /**
     * Сток обновления списка устройств
     */
    @Bean
    public Sinks.Many<DeviceUpdateProvider> updateDeviceSink(){
        return Sinks.many().multicast().directBestEffort();
    }

    /**
     * Исток обновления списка устройств
     */
    @Bean
    public Flux<DeviceUpdateProvider> updateDeviceFlux(){ return updateDeviceSink().asFlux();}
}
