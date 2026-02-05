package io.github.jukejuke.tool.dns;

import java.util.List;

public class DoHQueryTest {
    public static void main(String[] args) {
        try {
            // 创建DoHQuery实例
            DoHQuery dohQuery = new DoHQuery();
            
            // 使用Cloudflare的DoH服务
            //String dohHost = "cloudflare-dns.com";
            String dohHost = "dns.alidns.com";
            //String domain = "1-7.eq-workplace.zeiss.com.cn";
            String domain = "zeissid-cdn.azureedge.net";
            
            System.out.println("Testing DoHQuery for " + domain);
            System.out.println("Using DoH host: " + dohHost);
            
            // 测试A记录查询
            System.out.println("\n=== A Records ===");
            List<String> ipv4Addresses = dohQuery.query(dohHost, domain, "A");
            if (ipv4Addresses.isEmpty()) {
                System.out.println("No A records found");
            } else {
                for (String ip : ipv4Addresses) {
                    System.out.println(ip);
                }
            }
            
            // 测试AAAA记录查询
            System.out.println("\n=== AAAA Records ===");
            List<String> ipv6Addresses = dohQuery.query(dohHost, domain, "AAAA");
            if (ipv6Addresses.isEmpty()) {
                System.out.println("No AAAA records found");
            } else {
                for (String ip : ipv6Addresses) {
                    System.out.println(ip);
                }
            }
            
            // 测试默认记录类型查询（应该是A记录）
            System.out.println("\n=== Default Records (should be A) ===");
            List<String> defaultAddresses = dohQuery.query(dohHost, domain);
            if (defaultAddresses.isEmpty()) {
                System.out.println("No records found");
            } else {
                for (String ip : defaultAddresses) {
                    System.out.println(ip);
                }
            }
            
            System.out.println("\nTest completed successfully!");
            
        } catch (Exception e) {
            System.err.println("Error during test: " + e.getMessage());
            e.printStackTrace();
        }
    }
}