package io.github.jukejuke.tool.date;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.time.ZoneId;

/**
 * 日期时间处理工具类
 * 提供日期格式化、解析、转换、计算和比较等功能
 */
public class DateUtils {

    // 常用日期时间格式
    public static final String FORMAT_DATE = "yyyy-MM-dd";
    public static final String FORMAT_TIME = "HH:mm:ss";
    public static final String FORMAT_DATETIME = "yyyy-MM-dd HH:mm:ss";
    public static final String FORMAT_DATETIME_MILLI = "yyyy-MM-dd HH:mm:ss.SSS";
    public static final String FORMAT_DATE_CHINESE = "yyyy年MM月dd日";
    public static final String FORMAT_DATETIME_CHINESE = "yyyy年MM月dd日 HH:mm:ss";

    /**
     * 将LocalDate格式化为指定模式的字符串
     * @param date LocalDate对象
     * @param pattern 格式模式
     * @return 格式化后的日期字符串
     */
    public static String format(LocalDate date, String pattern) {
        if (date == null) {
            return null;
        }
        return date.format(DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * 将LocalDate格式化为默认模式(yyyy-MM-dd)的字符串
     * @param date LocalDate对象
     * @return 格式化后的日期字符串
     */
    public static String format(LocalDate date) {
        return format(date, FORMAT_DATE);
    }

    /**
     * 将LocalDateTime格式化为指定模式的字符串
     * @param dateTime LocalDateTime对象
     * @param pattern 格式模式
     * @return 格式化后的日期时间字符串
     */
    public static String format(LocalDateTime dateTime, String pattern) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.format(DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * 将LocalDateTime格式化为默认模式(yyyy-MM-dd HH:mm:ss)的字符串
     * @param dateTime LocalDateTime对象
     * @return 格式化后的日期时间字符串
     */
    public static String format(LocalDateTime dateTime) {
        return format(dateTime, FORMAT_DATETIME);
    }

    /**
     * 将字符串解析为指定模式的LocalDate
     * @param dateStr 日期字符串
     * @param pattern 格式模式
     * @return LocalDate对象
     */
    public static LocalDate parseDate(String dateStr, String pattern) {
        if (dateStr == null || dateStr.isEmpty()) {
            return null;
        }
        return LocalDate.parse(dateStr, DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * 将字符串解析为默认模式(yyyy-MM-dd)的LocalDate
     * @param dateStr 日期字符串
     * @return LocalDate对象
     */
    public static LocalDate parseDate(String dateStr) {
        return parseDate(dateStr, FORMAT_DATE);
    }

    /**
     * 将字符串解析为指定模式的LocalDateTime
     * @param dateTimeStr 日期时间字符串
     * @param pattern 格式模式
     * @return LocalDateTime对象
     */
    public static LocalDateTime parseDateTime(String dateTimeStr, String pattern) {
        if (dateTimeStr == null || dateTimeStr.isEmpty()) {
            return null;
        }
        return LocalDateTime.parse(dateTimeStr, DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * 将字符串解析为默认模式(yyyy-MM-dd HH:mm:ss)的LocalDateTime
     * @param dateTimeStr 日期时间字符串
     * @return LocalDateTime对象
     */
    public static LocalDateTime parseDateTime(String dateTimeStr) {
        return parseDateTime(dateTimeStr, FORMAT_DATETIME);
    }

    /**
     * 将Date转换为LocalDate
     * @param date Date对象
     * @return LocalDate对象
     */
    public static LocalDate toLocalDate(Date date) {
        if (date == null) {
            return null;
        }
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    /**
     * 将Date转换为LocalDateTime
     * @param date Date对象
     * @return LocalDateTime对象
     */
    public static LocalDateTime toLocalDateTime(Date date) {
        if (date == null) {
            return null;
        }
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    /**
     * 将LocalDate转换为Date
     * @param localDate LocalDate对象
     * @return Date对象
     */
    public static Date toDate(LocalDate localDate) {
        if (localDate == null) {
            return null;
        }
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 将LocalDateTime转换为Date
     * @param localDateTime LocalDateTime对象
     * @return Date对象
     */
    public static Date toDate(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return null;
        }
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 获取当前LocalDate
     * @return 当前LocalDate
     */
    public static LocalDate today() {
        return LocalDate.now();
    }

    /**
     * 获取当前LocalDateTime
     * @return 当前LocalDateTime
     */
    public static LocalDateTime now() {
        return LocalDateTime.now();
    }

    /**
     * 获取指定日期的开始时间(00:00:00)
     * @param date LocalDate对象
     * @return 当天开始时间的LocalDateTime
     */
    public static LocalDateTime startOfDay(LocalDate date) {
        if (date == null) {
            return null;
        }
        return date.atStartOfDay();
    }

    /**
     * 获取指定日期的结束时间(23:59:59.999999999)
     * @param date LocalDate对象
     * @return 当天结束时间的LocalDateTime
     */
    public static LocalDateTime endOfDay(LocalDate date) {
        if (date == null) {
            return null;
        }
        return date.atTime(LocalTime.MAX);
    }

    /**
     * 计算两个LocalDate之间的天数差
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 天数差
     */
    public static long daysBetween(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            return 0;
        }
        return ChronoUnit.DAYS.between(startDate, endDate);
    }

    /**
     * 计算两个LocalDateTime之间的小时差
     * @param startDateTime 开始日期时间
     * @param endDateTime 结束日期时间
     * @return 小时差
     */
    public static long hoursBetween(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        if (startDateTime == null || endDateTime == null) {
            return 0;
        }
        return ChronoUnit.HOURS.between(startDateTime, endDateTime);
    }

    /**
     * 计算两个LocalDateTime之间的分钟差
     * @param startDateTime 开始日期时间
     * @param endDateTime 结束日期时间
     * @return 分钟差
     */
    public static long minutesBetween(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        if (startDateTime == null || endDateTime == null) {
            return 0;
        }
        return ChronoUnit.MINUTES.between(startDateTime, endDateTime);
    }

    /**
     * 计算两个LocalDateTime之间的秒数差
     * @param startDateTime 开始日期时间
     * @param endDateTime 结束日期时间
     * @return 秒数差
     */
    public static long secondsBetween(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        if (startDateTime == null || endDateTime == null) {
            return 0;
        }
        return ChronoUnit.SECONDS.between(startDateTime, endDateTime);
    }

    /**
     * 给LocalDate添加指定天数
     * @param date LocalDate对象
     * @param days 要添加的天数
     * @return 新的LocalDate对象
     */
    public static LocalDate addDays(LocalDate date, long days) {
        if (date == null) {
            return null;
        }
        return date.plusDays(days);
    }

    /**
     * 给LocalDateTime添加指定小时数
     * @param dateTime LocalDateTime对象
     * @param hours 要添加的小时数
     * @return 新的LocalDateTime对象
     */
    public static LocalDateTime addHours(LocalDateTime dateTime, long hours) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.plusHours(hours);
    }

    /**
     * 给LocalDateTime添加指定分钟数
     * @param dateTime LocalDateTime对象
     * @param minutes 要添加的分钟数
     * @return 新的LocalDateTime对象
     */
    public static LocalDateTime addMinutes(LocalDateTime dateTime, long minutes) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.plusMinutes(minutes);
    }

    /**
     * 给LocalDateTime添加指定秒数
     * @param dateTime LocalDateTime对象
     * @param seconds 要添加的秒数
     * @return 新的LocalDateTime对象
     */
    public static LocalDateTime addSeconds(LocalDateTime dateTime, long seconds) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.plusSeconds(seconds);
    }

    /**
     * 检查两个LocalDate是否为同一天
     * @param date1 第一个日期
     * @param date2 第二个日期
     * @return 如果是同一天返回true，否则返回false
     */
    public static boolean isSameDay(LocalDate date1, LocalDate date2) {
        if (date1 == null || date2 == null) {
            return false;
        }
        return date1.isEqual(date2);
    }

    /**
     * 检查两个LocalDateTime是否为同一天
     * @param dateTime1 第一个日期时间
     * @param dateTime2 第二个日期时间
     * @return 如果是同一天返回true，否则返回false
     */
    public static boolean isSameDay(LocalDateTime dateTime1, LocalDateTime dateTime2) {
        if (dateTime1 == null || dateTime2 == null) {
            return false;
        }
        return dateTime1.toLocalDate().isEqual(dateTime2.toLocalDate());
    }

    /**
     * 检查日期是否为今天
     * @param date LocalDate对象
     * @return 如果是今天返回true，否则返回false
     */
    public static boolean isToday(LocalDate date) {
        if (date == null) {
            return false;
        }
        return date.isEqual(LocalDate.now());
    }

    /**
     * 检查日期时间是否为今天
     * @param dateTime LocalDateTime对象
     * @return 如果是今天返回true，否则返回false
     */
    public static boolean isToday(LocalDateTime dateTime) {
        if (dateTime == null) {
            return false;
        }
        return dateTime.toLocalDate().isEqual(LocalDate.now());
    }

    /**
     * 根据生日计算年龄
     * @param birthday 生日LocalDate
     * @return 年龄
     */
    public static int getAge(LocalDate birthday) {
        if (birthday == null) {
            return 0;
        }
        return (int) ChronoUnit.YEARS.between(birthday, LocalDate.now());
    }

    /**
     * 获取月份的第一天
     * @param date LocalDate对象
     * @return 月份的第一天
     */
    public static LocalDate firstDayOfMonth(LocalDate date) {
        if (date == null) {
            return null;
        }
        return date.withDayOfMonth(1);
    }

    /**
     * 获取月份的最后一天
     * @param date LocalDate对象
     * @return 月份的最后一天
     */
    public static LocalDate lastDayOfMonth(LocalDate date) {
        if (date == null) {
            return null;
        }
        return date.withDayOfMonth(date.lengthOfMonth());
    }
}
