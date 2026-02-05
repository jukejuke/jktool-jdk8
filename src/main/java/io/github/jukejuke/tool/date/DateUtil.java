package io.github.jukejuke.tool.date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Date工具类
 * 提供Date对象的格式化、解析、计算和比较等功能
 */
public class DateUtil {

    public static final String FORMAT_DATE = "yyyy-MM-dd";
    public static final String FORMAT_TIME = "HH:mm:ss";
    public static final String FORMAT_DATETIME = "yyyy-MM-dd HH:mm:ss";
    public static final String FORMAT_DATETIME_MILLI = "yyyy-MM-dd HH:mm:ss.SSS";
    public static final String FORMAT_DATE_CHINESE = "yyyy年MM月dd日";
    public static final String FORMAT_DATETIME_CHINESE = "yyyy年MM月dd日 HH:mm:ss";
    public static final String FORMAT_TIME_ONLY = "HH:mm";
    public static final String FORMAT_MONTH_DAY = "MM-dd";
    public static final String FORMAT_YEAR_MONTH = "yyyy-MM";

    /**
     * 将Date格式化为指定模式的字符串
     * @param date Date对象
     * @param pattern 格式模式
     * @return 格式化后的日期字符串
     */
    public static String format(Date date, String pattern) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(date);
    }

    /**
     * 将Date格式化为默认模式(yyyy-MM-dd HH:mm:ss)的字符串
     * @param date Date对象
     * @return 格式化后的日期时间字符串
     */
    public static String format(Date date) {
        return format(date, FORMAT_DATETIME);
    }

    /**
     * 将字符串解析为指定模式的Date
     * @param dateStr 日期字符串
     * @param pattern 格式模式
     * @return Date对象
     */
    public static Date parse(String dateStr, String pattern) {
        if (dateStr == null || dateStr.isEmpty()) {
            return null;
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            return sdf.parse(dateStr);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 将字符串解析为默认模式(yyyy-MM-dd HH:mm:ss)的Date
     * @param dateStr 日期时间字符串
     * @return Date对象
     */
    public static Date parse(String dateStr) {
        return parse(dateStr, FORMAT_DATETIME);
    }

    /**
     * 获取Date的年份
     * @param date Date对象
     * @return 年份
     */
    public static int getYear(Date date) {
        if (date == null) {
            return 0;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.YEAR);
    }

    /**
     * 获取Date的月份（1-12）
     * @param date Date对象
     * @return 月份
     */
    public static int getMonth(Date date) {
        if (date == null) {
            return 0;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.MONTH) + 1;
    }

    /**
     * 获取Date的日期（1-31）
     * @param date Date对象
     * @return 日期
     */
    public static int getDay(Date date) {
        if (date == null) {
            return 0;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 获取Date的小时（0-23）
     * @param date Date对象
     * @return 小时
     */
    public static int getHour(Date date) {
        if (date == null) {
            return 0;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.HOUR_OF_DAY);
    }

    /**
     * 获取Date的分钟（0-59）
     * @param date Date对象
     * @return 分钟
     */
    public static int getMinute(Date date) {
        if (date == null) {
            return 0;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.MINUTE);
    }

    /**
     * 获取Date的秒数（0-59）
     * @param date Date对象
     * @return 秒数
     */
    public static int getSecond(Date date) {
        if (date == null) {
            return 0;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.SECOND);
    }

    /**
     * 获取Date是一周中的第几天（1-7，星期日为1）
     * @param date Date对象
     * @return 一周中的第几天
     */
    public static int getDayOfWeek(Date date) {
        if (date == null) {
            return 0;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_WEEK);
    }

    /**
     * 获取Date是一年中的第几天
     * @param date Date对象
     * @return 一年中的第几天
     */
    public static int getDayOfYear(Date date) {
        if (date == null) {
            return 0;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_YEAR);
    }

    /**
     * 获取Date所在月份的天数
     * @param date Date对象
     * @return 月份天数
     */
    public static int getDaysInMonth(Date date) {
        if (date == null) {
            return 0;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    /**
     * 给Date添加指定年份
     * @param date Date对象
     * @param years 要添加的年份数
     * @return 新的Date对象
     */
    public static Date addYears(Date date, int years) {
        if (date == null) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.YEAR, years);
        return cal.getTime();
    }

    /**
     * 给Date添加指定月份
     * @param date Date对象
     * @param months 要添加的月份数
     * @return 新的Date对象
     */
    public static Date addMonths(Date date, int months) {
        if (date == null) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, months);
        return cal.getTime();
    }

    /**
     * 给Date添加指定天数
     * @param date Date对象
     * @param days 要添加的天数
     * @return 新的Date对象
     */
    public static Date addDays(Date date, int days) {
        if (date == null) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_MONTH, days);
        return cal.getTime();
    }

    /**
     * 给Date添加指定小时数
     * @param date Date对象
     * @param hours 要添加的小时数
     * @return 新的Date对象
     */
    public static Date addHours(Date date, int hours) {
        if (date == null) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.HOUR_OF_DAY, hours);
        return cal.getTime();
    }

    /**
     * 给Date添加指定分钟数
     * @param date Date对象
     * @param minutes 要添加的分钟数
     * @return 新的Date对象
     */
    public static Date addMinutes(Date date, int minutes) {
        if (date == null) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MINUTE, minutes);
        return cal.getTime();
    }

    /**
     * 给Date添加指定秒数
     * @param date Date对象
     * @param seconds 要添加的秒数
     * @return 新的Date对象
     */
    public static Date addSeconds(Date date, int seconds) {
        if (date == null) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.SECOND, seconds);
        return cal.getTime();
    }

    /**
     * 计算两个Date之间的毫秒差
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @return 毫秒差
     */
    public static long millisecondsBetween(Date startDate, Date endDate) {
        if (startDate == null || endDate == null) {
            return 0;
        }
        return endDate.getTime() - startDate.getTime();
    }

    /**
     * 计算两个Date之间的秒差
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @return 秒差
     */
    public static long secondsBetween(Date startDate, Date endDate) {
        return TimeUnit.MILLISECONDS.toSeconds(millisecondsBetween(startDate, endDate));
    }

    /**
     * 计算两个Date之间的分钟差
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @return 分钟差
     */
    public static long minutesBetween(Date startDate, Date endDate) {
        return TimeUnit.MILLISECONDS.toMinutes(millisecondsBetween(startDate, endDate));
    }

    /**
     * 计算两个Date之间的小时差
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @return 小时差
     */
    public static long hoursBetween(Date startDate, Date endDate) {
        return TimeUnit.MILLISECONDS.toHours(millisecondsBetween(startDate, endDate));
    }

    /**
     * 计算两个Date之间的天数差
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @return 天数差
     */
    public static long daysBetween(Date startDate, Date endDate) {
        return TimeUnit.MILLISECONDS.toDays(millisecondsBetween(startDate, endDate));
    }

    /**
     * 比较两个Date的大小
     * @param date1 第一个Date
     * @param date2 第二个Date
     * @return 如果date1等于date2返回0，date1大于date2返回1，否则返回-1
     */
    public static int compare(Date date1, Date date2) {
        if (date1 == null && date2 == null) {
            return 0;
        }
        if (date1 == null) {
            return -1;
        }
        if (date2 == null) {
            return 1;
        }
        return date1.compareTo(date2);
    }

    /**
     * 检查两个Date是否为同一天
     * @param date1 第一个Date
     * @param date2 第二个Date
     * @return 如果是同一天返回true，否则返回false
     */
    public static boolean isSameDay(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            return false;
        }
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
               cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
    }

    /**
     * 检查Date是否为今天
     * @param date Date对象
     * @return 如果是今天返回true，否则返回false
     */
    public static boolean isToday(Date date) {
        if (date == null) {
            return false;
        }
        return isSameDay(date, new Date());
    }

    /**
     * 检查Date是否为昨天
     * @param date Date对象
     * @return 如果是昨天返回true，否则返回false
     */
    public static boolean isYesterday(Date date) {
        if (date == null) {
            return false;
        }
        return isSameDay(date, addDays(new Date(), -1));
    }

    /**
     * 检查Date是否为明天
     * @param date Date对象
     * @return 如果是明天返回true，否则返回false
     */
    public static boolean isTomorrow(Date date) {
        if (date == null) {
            return false;
        }
        return isSameDay(date, addDays(new Date(), 1));
    }

    /**
     * 获取Date所在周的第一天（星期日）
     * @param date Date对象
     * @return 周的第一天
     */
    public static Date firstDayOfWeek(Date date) {
        if (date == null) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        return cal.getTime();
    }

    /**
     * 获取Date所在周的最后一天（星期六）
     * @param date Date对象
     * @return 周的最后一天
     */
    public static Date lastDayOfWeek(Date date) {
        if (date == null) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
        return cal.getTime();
    }

    /**
     * 获取Date所在月的第一天
     * @param date Date对象
     * @return 月的第一天
     */
    public static Date firstDayOfMonth(Date date) {
        if (date == null) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        return cal.getTime();
    }

    /**
     * 获取Date所在月的最后一天
     * @param date Date对象
     * @return 月的最后一天
     */
    public static Date lastDayOfMonth(Date date) {
        if (date == null) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        return cal.getTime();
    }

    /**
     * 获取Date所在年的第一天
     * @param date Date对象
     * @return 年的第一天
     */
    public static Date firstDayOfYear(Date date) {
        if (date == null) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_YEAR, 1);
        return cal.getTime();
    }

    /**
     * 获取Date所在年的最后一天
     * @param date Date对象
     * @return 年的最后一天
     */
    public static Date lastDayOfYear(Date date) {
        if (date == null) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_YEAR, cal.getActualMaximum(Calendar.DAY_OF_YEAR));
        return cal.getTime();
    }

    /**
     * 设置Date的小时、分钟、秒、毫秒为0
     * @param date Date对象
     * @return 时间部分为0的Date
     */
    public static Date truncateTime(Date date) {
        if (date == null) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     * 根据生日计算年龄
     * @param birthday 生日
     * @return 年龄
     */
    public static int getAge(Date birthday) {
        if (birthday == null) {
            return 0;
        }
        Calendar birth = Calendar.getInstance();
        birth.setTime(birthday);
        Calendar now = Calendar.getInstance();
        int age = now.get(Calendar.YEAR) - birth.get(Calendar.YEAR);
        if (now.get(Calendar.DAY_OF_YEAR) < birth.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }
        return age;
    }

    /**
     * 将Date转换为当天的开始时间（00:00:00.000）
     * @param date Date对象
     * @return 当天开始时间
     */
    public static Date getStartOfDay(Date date) {
        return truncateTime(date);
    }

    /**
     * 将Date转换为当天的结束时间（23:59:59.999）
     * @param date Date对象
     * @return 当天结束时间
     */
    public static Date getEndOfDay(Date date) {
        if (date == null) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        return cal.getTime();
    }

    /**
     * 获取友好格式的相对时间描述
     * @param date Date对象
     * @return 相对时间描述，如"刚刚"、"5分钟前"、"3小时前"、"昨天"、"3天前"等
     */
    public static String getFriendlyTime(Date date) {
        if (date == null) {
            return null;
        }
        long diff = System.currentTimeMillis() - date.getTime();
        if (diff < 0) {
            return format(date);
        }
        if (diff < TimeUnit.MINUTES.toMillis(1)) {
            return "刚刚";
        }
        if (diff < TimeUnit.HOURS.toMillis(1)) {
            long minutes = TimeUnit.MILLISECONDS.toMinutes(diff);
            return minutes + "分钟前";
        }
        if (diff < TimeUnit.DAYS.toMillis(1)) {
            long hours = TimeUnit.MILLISECONDS.toHours(diff);
            return hours + "小时前";
        }
        if (diff < TimeUnit.DAYS.toMillis(2)) {
            return "昨天";
        }
        if (diff < TimeUnit.DAYS.toMillis(7)) {
            long days = TimeUnit.MILLISECONDS.toDays(diff);
            return days + "天前";
        }
        return format(date);
    }
}
