package io.github.jukejuke.util;

import io.github.jukejuke.tool.date.DateUtils;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import static org.junit.jupiter.api.Assertions.*;

public class DateUtilsTest {

    @Test
    void testFormatLocalDate() {
        LocalDate date = LocalDate.of(2025, 12, 25);
        
        String formatted = DateUtils.format(date);
        assertEquals("2025-12-25", formatted);
        
        String formattedChinese = DateUtils.format(date, DateUtils.FORMAT_DATE_CHINESE);
        assertEquals("2025年12月25日", formattedChinese);
    }

    @Test
    void testFormatLocalDateTime() {
        LocalDateTime dateTime = LocalDateTime.of(2025, 12, 25, 10, 30, 45);
        
        String formatted = DateUtils.format(dateTime);
        assertEquals("2025-12-25 10:30:45", formatted);
        
        String formattedMilli = DateUtils.format(dateTime, DateUtils.FORMAT_DATETIME_MILLI);
        assertEquals("2025-12-25 10:30:45.000", formattedMilli);
        
        String formattedChinese = DateUtils.format(dateTime, DateUtils.FORMAT_DATETIME_CHINESE);
        assertEquals("2025年12月25日 10:30:45", formattedChinese);
    }

    @Test
    void testParseDate() {
        String dateStr = "2025-12-25";
        LocalDate date = DateUtils.parseDate(dateStr);
        
        assertNotNull(date);
        assertEquals(2025, date.getYear());
        assertEquals(12, date.getMonthValue());
        assertEquals(25, date.getDayOfMonth());
        
        String chineseDateStr = "2025年12月25日";
        LocalDate chineseDate = DateUtils.parseDate(chineseDateStr, DateUtils.FORMAT_DATE_CHINESE);
        assertEquals(2025, chineseDate.getYear());
        assertEquals(12, chineseDate.getMonthValue());
        assertEquals(25, chineseDate.getDayOfMonth());
    }

    @Test
    void testParseDateTime() {
        String dateTimeStr = "2025-12-25 10:30:45";
        LocalDateTime dateTime = DateUtils.parseDateTime(dateTimeStr);
        
        assertNotNull(dateTime);
        assertEquals(2025, dateTime.getYear());
        assertEquals(12, dateTime.getMonthValue());
        assertEquals(25, dateTime.getDayOfMonth());
        assertEquals(10, dateTime.getHour());
        assertEquals(30, dateTime.getMinute());
        assertEquals(45, dateTime.getSecond());
    }

    @Test
    void testDateConversion() {
        LocalDate localDate = LocalDate.of(2025, 12, 25);
        Date date = DateUtils.toDate(localDate);
        LocalDate convertedBack = DateUtils.toLocalDate(date);
        
        assertNotNull(date);
        assertNotNull(convertedBack);
        assertEquals(localDate, convertedBack);
        
        LocalDateTime localDateTime = LocalDateTime.of(2025, 12, 25, 10, 30, 45);
        Date dateFromDateTime = DateUtils.toDate(localDateTime);
        LocalDateTime convertedDateTimeBack = DateUtils.toLocalDateTime(dateFromDateTime);
        
        assertNotNull(dateFromDateTime);
        assertNotNull(convertedDateTimeBack);
        assertEquals(localDateTime.getYear(), convertedDateTimeBack.getYear());
        assertEquals(localDateTime.getMonthValue(), convertedDateTimeBack.getMonthValue());
        assertEquals(localDateTime.getDayOfMonth(), convertedDateTimeBack.getDayOfMonth());
        assertEquals(localDateTime.getHour(), convertedDateTimeBack.getHour());
        assertEquals(localDateTime.getMinute(), convertedDateTimeBack.getMinute());
        assertEquals(localDateTime.getSecond(), convertedDateTimeBack.getSecond());
    }

    @Test
    void testStartAndEndOfDay() {
        LocalDate date = LocalDate.of(2025, 12, 25);
        
        LocalDateTime startOfDay = DateUtils.startOfDay(date);
        assertNotNull(startOfDay);
        assertEquals(0, startOfDay.getHour());
        assertEquals(0, startOfDay.getMinute());
        assertEquals(0, startOfDay.getSecond());
        
        LocalDateTime endOfDay = DateUtils.endOfDay(date);
        assertNotNull(endOfDay);
        assertEquals(23, endOfDay.getHour());
        assertEquals(59, endOfDay.getMinute());
        assertEquals(59, endOfDay.getSecond());
    }

    @Test
    void testDateDifference() {
        LocalDate startDate = LocalDate.of(2025, 12, 25);
        LocalDate endDate = LocalDate.of(2024, 1, 1);
        
        long daysBetween = DateUtils.daysBetween(startDate, endDate);
        assertEquals(7, daysBetween);
        
        LocalDateTime startTime = LocalDateTime.of(2025, 12, 25, 10, 0, 0);
        LocalDateTime endTime = LocalDateTime.of(2025, 12, 25, 12, 30, 0);
        
        long hoursBetween = DateUtils.hoursBetween(startTime, endTime);
        assertEquals(2, hoursBetween);
        
        long minutesBetween = DateUtils.minutesBetween(startTime, endTime);
        assertEquals(150, minutesBetween);
        
        long secondsBetween = DateUtils.secondsBetween(startTime, endTime);
        assertEquals(9000, secondsBetween);
    }

    @Test
    void testAddDateComponents() {
        LocalDate date = LocalDate.of(2025, 12, 25);
        LocalDate futureDate = DateUtils.addDays(date, 5);
        
        assertNotNull(futureDate);
        assertEquals(2025, futureDate.getYear());
        assertEquals(12, futureDate.getMonthValue());
        assertEquals(30, futureDate.getDayOfMonth());
        
        LocalDateTime dateTime = LocalDateTime.of(2025, 12, 25, 10, 0, 0);
        LocalDateTime futureDateTime = DateUtils.addHours(dateTime, 3);
        futureDateTime = DateUtils.addMinutes(futureDateTime, 30);
        futureDateTime = DateUtils.addSeconds(futureDateTime, 45);
        
        assertNotNull(futureDateTime);
        assertEquals(2025, futureDateTime.getYear());
        assertEquals(12, futureDateTime.getMonthValue());
        assertEquals(25, futureDateTime.getDayOfMonth());
        assertEquals(13, futureDateTime.getHour());
        assertEquals(30, futureDateTime.getMinute());
        assertEquals(45, futureDateTime.getSecond());
    }

    @Test
    void testSameDayCheck() {
        LocalDate date1 = LocalDate.of(2025, 12, 25);
        LocalDate date2 = LocalDate.of(2025, 12, 25);
        LocalDate date3 = LocalDate.of(2025, 12, 26);
        
        assertTrue(DateUtils.isSameDay(date1, date2));
        assertFalse(DateUtils.isSameDay(date1, date3));
        
        LocalDateTime dateTime1 = LocalDateTime.of(2025, 12, 25, 10, 0, 0);
        LocalDateTime dateTime2 = LocalDateTime.of(2025, 12, 25, 15, 30, 0);
        LocalDateTime dateTime3 = LocalDateTime.of(2025, 12, 26, 10, 0, 0);
        
        assertTrue(DateUtils.isSameDay(dateTime1, dateTime2));
        assertFalse(DateUtils.isSameDay(dateTime1, dateTime3));
    }

    @Test
    void testIsToday() {
        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);
        
        assertTrue(DateUtils.isToday(today));
        assertFalse(DateUtils.isToday(yesterday));
        
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime yesterdayNow = now.minusDays(1);
        
        assertTrue(DateUtils.isToday(now));
        assertFalse(DateUtils.isToday(yesterdayNow));
    }

    @Test
    void testGetAge() {
        LocalDate birthDate = LocalDate.of(1990, 5, 15);
        int age = DateUtils.getAge(birthDate);
        
        // Calculate expected age
        LocalDate currentDate = LocalDate.now();
        int expectedAge = currentDate.getYear() - birthDate.getYear();
        if (currentDate.getMonthValue() < birthDate.getMonthValue() ||
            (currentDate.getMonthValue() == birthDate.getMonthValue() &&
             currentDate.getDayOfMonth() < birthDate.getDayOfMonth())) {
            expectedAge--;
        }
        
        assertEquals(expectedAge, age);
    }

    @Test
    void testFirstAndLastDayOfMonth() {
        LocalDate date = LocalDate.of(2025, 2, 15);
        
        LocalDate firstDay = DateUtils.firstDayOfMonth(date);
        assertNotNull(firstDay);
        assertEquals(1, firstDay.getDayOfMonth());
        
        LocalDate lastDay = DateUtils.lastDayOfMonth(date);
        assertNotNull(lastDay);
        // 2025年2月有28天
        assertEquals(28, lastDay.getDayOfMonth());
        
        // Test leap year
        LocalDate leapYearDate = LocalDate.of(2024, 2, 15);
        LocalDate leapYearLastDay = DateUtils.lastDayOfMonth(leapYearDate);
        assertEquals(29, leapYearLastDay.getDayOfMonth());
    }

    @Test
    void testNullHandling() {
        // Test null handling in format methods
        assertNull(DateUtils.format((LocalDate) null));
        assertNull(DateUtils.format((LocalDateTime) null));
        
        // Test null handling in parse methods
        assertNull(DateUtils.parseDate(null));
        assertNull(DateUtils.parseDateTime(null));
        
        // Test null handling in conversion methods
        assertNull(DateUtils.toLocalDate(null));
        assertNull(DateUtils.toLocalDateTime(null));
        assertNull(DateUtils.toDate((LocalDate) null));
        assertNull(DateUtils.toDate((LocalDateTime) null));
        
        // Test null handling in start/end of day
        assertNull(DateUtils.startOfDay(null));
        assertNull(DateUtils.endOfDay(null));
        
        // Test null handling in difference methods
        assertEquals(0, DateUtils.daysBetween(null, LocalDate.now()));
        assertEquals(0, DateUtils.daysBetween(LocalDate.now(), null));
        assertEquals(0, DateUtils.daysBetween(null, null));
        
        // Test null handling in add methods
        assertNull(DateUtils.addDays(null, 5));
        assertNull(DateUtils.addHours(null, 5));
        
        // Test null handling in same day check
        assertFalse(DateUtils.isSameDay((LocalDate) null, LocalDate.now()));
        assertFalse(DateUtils.isSameDay(LocalDate.now(), (LocalDate) null));
        assertFalse(DateUtils.isSameDay((LocalDate) null, (LocalDate) null));
        assertFalse(DateUtils.isSameDay((LocalDateTime) null, LocalDateTime.now()));
        assertFalse(DateUtils.isSameDay(LocalDateTime.now(), (LocalDateTime) null));
        assertFalse(DateUtils.isSameDay((LocalDateTime) null, (LocalDateTime) null));
        
        // Test null handling in isToday
        assertFalse(DateUtils.isToday((LocalDate) null));
        assertFalse(DateUtils.isToday((LocalDateTime) null));
        
        // Test null handling in getAge
        assertEquals(0, DateUtils.getAge(null));
        
        // Test null handling in first/last day of month
        assertNull(DateUtils.firstDayOfMonth(null));
        assertNull(DateUtils.lastDayOfMonth(null));
    }
}
