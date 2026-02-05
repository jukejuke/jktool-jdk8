package io.github.jukejuke.tool.ip;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * 域名IP地址解析器
 * 用于获取指定域名的IP地址列表
 */
public class IPAddressResolver {

    /**
     * 获取指定域名的所有IP地址
     * @param domain 要解析的域名
     * @return IP地址列表
     * @throws UnknownHostException 如果域名无法解析
     */
    public List<String> getIpAddresses(String domain) throws UnknownHostException {
        if (domain == null || domain.isEmpty()) {
            throw new IllegalArgumentException("Domain name cannot be null or empty");
        }

        InetAddress[] addresses = InetAddress.getAllByName(domain);
        List<String> ipList = new ArrayList<>(addresses.length);

        for (InetAddress address : addresses) {
            ipList.add(address.getHostAddress());
        }

        return ipList;
    }

    /**
     * 获取指定域名的第一个IP地址
     * @param domain 要解析的域名
     * @return 第一个IP地址
     * @throws UnknownHostException 如果域名无法解析
     */
    public String getFirstIpAddress(String domain) throws UnknownHostException {
        if (domain == null || domain.isEmpty()) {
            throw new IllegalArgumentException("Domain name cannot be null or empty");
        }

        InetAddress address = InetAddress.getByName(domain);
        return address.getHostAddress();
    }

    /**
     * 检查域名是否可以解析
     * @param domain 要检查的域名
     * @return 如果可以解析返回true，否则返回false
     */
    public boolean isResolvable(String domain) {
        if (domain == null || domain.isEmpty()) {
            return false;
        }

        try {
            InetAddress.getByName(domain);
            return true;
        } catch (UnknownHostException e) {
            return false;
        }
    }
}
