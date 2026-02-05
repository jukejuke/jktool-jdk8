package io.github.jukejuke.tool.ip;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 通过代理获取域名IP地址的解析器
 * 支持HTTP代理和SOCKS代理
 */
public class ProxyIPAddressResolver {

    private final Proxy proxy;

    /**
     * 使用HTTP代理创建解析器
     * @param host 代理服务器主机名
     * @param port 代理服务器端口
     */
    public ProxyIPAddressResolver(String host, int port) {
        this(host, port, Proxy.Type.HTTP);
    }

    /**
     * 使用指定类型的代理创建解析器
     * @param host 代理服务器主机名
     * @param port 代理服务器端口
     * @param type 代理类型（HTTP或SOCKS）
     */
    public ProxyIPAddressResolver(String host, int port, Proxy.Type type) {
        if (host == null || host.isEmpty()) {
            throw new IllegalArgumentException("Proxy host cannot be null or empty");
        }
        if (port <= 0 || port > 65535) {
            throw new IllegalArgumentException("Proxy port must be between 1 and 65535");
        }
        if (type == null) {
            throw new IllegalArgumentException("Proxy type cannot be null");
        }
        this.proxy = new Proxy(type, new InetSocketAddress(host, port));
    }

    /**
     * 使用认证代理创建解析器
     * @param host 代理服务器主机名
     * @param port 代理服务器端口
     * @param username 代理用户名
     * @param password 代理密码
     */
    public ProxyIPAddressResolver(String host, int port, String username, String password) {
        this(host, port, username, password, Proxy.Type.HTTP);
    }

    /**
     * 使用认证代理创建解析器
     * @param host 代理服务器主机名
     * @param port 代理服务器端口
     * @param username 代理用户名
     * @param password 代理密码
     * @param type 代理类型（HTTP或SOCKS）
     */
    public ProxyIPAddressResolver(String host, int port, String username, String password, Proxy.Type type) {
        this(host, port, type);
        
        // 设置代理认证信息
        Authenticator.setDefault(new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                if (getRequestorType() == RequestorType.PROXY) {
                    if (getRequestingHost().equalsIgnoreCase(host) && getRequestingPort() == port) {
                        return new PasswordAuthentication(username, password.toCharArray());
                    }
                }
                return null;
            }
        });
    }

    /**
     * 获取指定域名的所有IP地址
     * @param domain 要解析的域名
     * @return IP地址列表
     * @throws IOException 如果解析失败
     */
    public List<String> getIpAddresses(String domain) throws IOException {
        if (domain == null || domain.isEmpty()) {
            throw new IllegalArgumentException("Domain name cannot be null or empty");
        }

        // 使用代理创建Socket连接
        try (Socket socket = new Socket(proxy)) {
            // 设置连接超时时间
            socket.connect(new InetSocketAddress(domain, 80), 5000);
            // 获取连接的本地地址信息
            InetAddress localAddress = socket.getLocalAddress();
            
            // 获取域名的所有IP地址
            InetAddress[] addresses = InetAddress.getAllByName(domain);
            List<String> ipList = new ArrayList<>(addresses.length);

            for (InetAddress address : addresses) {
                ipList.add(address.getHostAddress());
            }

            return ipList;
        }
    }

    /**
     * 获取指定域名的第一个IP地址
     * @param domain 要解析的域名
     * @return 第一个IP地址
     * @throws IOException 如果解析失败
     */
    public String getFirstIpAddress(String domain) throws IOException {
        if (domain == null || domain.isEmpty()) {
            throw new IllegalArgumentException("Domain name cannot be null or empty");
        }

        // 使用代理创建Socket连接
        try (Socket socket = new Socket(proxy)) {
            // 设置连接超时时间
            socket.connect(new InetSocketAddress(domain, 80), 5000);
            // 获取远程IP地址
            InetAddress remoteAddress = socket.getInetAddress();
            return remoteAddress.getHostAddress();
        }
    }

    /**
     * 检查域名是否可以通过代理解析
     * @param domain 要检查的域名
     * @return 如果可以解析返回true，否则返回false
     */
    public boolean isResolvable(String domain) {
        if (domain == null || domain.isEmpty()) {
            return false;
        }

        try {
            getFirstIpAddress(domain);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * 获取当前使用的代理
     * @return 代理对象
     */
    public Proxy getProxy() {
        return proxy;
    }
}
