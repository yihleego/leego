package io.leego.commons.standard.time;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
public final class DateUtils {
    private static final ConcurrentMap<String, DateTimeFormatter> cache = new ConcurrentHashMap<>(32);
    private static final DateTimeFormatter formatter = ofPattern(DatePatterns.DATE);

    private DateUtils() {
    }

    public static DateTimeFormatter ofPattern(String pattern) {
        if (pattern == null || pattern.isEmpty()) {
            throw new IllegalArgumentException("Pattern is required");
        }
        return cache.computeIfAbsent(pattern, DateTimeFormatter::ofPattern);
    }

    public static LocalDate date() {
        return LocalDate.now();
    }

    public static LocalDate date(LocalDateTime dateTime) {
        return dateTime.toLocalDate();
    }

    public static String now() {
        return formatter.format(LocalDate.now());
    }

    public static String format(LocalDate date) {
        return formatter.format(date);
    }

    public static String format(LocalDate date, String pattern) {
        return ofPattern(pattern).format(date);
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

    public static LocalDate parse(String s) {
        return LocalDate.parse(s, formatter);
    }

    public static LocalDate parse(String s, String pattern) {
        return LocalDate.parse(s, ofPattern(pattern));
    }

    public static LocalDate parse(long timestamp) {
        return Instant.ofEpochMilli(timestamp).atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public static LocalDate plusYears(LocalDate date, long amount) {
        return date.plusYears(amount);
    }

    public static LocalDate plusMonths(LocalDate date, long amount) {
        return date.plusMonths(amount);
    }

    public static LocalDate plusWeeks(LocalDate date, long amount) {
        return date.plusWeeks(amount);
    }

    public static LocalDate plusDays(LocalDate date, long amount) {
        return date.plusDays(amount);
    }

    public static LocalDate adjustFirstDayOfYear(LocalDate date) {
        return date.with(TemporalAdjusters.firstDayOfYear());
    }

    public static LocalDate adjustLastDayOfYear(LocalDate date) {
        return date.with(TemporalAdjusters.lastDayOfYear());
    }

    public static LocalDate adjustFirstDayOfMonth(LocalDate date) {
        return date.with(TemporalAdjusters.firstDayOfMonth());
    }

    public static LocalDate adjustLastDayOfMonth(LocalDate date) {
        return date.with(TemporalAdjusters.lastDayOfMonth());
    }

    public static int getDayOfYear(LocalDate date) {
        return date.getDayOfYear();
    }

    public static int getDayOfMonth(LocalDate date) {
        return date.getDayOfMonth();
    }

    public static int getDayOfWeek(LocalDate date) {
        return date.getDayOfWeek().getValue();
    }

    public static int getLastDayOfMonth(LocalDate date) {
        return date.with(TemporalAdjusters.lastDayOfMonth()).getDayOfMonth();
    }

    public static boolean isWeekend(LocalDate date) {
        DayOfWeek dayOfWeek = date.getDayOfWeek();
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

}
