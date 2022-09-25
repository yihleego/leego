package io.leego.commons.time;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author Leego Yih
 */
public final class TimeUtils {
    private static final ConcurrentMap<String, DateTimeFormatter> cache = new ConcurrentHashMap<>(32);
    private static final DateTimeFormatter formatter = ofPattern(TimePatterns.TIME);

    private TimeUtils() {
    }

    public static DateTimeFormatter ofPattern(String pattern) {
        if (pattern == null || pattern.isEmpty()) {
            throw new IllegalArgumentException("Pattern is required");
        }
        return cache.computeIfAbsent(pattern, DateTimeFormatter::ofPattern);
    }

    public static LocalTime time() {
        return LocalTime.now();
    }

    public static LocalTime time(LocalDateTime dateTime) {
        return dateTime.toLocalTime();
    }

    public static String now() {
        return formatter.format(LocalTime.now());
    }

    public static String format(LocalTime time) {
        return formatter.format(time);
    }

    public static String format(LocalTime time, String pattern) {
        return ofPattern(pattern).format(time);
    }

    public static String format(String s, String parsePattern, String formatPattern) {
        return format(parse(s, parsePattern), formatPattern);
    }

    public static String format(long timestamp) {
        return formatter.format(parse(timestamp));
    }

    public static String format(long timestamp, String pattern) {
        return ofPattern(pattern).format(parse(timestamp));
    }

    public static LocalTime parse(String s) {
        return LocalTime.parse(s, formatter);
    }

    public static LocalTime parse(String s, String pattern) {
        return LocalTime.parse(s, ofPattern(pattern));
    }

    public static LocalTime parse(long timestamp) {
        return Instant.ofEpochMilli(timestamp).atZone(ZoneId.systemDefault()).toLocalTime();
    }

    public static LocalTime plusHours(LocalTime time, long amount) {
        return time.plusHours(amount);
    }

    public static LocalTime plusMinutes(LocalTime time, long amount) {
        return time.plusMinutes(amount);
    }

    public static LocalTime plusSeconds(LocalTime time, long amount) {
        return time.plusSeconds(amount);
    }

    public static LocalTime plusMillis(LocalTime time, long amount) {
        return time.plus(amount, ChronoUnit.MILLIS);
    }

    public static LocalTime plusMicros(LocalTime time, long amount) {
        return time.plus(amount, ChronoUnit.MICROS);
    }

    public static LocalTime plusNanos(LocalTime time, long amount) {
        return time.plus(amount, ChronoUnit.NANOS);
    }

    public static LocalTime minusHours(LocalTime time, long amount) {
        return time.minusHours(amount);
    }

    public static LocalTime minusMinutes(LocalTime time, long amount) {
        return time.minusMinutes(amount);
    }

    public static LocalTime minusSeconds(LocalTime time, long amount) {
        return time.minusSeconds(amount);
    }

    public static LocalTime minusMillis(LocalTime time, long amount) {
        return time.minus(amount, ChronoUnit.MILLIS);
    }

    public static LocalTime minusMicros(LocalTime time, long amount) {
        return time.minus(amount, ChronoUnit.MICROS);
    }

    public static LocalTime minusNanos(LocalTime time, long amount) {
        return time.minus(amount, ChronoUnit.NANOS);
    }

    public static long diffOfHours(Temporal inclusive, Temporal exclusive) {
        return ChronoUnit.HOURS.between(inclusive, exclusive);
    }

    public static long diffOfMinutes(Temporal inclusive, Temporal exclusive) {
        return ChronoUnit.MINUTES.between(inclusive, exclusive);
    }

    public static long diffOfSeconds(Temporal inclusive, Temporal exclusive) {
        return ChronoUnit.SECONDS.between(inclusive, exclusive);
    }

    public static long diffOfMillis(Temporal inclusive, Temporal exclusive) {
        return ChronoUnit.MILLIS.between(inclusive, exclusive);
    }

    public static long diffOfMicros(Temporal inclusive, Temporal exclusive) {
        return ChronoUnit.MICROS.between(inclusive, exclusive);
    }

    public static long diffOfNanos(Temporal inclusive, Temporal exclusive) {
        return ChronoUnit.NANOS.between(inclusive, exclusive);
    }

}
