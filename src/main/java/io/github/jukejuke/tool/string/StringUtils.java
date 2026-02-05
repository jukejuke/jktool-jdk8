package io.github.jukejuke.tool.string;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.StringJoiner;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 字符串工具类，提供常用的字符串操作方法
 */
public class StringUtils {
    /**
     * 判断字符串是否为null或空字符串
     * @param str 要判断的字符串
     * @return 如果字符串为null或空字符串则返回true，否则返回false
     */
    public static boolean isEmpty(String str) {
        return str == null || str.isEmpty();
    }

    /**
     * 判断字符串是否为null、空字符串或仅包含空白字符
     * @param str 要判断的字符串
     * @return 如果字符串为null、空字符串或仅包含空白字符则返回true，否则返回false
     */
    public static boolean isBlank(String str) {
        return str == null || str.trim().isEmpty();
    }

    /**
     * 判断字符串是否不为null且不为空字符串
     * @param str 要判断的字符串
     * @return 如果字符串不为null且不为空字符串则返回true，否则返回false
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    /**
     * 判断字符串是否不为null、空字符串且不全是空白字符
     * @param str 要判断的字符串
     * @return 如果字符串不为null、空字符串且不全是空白字符则返回true，否则返回false
     */
    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }

    /**
     * 判断两个字符串是否相等
     * @param str1 第一个字符串
     * @param str2 第二个字符串
     * @return 如果两个字符串相等则返回true，否则返回false
     */
    public static boolean equals(String str1, String str2) {
        if (str1 == str2) {
            return true;
        }
        if (str1 == null || str2 == null) {
            return false;
        }
        return str1.equals(str2);
    }

    /**
     * 判断两个字符串是否相等（忽略大小写）
     * @param str1 第一个字符串
     * @param str2 第二个字符串
     * @return 如果两个字符串相等则返回true，否则返回false
     */
    public static boolean equalsIgnoreCase(String str1, String str2) {
        if (str1 == str2) {
            return true;
        }
        if (str1 == null || str2 == null) {
            return false;
        }
        return str1.equalsIgnoreCase(str2);
    }

    /**
     * 判断两个字符串是否相等（修剪后比较）
     * @param str1 第一个字符串
     * @param str2 第二个字符串
     * @return 如果两个字符串修剪后相等则返回true，否则返回false
     */
    public static boolean equalsTrim(String str1, String str2) {
        if (str1 == str2) {
            return true;
        }
        if (str1 == null || str2 == null) {
            return false;
        }
        return str1.trim().equals(str2.trim());
    }

    /**
     * 修剪字符串，去除两端的空白字符
     * @param str 要修剪的字符串
     * @return 修剪后的字符串，如果原字符串为null则返回null
     */
    public static String trim(String str) {
        return str != null ? str.trim() : null;
    }

    /**
     * 修剪字符串，如果修剪后的字符串为空则返回null
     * @param str 要修剪的字符串
     * @return 修剪后的字符串，如果原字符串为null或修剪后为空则返回null
     */
    public static String trimToNull(String str) {
        String trimmed = trim(str);
        return isEmpty(trimmed) ? null : trimmed;
    }

    /**
     * 修剪字符串，如果原字符串为null则返回空字符串
     * @param str 要修剪的字符串
     * @return 修剪后的字符串，如果原字符串为null则返回空字符串
     */
    public static String trimToEmpty(String str) {
        return str != null ? str.trim() : "";
    }

    /**
     * 分割字符串
     * @param str 要分割的字符串
     * @param delimiter 分隔符
     * @return 分割后的字符串数组，如果原字符串为null则返回空数组
     */
    public static String[] split(String str, String delimiter) {
        if (isEmpty(str)) {
            return new String[0];
        }
        return str.split(delimiter);
    }

    /**
     * 分割字符串并去除每个元素的空白字符
     * @param str 要分割的字符串
     * @param delimiter 分隔符
     * @return 分割后的字符串列表，如果原字符串为null则返回空列表
     */
    public static List<String> splitToList(String str, String delimiter) {
        if (isEmpty(str)) {
            return Collections.emptyList();
        }
        return Arrays.stream(str.split(delimiter))
                .map(String::trim)
                .filter(StringUtils::isNotEmpty)
                .collect(Collectors.toList());
    }

    /**
     * 连接字符串数组
     * @param array 字符串数组
     * @param delimiter 连接符
     * @return 连接后的字符串，如果数组为null或空则返回空字符串
     */
    public static String join(String[] array, String delimiter) {
        if (array == null || array.length == 0) {
            return "";
        }
        StringJoiner joiner = new StringJoiner(delimiter);
        for (String str : array) {
            if (str != null) {
                joiner.add(str);
            }
        }
        return joiner.toString();
    }

    /**
     * 连接字符串列表
     * @param list 字符串列表
     * @param delimiter 连接符
     * @return 连接后的字符串，如果列表为null或空则返回空字符串
     */
    public static String join(List<String> list, String delimiter) {
        if (list == null || list.isEmpty()) {
            return "";
        }
        StringJoiner joiner = new StringJoiner(delimiter);
        for (String str : list) {
            if (str != null) {
                joiner.add(str);
            }
        }
        return joiner.toString();
    }

    /**
     * 替换字符串中的所有指定字符
     * @param str 原字符串
     * @param oldChar 要替换的字符
     * @param newChar 替换后的字符
     * @return 替换后的字符串，如果原字符串为null则返回null
     */
    public static String replace(String str, char oldChar, char newChar) {
        return str != null ? str.replace(oldChar, newChar) : null;
    }

    /**
     * 替换字符串中的所有指定子串
     * @param str 原字符串
     * @param oldStr 要替换的子串
     * @param newStr 替换后的子串
     * @return 替换后的字符串，如果原字符串为null则返回null
     */
    public static String replace(String str, String oldStr, String newStr) {
        return str != null ? str.replace(oldStr, newStr) : null;
    }

    /**
     * 去除字符串中的所有空白字符
     * @param str 原字符串
     * @return 去除空白字符后的字符串，如果原字符串为null则返回null
     */
    public static String removeWhitespace(String str) {
        if (str == null) {
            return null;
        }
        return str.replaceAll("\\s+", "");
    }

    /**
     * 判断字符串是否为纯数字
     * @param str 要判断的字符串
     * @return 如果字符串为纯数字则返回true，否则返回false
     */
    public static boolean isNumeric(String str) {
        if (isBlank(str)) {
            return false;
        }
        return Pattern.matches("\\d+", str);
    }

    /**
     * 判断字符串是否为浮点数
     * @param str 要判断的字符串
     * @return 如果字符串为浮点数则返回true，否则返回false
     */
    public static boolean isFloat(String str) {
        if (isBlank(str)) {
            return false;
        }
        return Pattern.matches("[+-]?\\d*\\.\\d+", str);
    }

    /**
     * 判断字符串是否为整数或浮点数
     * @param str 要判断的字符串
     * @return 如果字符串为数字则返回true，否则返回false
     */
    public static boolean isNumber(String str) {
        if (isBlank(str)) {
            return false;
        }
        return Pattern.matches("[+-]?\\d+(\\.\\d+)?", str);
    }

    /**
     * 将字符串转换为大写
     * @param str 原字符串
     * @return 大写字符串，如果原字符串为null则返回null
     */
    public static String toUpperCase(String str) {
        return str != null ? str.toUpperCase() : null;
    }

    /**
     * 将字符串转换为小写
     * @param str 原字符串
     * @return 小写字符串，如果原字符串为null则返回null
     */
    public static String toLowerCase(String str) {
        return str != null ? str.toLowerCase() : null;
    }

    /**
     * 将字符串首字母大写，其余小写
     * @param str 原字符串
     * @return 首字母大写的字符串，如果原字符串为null则返回null
     */
    public static String capitalize(String str) {
        if (isBlank(str)) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }

    /**
     * 截取字符串
     * @param str 原字符串
     * @param start 起始索引
     * @return 截取后的字符串，如果原字符串为null则返回null
     */
    public static String substring(String str, int start) {
        if (str == null) {
            return null;
        }
        return str.substring(start);
    }

    /**
     * 截取字符串
     * @param str 原字符串
     * @param start 起始索引
     * @param end 结束索引
     * @return 截取后的字符串，如果原字符串为null则返回null
     */
    public static String substring(String str, int start, int end) {
        if (str == null) {
            return null;
        }
        return str.substring(start, end);
    }

    /**
     * 计算字符串中指定子串的出现次数
     * @param str 原字符串
     * @param subStr 要查找的子串
     * @return 子串的出现次数，如果原字符串或子串为null则返回0
     */
    public static int countOccurrences(String str, String subStr) {
        if (isEmpty(str) || isEmpty(subStr)) {
            return 0;
        }
        int count = 0;
        int index = 0;
        while ((index = str.indexOf(subStr, index)) != -1) {
            count++;
            index += subStr.length();
        }
        return count;
    }

    /**
     * 左填充字符串
     * @param str 原字符串
     * @param length 目标长度
     * @param padChar 填充字符
     * @return 填充后的字符串
     */
    public static String leftPad(String str, int length, char padChar) {
        if (str == null) {
            str = "";
        }
        StringBuilder sb = new StringBuilder(str);
        while (sb.length() < length) {
            sb.insert(0, padChar);
        }
        return sb.toString();
    }

    /**
     * 右填充字符串
     * @param str 原字符串
     * @param length 目标长度
     * @param padChar 填充字符
     * @return 填充后的字符串
     */
    public static String rightPad(String str, int length, char padChar) {
        if (str == null) {
            str = "";
        }
        StringBuilder sb = new StringBuilder(str);
        while (sb.length() < length) {
            sb.append(padChar);
        }
        return sb.toString();
    }

    /**
     * 反转字符串
     * @param str 原字符串
     * @return 反转后的字符串，如果原字符串为null则返回null
     */
    public static String reverse(String str) {
        if (str == null) {
            return null;
        }
        return new StringBuilder(str).reverse().toString();
    }
}