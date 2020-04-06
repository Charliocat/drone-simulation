package com.simulation.control;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

public class Timer {

    public static Instant getInstantFromString(String timestamp) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        TemporalAccessor temporalAccessor = formatter.parse(timestamp);
        LocalDateTime localDateTime = LocalDateTime.from(temporalAccessor);
        ZonedDateTime zonedDateTime = ZonedDateTime.of(localDateTime, ZoneId.of("UTC"));
        return Instant.from(zonedDateTime);
    }

    public static Instant getInstantFromString(String timestamp, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        TemporalAccessor temporalAccessor = formatter.parse(timestamp);
        LocalDateTime localDateTime = LocalDateTime.from(temporalAccessor);
        ZonedDateTime zonedDateTime = ZonedDateTime.of(localDateTime, ZoneId.of("UTC"));
        return Instant.from(zonedDateTime);
    }

    private volatile Instant currentTime;
    private volatile Instant endTime;

    public Timer(String currentTime, String endTime) {
        this.currentTime = getInstantFromString(currentTime);
        this.endTime = getInstantFromString(endTime);
    }

    public Instant getTime() {
        currentTime = currentTime.plusSeconds(1);
        return currentTime;
    }

    public Instant getEndTime() {
        return endTime;
    }

}
