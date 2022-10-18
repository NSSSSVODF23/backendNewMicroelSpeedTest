package com.microel.speedtest.common;

public class Util {
    public static String toDayOfWeek(Integer dayNum){
        switch (dayNum){
            case 0: return "Воскресенье";
            case 1: return "Понедельник";
            case 2: return "Вторник";
            case 3: return "Среда";
            case 4: return "Четверг";
            case 5: return "Пятница";
            case 6: return "Суббота";
            default: return "";
        }
    }
}
