package io.github.jukejuke.tool.ip;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * ProxyIPAddressResolver的JUnit测试用例
 */
class ProxyIPAddressResolverTest {

    /**
     * 测试代理解析器的构造函数
     */
    @Test
    void testProxyIPAddressResolverConstruction() {
        // 测试HTTP代理构造
        ProxyIPAddressResolver httpResolver = new ProxyIPAddressResolver("127.0.0.1", 8080);
        assertEquals(Proxy.Type.HTTP, httpResolver.getProxy().type());
        
        // 测试SOCKS代理构造
        ProxyIPAddressResolver socksResolver = new ProxyIPAddressResolver("127.0.0.1", 1080, Proxy.Type.SOCKS);
        assertEquals(Proxy.Type.SOCKS, socksResolver.getProxy().type());
    }

    /**
     * 测试代理解析器的构造函数参数验证
     */
    @Test
    void testProxyIPAddressResolverConstructionValidation() {
        // 测试空主机名
        assertThrows(IllegalArgumentException.class, () -> {
            new ProxyIPAddressResolver("", 8080);
        });
        
        // 测试null主机名
        assertThrows(IllegalArgumentException.class, () -> {
            new ProxyIPAddressResolver(null, 8080);
        });
        
        // 测试无效端口
        assertThrows(IllegalArgumentException.class, () -> {
            new ProxyIPAddressResolver("127.0.0.1", 0);
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            new ProxyIPAddressResolver("127.0.0.1", 65536);
        });
        
        // 测试null代理类型
        assertThrows(IllegalArgumentException.class, () -> {
            new ProxyIPAddressResolver("127.0.0.1", 8080, null);
        });
    }

    /**
     * 测试带认证信息的代理构造函数
     */
    @Test
    void testProxyIPAddressResolverWithAuthentication() {
        // 测试HTTP认证代理构造
        ProxyIPAddressResolver httpAuthResolver = new ProxyIPAddressResolver("127.0.0.1", 8080, "username", "password");
        assertEquals(Proxy.Type.HTTP, httpAuthResolver.getProxy().type());
        
        // 测试SOCKS认证代理构造
        ProxyIPAddressResolver socksAuthResolver = new ProxyIPAddressResolver("127.0.0.1", 1080, "username", "password", Proxy.Type.SOCKS);
        assertEquals(Proxy.Type.SOCKS, socksAuthResolver.getProxy().type());
    }

    /**
     * 测试代理类型的边界值
     */
    @Test
    void testProxyTypes() {
        // 测试所有支持的代理类型
        for (Proxy.Type type : Proxy.Type.values()) {
            if (type == Proxy.Type.HTTP || type == Proxy.Type.SOCKS) {
                ProxyIPAddressResolver resolver = new ProxyIPAddressResolver("127.0.0.1", 8080, type);
                assertEquals(type, resolver.getProxy().type());
            }
        }
    }

    /**
     * 测试端口边界值
     */
    @Test
    void testPortBoundaries() {
        // 测试最小有效端口
        ProxyIPAddressResolver minPortResolver = new ProxyIPAddressResolver("127.0.0.1", 1);
        assertEquals(1, ((InetSocketAddress) minPortResolver.getProxy().address()).getPort());
        
        // 测试最大有效端口
        ProxyIPAddressResolver maxPortResolver = new ProxyIPAddressResolver("127.0.0.1", 65535);
        assertEquals(65535, ((InetSocketAddress) maxPortResolver.getProxy().address()).getPort());
    }

    /**
     * 测试代理地址信息
     */
    @Test
    void testProxyAddress() {
        String proxyHost = "proxy.example.com";
        int proxyPort = 3128;
        ProxyIPAddressResolver resolver = new ProxyIPAddressResolver(proxyHost, proxyPort);
        
        InetSocketAddress address = (InetSocketAddress) resolver.getProxy().address();
        assertEquals(proxyHost, address.getHostString());
        assertEquals(proxyPort, address.getPort());
    }

    /**
     * 测试通过代理解析IP地址的功能
     * 注意：此测试需要实际可用的代理服务器
     */
    @Test
    void testGetIpAddressesWithProxy() {
        // 此测试需要实际可用的代理服务器
        // 如果没有可用代理，可以注释掉或使用本地代理服务器
        // 以下示例使用本地代理服务器（如Fiddler、Charles等）
        

        //ProxyIPAddressResolver resolver = new ProxyIPAddressResolver("127.0.0.1", 8888);
        ProxyIPAddressResolver resolver = new ProxyIPAddressResolver("192.168.0.26", 2222,Proxy.Type.SOCKS);
        try {
            String ip = resolver.getFirstIpAddress("www.163.com");
            System.out.println(ip);

            List<String> ipList = resolver.getIpAddresses("www.163.com");
            assertNotNull(ipList);
            assertFalse(ipList.isEmpty());
        } catch (IOException e) {
            e.printStackTrace();
            // 如果代理服务器不可用，跳过此测试
            System.out.println("Proxy server not available, skipping test: " + e.getMessage());
        }

    }

    /**
     * 测试获取域名是否可解析（可解析的域名）
     */
    @Test
    void testIsResolvableWithProxy() {
        // 此测试需要实际可用的代理服务器
        // 以下示例使用本地代理服务器
        
        /*
        ProxyIPAddressResolver resolver = new ProxyIPAddressResolver("127.0.0.1", 8888);
        boolean isResolvable = resolver.isResolvable("www.baidu.com");
        // 由于代理可能不可用，这里不做严格断言
        System.out.println("Is www.baidu.com resolvable via proxy? " + isResolvable);
        */
    }

    /**
     * 测试IP地址解析器的空域名参数处理
     */
    @Test
    void testEmptyDomain() throws IOException {
        ProxyIPAddressResolver resolver = new ProxyIPAddressResolver("127.0.0.1", 8080);
        
        // 测试getIpAddresses方法
        assertThrows(IllegalArgumentException.class, () -> {
            resolver.getIpAddresses("");
        });
        
        // 测试getFirstIpAddress方法
        assertThrows(IllegalArgumentException.class, () -> {
            resolver.getFirstIpAddress("");
        });
        
        // 测试isResolvable方法
        assertFalse(resolver.isResolvable(""));
    }

    /**
     * 测试IP地址解析器的null域名参数处理
     */
    @Test
    void testNullDomain() throws IOException {
        ProxyIPAddressResolver resolver = new ProxyIPAddressResolver("127.0.0.1", 8080);
        
        // 测试getIpAddresses方法
        assertThrows(IllegalArgumentException.class, () -> {
            resolver.getIpAddresses(null);
        });
        
        // 测试getFirstIpAddress方法
        assertThrows(IllegalArgumentException.class, () -> {
            resolver.getFirstIpAddress(null);
        });
        
        // 测试isResolvable方法
        assertFalse(resolver.isResolvable(null));
    }
}
