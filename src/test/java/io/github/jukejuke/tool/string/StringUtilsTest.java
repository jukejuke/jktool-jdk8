package io.github.jukejuke.tool.string;

/**
 * StringUtils测试类
 */
public class StringUtilsTest {
    public static void main(String[] args) {
        // 测试字符串判空功能
        System.out.println("=== 测试字符串判空功能 ===");
        System.out.println("isEmpty(null): " + StringUtils.isEmpty(null));
        System.out.println("isEmpty(\"\"): " + StringUtils.isEmpty(""));
        System.out.println("isEmpty(\"   \"): " + StringUtils.isEmpty("   "));
        System.out.println("isEmpty(\"abc\"): " + StringUtils.isEmpty("abc"));
        System.out.println("isBlank(null): " + StringUtils.isBlank(null));
        System.out.println("isBlank(\"\"): " + StringUtils.isBlank(""));
        System.out.println("isBlank(\"   \"): " + StringUtils.isBlank("   "));
        System.out.println("isBlank(\"abc\"): " + StringUtils.isBlank("abc"));

        // 测试字符串修剪功能
        System.out.println("\n=== 测试字符串修剪功能 ===");
        System.out.println("trim(null): " + StringUtils.trim(null));
        System.out.println("trim(\"  abc  \"): |" + StringUtils.trim("  abc  ") + "|");
        System.out.println("trimToNull(null): " + StringUtils.trimToNull(null));
        System.out.println("trimToNull(\"   \"): " + StringUtils.trimToNull("   "));
        System.out.println("trimToEmpty(null): |" + StringUtils.trimToEmpty(null) + "|");

        // 测试字符串分割和连接功能
        System.out.println("\n=== 测试字符串分割和连接功能 ===");
        String[] splitResult = StringUtils.split("a,b,c,d", ",");
        System.out.println("split(\"a,b,c,d\", \",\"): " + java.util.Arrays.toString(splitResult));
        System.out.println("join([\"a\", \"b\", \"c\"], \"-\"): " + StringUtils.join(new String[]{"a", "b", "c"}, "-"));

        // 测试字符串相等判断功能
        System.out.println("\n=== 测试字符串相等判断功能 ===");
        System.out.println("equals(null, null): " + StringUtils.equals(null, null));
        System.out.println("equals(null, \"abc\"): " + StringUtils.equals(null, "abc"));
        System.out.println("equals(\"abc\", null): " + StringUtils.equals("abc", null));
        System.out.println("equals(\"abc\", \"abc\"): " + StringUtils.equals("abc", "abc"));
        System.out.println("equals(\"abc\", \"def\"): " + StringUtils.equals("abc", "def"));
        System.out.println("equalsIgnoreCase(\"ABC\", \"abc\"): " + StringUtils.equalsIgnoreCase("ABC", "abc"));
        System.out.println("equalsTrim(\"  abc  \", \"abc\"): " + StringUtils.equalsTrim("  abc  ", "abc"));

        // 测试字符串转换功能
        System.out.println("\n=== 测试字符串转换功能 ===");
        System.out.println("toUpperCase(\"abc\"): " + StringUtils.toUpperCase("abc"));
        System.out.println("toLowerCase(\"ABC\"): " + StringUtils.toLowerCase("ABC"));
        System.out.println("capitalize(\"hello\"): " + StringUtils.capitalize("hello"));
        System.out.println("reverse(\"hello\"): " + StringUtils.reverse("hello"));

        // 测试字符串检查功能
        System.out.println("\n=== 测试字符串检查功能 ===");
        System.out.println("isNumeric(\"123\"): " + StringUtils.isNumeric("123"));
        System.out.println("isNumeric(\"abc\"): " + StringUtils.isNumeric("abc"));
        System.out.println("isNumber(\"123\"): " + StringUtils.isNumber("123"));
        System.out.println("isNumber(\"123.45\"): " + StringUtils.isNumber("123.45"));
        System.out.println("isFloat(\"123.45\"): " + StringUtils.isFloat("123.45"));

        // 测试其他功能
        System.out.println("\n=== 测试其他功能 ===");
        System.out.println("replace(\"hello world\", \"world\", \"java\"): " + StringUtils.replace("hello world", "world", "java"));
        System.out.println("removeWhitespace(\"h e l l o\"): " + StringUtils.removeWhitespace("h e l l o"));
        System.out.println("countOccurrences(\"ababab\", \"ab\"): " + StringUtils.countOccurrences("ababab", "ab"));
        System.out.println("leftPad(\"123\", 5, '0'): " + StringUtils.leftPad("123", 5, '0'));
        System.out.println("rightPad(\"123\", 5, '0'): " + StringUtils.rightPad("123", 5, '0'));
        System.out.println("substring(\"hello\", 2): " + StringUtils.substring("hello", 2));
        System.out.println("substring(\"hello\", 1, 4): " + StringUtils.substring("hello", 1, 4));

        System.out.println("\n=== 测试完成 ===");
    }
}