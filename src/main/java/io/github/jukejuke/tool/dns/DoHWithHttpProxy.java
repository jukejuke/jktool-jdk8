package io.github.jukejuke.tool.dns;

import okhttp3.*;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.List;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;

public class DoHWithHttpProxy {

    /**
     * 使用HTTP代理进行DNS-over-HTTPS查询
     * @param proxyHost 代理服务器主机名
     * @param proxyPort 代理服务器端口
     * @param dohHost DNS-over-HTTPS服务主机名
     * @param domain 要查询的域名
     * @param recordType DNS记录类型 (如"A", "AAAA")
     * @return 查询到的IP地址列表
     * @throws Exception 当查询过程中发生错误时抛出
     */
    public List<String> queryWithProxy(String proxyHost, int proxyPort, Proxy.Type type,String dohHost, String domain, String recordType) throws Exception {
        // 配置HTTP代理
        Proxy proxy = new Proxy(type, new InetSocketAddress(proxyHost, proxyPort));

        // 构建OkHttpClient实例
        OkHttpClient client = new OkHttpClient.Builder()
                .proxy(proxy)
                .build();

        // 构建DoH查询URL
        HttpUrl url = new HttpUrl.Builder()
                .scheme("https")
                .host(dohHost)
                .addPathSegment("resolve")
                .addQueryParameter("name", domain)
                .addQueryParameter("type", recordType)
                .build();

        // 创建HTTP请求
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Accept", "application/dns-json")
                .build();

        // 发送请求并处理响应
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful() || response.body() == null) {
                throw new Exception("DoH request failed: " + response.code());
            }

            // 解析JSON响应
            String json = response.body().string();
            JSONObject jsonObject = JSONObject.parseObject(json);
            JSONArray answers = jsonObject.getJSONArray("Answer");

            List<String> ips = new ArrayList<>();
            if (answers != null) {
                for (Object answer : answers) {
                    JSONObject answerObj = (JSONObject) answer;
                    String data = answerObj.getString("data");
                    if (isValidIpAddress(data, recordType)) {
                        ips.add(data);
                    }
                }
            }

            return ips;
        }
    }

    /**
     * 使用HTTP代理进行DNS-over-HTTPS查询（默认使用HTTP代理类型）
     * @param proxyHost 代理服务器主机名
     * @param proxyPort 代理服务器端口
     * @param dohHost DNS-over-HTTPS服务主机名
     * @param domain 要查询的域名
     * @param recordType DNS记录类型 (如"A", "AAAA")
     * @return 查询到的IP地址列表
     * @throws Exception 当查询过程中发生错误时抛出
     */
    public List<String> queryWithProxy(String proxyHost, int proxyPort, String dohHost, String domain, String recordType) throws Exception {
        return queryWithProxy(proxyHost, proxyPort, Proxy.Type.HTTP, dohHost, domain, recordType);
    }

    /**
     * 使用HTTP代理进行DNS-over-HTTPS查询（默认使用HTTP代理类型和A记录查询）
     * @param proxyHost 代理服务器主机名
     * @param proxyPort 代理服务器端口
     * @param dohHost DNS-over-HTTPS服务主机名
     * @param domain 要查询的域名
     * @return 查询到的IP地址列表
     * @throws Exception 当查询过程中发生错误时抛出
     */
    public List<String> queryWithProxy(String proxyHost, int proxyPort, String dohHost, String domain) throws Exception {
        return queryWithProxy(proxyHost, proxyPort, Proxy.Type.HTTP, dohHost, domain, "A");
    }

    /**
     * 使用SOCKS代理进行DNS-over-HTTPS查询
     * @param proxyHost 代理服务器主机名
     * @param proxyPort 代理服务器端口
     * @param dohHost DNS-over-HTTPS服务主机名
     * @param domain 要查询的域名
     * @param recordType DNS记录类型 (如"A", "AAAA")
     * @return 查询到的IP地址列表
     * @throws Exception 当查询过程中发生错误时抛出
     */
    public List<String> queryWithSocksProxy(String proxyHost, int proxyPort, String dohHost, String domain, String recordType) throws Exception {
        return queryWithProxy(proxyHost, proxyPort, Proxy.Type.SOCKS, dohHost, domain, recordType);
    }

    /**
     * 使用SOCKS代理进行DNS-over-HTTPS查询（默认A记录查询）
     * @param proxyHost 代理服务器主机名
     * @param proxyPort 代理服务器端口
     * @param dohHost DNS-over-HTTPS服务主机名
     * @param domain 要查询的域名
     * @return 查询到的IP地址列表
     * @throws Exception 当查询过程中发生错误时抛出
     */
    public List<String> queryWithSocksProxy(String proxyHost, int proxyPort, String dohHost, String domain) throws Exception {
        return queryWithProxy(proxyHost, proxyPort, Proxy.Type.SOCKS, dohHost, domain, "A");
    }

    /**
     * 验证IP地址格式是否与请求的记录类型匹配
     * @param ip 待验证的IP地址字符串
     * @param recordType DNS记录类型 (A/AAAA)
     * @return 如果格式匹配则返回true
     */
    private boolean isValidIpAddress(String ip, String recordType) {
        if (ip == null || ip.isEmpty()) {
            return false;
        }
        
        if ("A".equals(recordType)) {
            // IPv4地址正则表达式
            String ipv4Regex = "^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";
            return ip.matches(ipv4Regex);
        } else if ("AAAA".equals(recordType)) {
            // IPv6地址简化正则表达式
            String ipv6Regex = "^([0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}$";
            return ip.matches(ipv6Regex);
        }
        
        // 对于其他记录类型不进行验证
        return true;
    }
}