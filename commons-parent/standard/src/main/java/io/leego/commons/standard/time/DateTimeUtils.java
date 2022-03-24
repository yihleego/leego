package io.leego.commons.standard.time;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAdjusters;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author Leego Yih
 */
public final class DateTimeUtils {
    private static final ConcurrentMap<String, DateTimeFormatter> cache = new ConcurrentHashMap<>(32);
    private static final DateTimeFormatter formatter = ofPattern(DateTimePatterns.DATE_TIME);

    private DateTimeUtils() {
    }

    public static DateTimeFormatter ofPattern(String pattern) {
        if (pattern == null || pattern.isEmpty()) {
            throw new IllegalArgumentException("Pattern is required");
        }
        return cache.computeIfAbsent(pattern, DateTimeFormatter::ofPattern);
    }

    public static LocalDateTime datetime() {
        return LocalDateTime.now();
    }

    public static long timestamp() {
        return System.currentTimeMillis();
    }

    public static long timestamp(LocalDateTime dateTime) {
        return dateTime.toInstant(OffsetDateTime.now().getOffset()).toEpochMilli();
    }

    public static String now() {
        return formatter.format(LocalDateTime.now());
    }

    public static String format(LocalDateTime dateTime) {
        return formatter.format(dateTime);
    }

    public static String format(LocalDateTime dateTime, String pattern) {
        return ofPattern(pattern).format(dateTime);
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

    public static LocalDateTime parse(String s) {
        return LocalDateTime.parse(s, formatter);
    }

    public static LocalDateTime parse(String s, String pattern) {
        return LocalDateTime.parse(s, ofPattern(pattern));
    }

    public static LocalDateTime parse(long timestamp) {
        return Instant.ofEpochMilli(timestamp).atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public static LocalDateTime plusYears(LocalDateTime dateTime, long amount) {
        return dateTime.plusYears(amount);
    }

    public static LocalDateTime plusMonths(LocalDateTime dateTime, long amount) {
        return dateTime.plusMonths(amount);
    }

    public static LocalDateTime plusWeeks(LocalDateTime dateTime, long amount) {
        return dateTime.plusWeeks(amount);
    }

    public static LocalDateTime plusDays(LocalDateTime dateTime, long amount) {
        return dateTime.plusDays(amount);
    }

    public static LocalDateTime plusHours(LocalDateTime dateTime, long amount) {
        return dateTime.plusHours(amount);
    }

    public static LocalDateTime plusMinutes(LocalDateTime dateTime, long amount) {
        return dateTime.plusMinutes(amount);
    }

    public static LocalDateTime plusSeconds(LocalDateTime dateTime, long amount) {
        return dateTime.plusSeconds(amount);
    }

    public static LocalDateTime plusMillis(LocalDateTime dateTime, long amount) {
        return dateTime.plus(amount, ChronoUnit.MILLIS);
    }

    public static LocalDateTime plusMicros(LocalDateTime dateTime, long amount) {
        return dateTime.plus(amount, ChronoUnit.MICROS);
    }

    public static LocalDateTime plusNanos(LocalDateTime dateTime, long amount) {
        return dateTime.plus(amount, ChronoUnit.NANOS);
    }

    public static LocalDateTime adjustFirstDayOfYear(LocalDateTime dateTime) {
        return dateTime.with(TemporalAdjusters.firstDayOfYear());
    }

    public static LocalDateTime adjustLastDayOfYear(LocalDateTime dateTime) {
        return dateTime.with(TemporalAdjusters.lastDayOfYear());
    }

    public static LocalDateTime adjustFirstDayOfMonth(LocalDateTime dateTime) {
        return dateTime.with(TemporalAdjusters.firstDayOfMonth());
    }

    public static LocalDateTime adjustLastDayOfMonth(LocalDateTime dateTime) {
        return dateTime.with(TemporalAdjusters.lastDayOfMonth());
    }

    public static int getDayOfYear(LocalDateTime dateTime) {
        return dateTime.getDayOfYear();
    }

    public static int getDayOfMonth(LocalDateTime dateTime) {
        return dateTime.getDayOfMonth();
    }

    public static int getDayOfWeek(LocalDateTime dateTime) {
        return dateTime.getDayOfWeek().getValue();
    }

    public static int getLastDayOfMonth(LocalDateTime dateTime) {
        return adjustLastDayOfMonth(dateTime).getDayOfMonth();
    }

    public static boolean isWeekend(LocalDateTime dateTime) {
        DayOfWeek dayOfWeek = dateTime.getDayOfWeek();
        return dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY;
    }

    public static long diffOfYears(Temporal inclusive, Temporal exclusive) {
        return ChronoUnit.YEARS.between(inclusive, exclusive);
    }

    public static long diffOfMonths(Temporal inclusive, Temporal exclusive) {
        return ChronoUnit.MONTHS.between(inclusive, exclusive);
    }

    public static long diffOfWeeks(Temporal inclusive, Temporal exclusive) {
        return ChronoUnit.WEEKS.between(inclusive, exclusive);
    }

    public static long diffOfDays(Temporal inclusive, Temporal exclusive) {
        return ChronoUnit.DAYS.between(inclusive, exclusive);
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
