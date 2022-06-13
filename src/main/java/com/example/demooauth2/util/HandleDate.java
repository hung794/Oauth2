package com.example.demooauth2.util;

import java.time.LocalDateTime;
import java.time.ZoneId;

public class HandleDate {
    public static long convertDateToLong(long dayPlus){
        ZoneId zoneId = ZoneId.systemDefault();
        return LocalDateTime.now().plusDays(dayPlus).atZone(zoneId).toEpochSecond();
    }
}
