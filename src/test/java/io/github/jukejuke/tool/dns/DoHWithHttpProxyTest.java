package io.github.jukejuke.tool.dns;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.net.Proxy;
import java.util.List;

class DoHWithHttpProxyTest {

    @Test
    void queryWithProxy() throws Exception {
        // 注意：请根据实际环境修改代理服务器地址和端口
        DoHWithHttpProxy dohClient = new DoHWithHttpProxy();
        List<String> ips = dohClient.queryWithProxy(
            "192.168.0.26",  // 代理服务器主机名
            2222,             // 代理服务器端口
            Proxy.Type.SOCKS,
            "dns.alidns.com", // DoH服务主机
            "www.163.com",  // 查询域名
            "A"               // DNS记录类型
        );

        assertNotNull(ips, "查询结果不应为null");
        assertFalse(ips.isEmpty(), "IP列表不应为空");
        System.out.println("查询到的IP地址: " + ips);
    }

    @Test
    void testQueryWithHttpProxy() throws Exception {
        DoHWithHttpProxy dohClient = new DoHWithHttpProxy();
        List<String> ips = dohClient.queryWithProxy(
            "192.168.0.26",  // 代理服务器主机名
            2222,             // 代理服务器端口
            "dns.alidns.com", // DoH服务主机
            "www.163.com",  // 查询域名
            "A"               // DNS记录类型
        );

        assertNotNull(ips, "HTTP代理查询结果不应为null");
        assertFalse(ips.isEmpty(), "HTTP代理IP列表不应为空");
        System.out.println("HTTP代理查询到的IP地址: " + ips);
    }

    @Test
    void testQueryWithHttpProxyDefaultRecordType() throws Exception {
        DoHWithHttpProxy dohClient = new DoHWithHttpProxy();
        List<String> ips = dohClient.queryWithProxy(
            "192.168.0.26",  // 代理服务器主机名
            2222,             // 代理服务器端口
            "dns.alidns.com", // DoH服务主机
            "www.163.com"   // 查询域名 (默认A记录)
        );

        assertNotNull(ips, "默认A记录查询结果不应为null");
        assertFalse(ips.isEmpty(), "默认A记录IP列表不应为空");
        System.out.println("默认A记录查询到的IP地址: " + ips);
    }

    @Test
    void testQueryWithSocksProxy() throws Exception {
        DoHWithHttpProxy dohClient = new DoHWithHttpProxy();
        List<String> ips = dohClient.queryWithSocksProxy(
            "192.168.0.26",  // 代理服务器主机名
            2222,             // 代理服务器端口
            "dns.alidns.com", // DoH服务主机
            "www.163.com",  // 查询域名
            "A"               // DNS记录类型
        );

        assertNotNull(ips, "SOCKS代理查询结果不应为null");
        assertFalse(ips.isEmpty(), "SOCKS代理IP列表不应为空");
        System.out.println("SOCKS代理查询到的IP地址: " + ips);
    }

    @Test
    void testQueryWithSocksProxyDefaultRecordType() throws Exception {
        DoHWithHttpProxy dohClient = new DoHWithHttpProxy();
        List<String> ips = dohClient.queryWithSocksProxy(
            "192.168.0.26",  // 代理服务器主机名
            2222,             // 代理服务器端口
            "dns.alidns.com", // DoH服务主机
            "www.163.com"   // 查询域名 (默认A记录)
        );

        assertNotNull(ips, "SOCKS默认A记录查询结果不应为null");
        assertFalse(ips.isEmpty(), "SOCKS默认A记录IP列表不应为空");
        System.out.println("SOCKS默认A记录查询到的IP地址: " + ips);
    }
}