package io.github.jukejuke.tool.ip;

import org.junit.jupiter.api.Test;

import java.net.UnknownHostException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * IPAddressResolver的JUnit测试用例
 */
class IPAddressResolverTest {

    private final IPAddressResolver resolver = new IPAddressResolver();

    /**
     * 测试获取指定域名的所有IP地址
     */
    @Test
    void testGetIpAddresses() throws UnknownHostException {
        String domain = "www.baidu.com";
        List<String> ipList = resolver.getIpAddresses(domain);
        
        assertNotNull(ipList, "IP地址列表不应为null");
        assertFalse(ipList.isEmpty(), "IP地址列表不应为空");
        
        // 验证每个IP地址格式是否正确
        for (String ip : ipList) {
            assertTrue(isValidIpAddress(ip), "IP地址格式不正确: " + ip);
        }
    }

    /**
     * 测试获取指定域名的第一个IP地址
     */
    @Test
    void testGetFirstIpAddress() throws UnknownHostException {
        String domain = "www.baidu.com";
        String firstIp = resolver.getFirstIpAddress(domain);
        
        assertNotNull(firstIp, "第一个IP地址不应为null");
        assertTrue(isValidIpAddress(firstIp), "第一个IP地址格式不正确: " + firstIp);
    }

    /**
     * 测试域名是否可解析（可解析的域名）
     */
    @Test
    void testIsResolvable() {
        String domain = "www.baidu.com";
        assertTrue(resolver.isResolvable(domain), "可解析的域名应返回true");
    }

    /**
     * 测试域名是否可解析（不可解析的域名）
     */
    @Test
    void testIsNotResolvable() {
        String domain = "this-domain-does-not-exist-12345.com";
        assertFalse(resolver.isResolvable(domain), "不可解析的域名应返回false");
    }

    /**
     * 测试空域名参数（getIpAddresses）
     */
    @Test
    void testGetIpAddressesWithEmptyDomain() {
        assertThrows(IllegalArgumentException.class, () -> {
            resolver.getIpAddresses("");
        }, "空域名应抛出IllegalArgumentException");
    }

    /**
     * 测试null域名参数（getIpAddresses）
     */
    @Test
    void testGetIpAddressesWithNullDomain() {
        assertThrows(IllegalArgumentException.class, () -> {
            resolver.getIpAddresses(null);
        }, "null域名应抛出IllegalArgumentException");
    }

    /**
     * 测试空域名参数（getFirstIpAddress）
     */
    @Test
    void testGetFirstIpAddressWithEmptyDomain() {
        assertThrows(IllegalArgumentException.class, () -> {
            resolver.getFirstIpAddress("");
        }, "空域名应抛出IllegalArgumentException");
    }

    /**
     * 测试null域名参数（getFirstIpAddress）
     */
    @Test
    void testGetFirstIpAddressWithNullDomain() {
        assertThrows(IllegalArgumentException.class, () -> {
            resolver.getFirstIpAddress(null);
        }, "null域名应抛出IllegalArgumentException");
    }

    /**
     * 测试空域名参数（isResolvable）
     */
    @Test
    void testIsResolvableWithEmptyDomain() {
        assertFalse(resolver.isResolvable(""), "空域名应返回false");
    }

    /**
     * 测试null域名参数（isResolvable）
     */
    @Test
    void testIsResolvableWithNullDomain() {
        assertFalse(resolver.isResolvable(null), "null域名应返回false");
    }

    /**
     * 测试解析多个不同的域名
     */
    @Test
    void testResolveMultipleDomains() throws UnknownHostException {
        String[] domains = {
            "www.baidu.com",
            "www.google.com",
            "www.github.com"
        };

        for (String domain : domains) {
            List<String> ipList = resolver.getIpAddresses(domain);
            assertNotNull(ipList, "域名" + domain + "的IP地址列表不应为null");
            assertFalse(ipList.isEmpty(), "域名" + domain + "的IP地址列表不应为空");
        }
    }

    /**
     * 测试解析本地域名
     */
    @Test
    void testResolveLocalhost() throws UnknownHostException {
        // 测试localhost
        String localhostIp = resolver.getFirstIpAddress("localhost");
        assertNotNull(localhostIp, "localhost的IP地址不应为null");
        assertTrue(isValidIpAddress(localhostIp), "localhost的IP地址格式不正确: " + localhostIp);

        // 测试127.0.0.1
        String loopbackIp = resolver.getFirstIpAddress("127.0.0.1");
        assertNotNull(loopbackIp, "127.0.0.1的IP地址不应为null");
        assertTrue(isValidIpAddress(loopbackIp), "127.0.0.1的IP地址格式不正确: " + loopbackIp);
    }

    /**
     * 测试解析IPv6地址
     */
    @Test
    void testResolveIPv6Address() throws UnknownHostException {
        String ipv6Domain = "ipv6.google.com";
        try {
            List<String> ipList = resolver.getIpAddresses(ipv6Domain);
            assertNotNull(ipList, "IPv6域名" + ipv6Domain + "的IP地址列表不应为null");
            assertFalse(ipList.isEmpty(), "IPv6域名" + ipv6Domain + "的IP地址列表不应为空");
        } catch (UnknownHostException e) {
            // 如果网络环境不支持IPv6，跳过此测试
            System.out.println("IPv6 not supported in current environment, skipping test: " + e.getMessage());
        }
    }

    /**
     * 测试批量解析IP地址的性能（非严格性能测试）
     */
    @Test
    void testBatchResolvePerformance() throws UnknownHostException {
        String[] domains = {
            "www.baidu.com",
            "www.google.com",
            "www.github.com",
            "www.microsoft.com",
            "www.apple.com"
        };

        long startTime = System.currentTimeMillis();
        
        for (String domain : domains) {
            resolver.getFirstIpAddress(domain);
        }
        
        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        
        // 这里只是一个简单的性能测试，不做严格断言
        System.out.println("批量解析" + domains.length + "个域名耗时: " + totalTime + "ms");
    }

    /**
     * 验证IP地址格式是否正确
     * @param ipAddress 要验证的IP地址
     * @return 如果IP地址格式正确返回true，否则返回false
     */
    private boolean isValidIpAddress(String ipAddress) {
        if (ipAddress == null || ipAddress.isEmpty()) {
            return false;
        }

        // 简单的IP地址格式验证
        String[] parts = ipAddress.split("\\.");
        if (parts.length != 4) {
            return false;
        }

        for (String part : parts) {
            try {
                int num = Integer.parseInt(part);
                if (num < 0 || num > 255) {
                    return false;
                }
            } catch (NumberFormatException e) {
                return false;
            }
        }

        return true;
    }
}
