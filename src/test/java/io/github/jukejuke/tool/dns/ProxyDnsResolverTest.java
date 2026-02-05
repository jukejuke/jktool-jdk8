package io.github.jukejuke.tool.dns;

import java.util.List;

public class ProxyDnsResolverTest {
    
    public static void main(String[] args) {
        System.out.println("=== ProxyDnsResolver Test ===\n");
        
        try {
            // 测试基本DNS解析功能
            testBasicDnsResolution();
            
            // 测试不同域名
            testDifferentDomains();
            
            // 测试错误情况
            testErrorScenarios();
            
            System.out.println("\n=== All tests completed successfully! ===");
            
        } catch (Exception e) {
            System.err.println("Error during test: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * 测试基本DNS解析功能
     */
    private static void testBasicDnsResolution() throws Exception {
        System.out.println("1. Testing basic DNS resolution...");
        
        // 使用Google公共DNS服务器
        //String dnsServer = "8.8.8.8";
        String dnsServer="61.139.2.69";
        //String domain = "google.com";
        String domain = "baidu.com";
        
        // 注意：这里需要实际的SOCKS代理服务器才能测试
        // 如果没有可用的代理，可以注释掉这部分测试
        String proxyHost = "192.168.0.26"; // 本地代理服务器
        int proxyPort = 2222; // 默认SOCKS端口
        
        System.out.println("   Domain: " + domain);
        System.out.println("   DNS Server: " + dnsServer);
        System.out.println("   Proxy: " + proxyHost + ":" + proxyPort);
        
        try {
            List<String> ipList = ProxyDnsResolver.resolveViaDnsJava(domain, dnsServer, proxyHost, proxyPort);
            
            if (ipList.isEmpty()) {
                System.out.println("   Result: No IP addresses found");
            } else {
                System.out.println("   Result: Found " + ipList.size() + " IP address(es)");
                for (String ip : ipList) {
                    System.out.println("     - " + ip);
                }
            }
            
        } catch (Exception e) {
            System.out.println("   Result: Failed - " + e.getMessage());
            System.out.println("   Note: This is expected if no SOCKS proxy is available");
        }
        
        System.out.println("   ✓ Basic DNS resolution test completed\n");
    }
    
    /**
     * 测试不同域名的解析
     */
    private static void testDifferentDomains() throws Exception {
        System.out.println("2. Testing different domains...");
        
        String[] domains = {
            "github.com",
            "baidu.com", 
            "qq.com"
        };
        
        String dnsServer = "8.8.8.8";
        String proxyHost = "127.0.0.1";
        int proxyPort = 1080;
        
        for (String domain : domains) {
            System.out.println("   Testing domain: " + domain);
            
            try {
                List<String> ipList = ProxyDnsResolver.resolveViaDnsJava(domain, dnsServer, proxyHost, proxyPort);
                
                if (ipList.isEmpty()) {
                    System.out.println("     Result: No IP addresses found");
                } else {
                    System.out.println("     Result: Found " + ipList.size() + " IP address(es)");
                    for (String ip : ipList) {
                        System.out.println("       - " + ip);
                    }
                }
                
            } catch (Exception e) {
                System.out.println("     Result: Failed - " + e.getMessage());
            }
        }
        
        System.out.println("   ✓ Different domains test completed\n");
    }
    
    /**
     * 测试错误情况
     */
    private static void testErrorScenarios() {
        System.out.println("3. Testing error scenarios...");
        
        // 测试无效域名
        testInvalidDomain();
        
        // 测试无效DNS服务器
        testInvalidDnsServer();
        
        // 测试无效代理
        testInvalidProxy();
        
        System.out.println("   ✓ Error scenarios test completed\n");
    }
    
    /**
     * 测试无效域名
     */
    private static void testInvalidDomain() {
        System.out.println("   Testing invalid domain...");
        
        try {
            List<String> ipList = ProxyDnsResolver.resolveViaDnsJava("invalid-domain-that-does-not-exist.xyz", 
                                                                     "8.8.8.8", "127.0.0.1", 1080);
            
            if (ipList.isEmpty()) {
                System.out.println("     Result: No IP addresses found (expected for invalid domain)");
            } else {
                System.out.println("     Result: Unexpectedly found IP addresses");
            }
            
        } catch (Exception e) {
            System.out.println("     Result: Failed as expected - " + e.getMessage());
        }
    }
    
    /**
     * 测试无效DNS服务器
     */
    private static void testInvalidDnsServer() {
        System.out.println("   Testing invalid DNS server...");
        
        try {
            List<String> ipList = ProxyDnsResolver.resolveViaDnsJava("google.com", 
                                                                     "999.999.999.999", "127.0.0.1", 1080);
            
            System.out.println("     Result: Unexpectedly succeeded");
            
        } catch (Exception e) {
            System.out.println("     Result: Failed as expected - " + e.getMessage());
        }
    }
    
    /**
     * 测试无效代理
     */
    private static void testInvalidProxy() {
        System.out.println("   Testing invalid proxy...");
        
        try {
            List<String> ipList = ProxyDnsResolver.resolveViaDnsJava("google.com", 
                                                                     "8.8.8.8", "invalid-proxy-host", 9999);
            
            System.out.println("     Result: Unexpectedly succeeded");
            
        } catch (Exception e) {
            System.out.println("     Result: Failed as expected - " + e.getMessage());
        }
    }
}