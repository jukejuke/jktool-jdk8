package io.github.jukejuke.tool.log;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 日志工具类，封装System.out.println功能
 */
public class LogUtil {
    /**
     * 时间格式化器
     */
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * 获取当前时间字符串
     * @return 格式化后的时间字符串
     */
    private static String getCurrentTime() {
        return LocalDateTime.now().format(DATE_TIME_FORMATTER);
    }
    /**
     * 打印字符串
     * @param message 要打印的消息
     */
    public static void print(String message) {
        System.out.print(message);
    }

    /**
     * 打印字符串并换行
     * @param message 要打印的消息
     */
    public static void println(String message) {
        System.out.println(message);
    }

    /**
     * 打印对象并换行
     * @param obj 要打印的对象
     */
    public static void println(Object obj) {
        System.out.println(obj);
    }

    /**
     * 打印换行
     */
    public static void println() {
        System.out.println();
    }

    /**
     * 格式化打印
     * @param format 格式字符串
     * @param args 格式参数
     */
    public static void printf(String format, Object... args) {
        System.out.printf(format, args);
    }

    /**
     * 打印调试信息
     * @param message 调试消息
     */
    public static void debug(String message) {
        System.out.println("[" + getCurrentTime() + "] [DEBUG] " + message);
    }

    /**
     * 打印信息
     * @param message 信息消息
     */
    public static void info(String message) {
        System.out.println("[" + getCurrentTime() + "] [INFO] " + message);
    }

    /**
     * 打印警告信息
     * @param message 警告消息
     */
    public static void warn(String message) {
        System.out.println("[" + getCurrentTime() + "] [WARN] " + message);
    }

    /**
     * 打印错误信息
     * @param message 错误消息
     */
    public static void error(String message) {
        System.err.println("[" + getCurrentTime() + "] [ERROR] " + message);
    }

    /**
     * 打印错误信息和异常
     * @param message 错误消息
     * @param throwable 异常对象
     */
    public static void error(String message, Throwable throwable) {
        System.err.println("[" + getCurrentTime() + "] [ERROR] " + message);
        throwable.printStackTrace();
    }
}