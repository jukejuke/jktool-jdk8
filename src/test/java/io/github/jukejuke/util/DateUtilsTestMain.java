package io.github.jukejuke.util;

import io.github.jukejuke.tool.date.DateUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class DateUtilsTestMain {
    public static void main(String[] args) {
        System.out.println("Testing DateUtils functionality...");
        
        // Test format methods
        LocalDate date = LocalDate.of(2025, 12, 25);
        String formattedDate = DateUtils.format(date);
        System.out.println("Formatted date: " + formattedDate);
        
        LocalDateTime dateTime = LocalDateTime.of(2025, 12, 25, 10, 30, 45);
        String formattedDateTime = DateUtils.format(dateTime);
        System.out.println("Formatted datetime: " + formattedDateTime);
        
        // Test parse methods
        LocalDate parsedDate = DateUtils.parseDate("2025-12-25");
        System.out.println("Parsed date: " + parsedDate);
        
        LocalDateTime parsedDateTime = DateUtils.parseDateTime("2025-12-25 10:30:45");
        System.out.println("Parsed datetime: " + parsedDateTime);
        
        // Test date operations
        LocalDate tomorrow = DateUtils.addDays(date, 1);
        System.out.println("Tomorrow: " + tomorrow);
        
        long daysBetween = DateUtils.daysBetween(date, tomorrow);
        System.out.println("Days between date and tomorrow: " + daysBetween);
        
        // Test today and now
        LocalDate today = DateUtils.today();
        LocalDateTime now = DateUtils.now();
        System.out.println("Today: " + today);
        System.out.println("Now: " + now);
        
        // Test start and end of day
        LocalDateTime startOfDay = DateUtils.startOfDay(date);
        LocalDateTime endOfDay = DateUtils.endOfDay(date);
        System.out.println("Start of day: " + startOfDay);
        System.out.println("End of day: " + endOfDay);
        
        // Test is same day
        boolean isSameDay = DateUtils.isSameDay(dateTime, LocalDateTime.of(2025, 12, 25, 15, 0, 0));
        System.out.println("Is same day: " + isSameDay);
        
        System.out.println("\nAll tests completed successfully!");
    }
}
