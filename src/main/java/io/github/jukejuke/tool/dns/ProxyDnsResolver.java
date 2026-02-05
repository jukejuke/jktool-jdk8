package io.github.jukejuke.tool.dns;

import org.xbill.DNS.*;
import org.xbill.DNS.utils.base64;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class ProxyDnsResolver {

    /**
     * 使用dnsjava通过代理进行DNS查询 （验证代理，目前未调通），通过dns tcp去解析域名
     * socks代理
     */
    public static List<String> resolveViaDnsJava(String domain, String dnsIp,String proxyHost, int proxyPort) throws Exception {
        List<String> ipList = new ArrayList<>();

        // 创建SOCKS代理
        SocketAddress proxyAddr = new InetSocketAddress(proxyHost, proxyPort);
        Proxy proxy = new Proxy(Proxy.Type.SOCKS, proxyAddr);

        // 创建通过代理的Socket
        try (Socket socket = new Socket(proxy)) {
            // 连接到DNS服务器（这里使用Google的公共DNS）
            //socket.connect(new InetSocketAddress("8.8.8.8", 53), 5000);
            socket.connect(new InetSocketAddress(dnsIp, 53), 5000);

            // 构建DNS查询消息
            Message query = Message.newQuery(org.xbill.DNS.Record.newRecord(
                    Name.fromString(domain + "."),
                    Type.A,
                    DClass.IN
            ));

            // 发送DNS查询
            byte[] queryData = query.toWire();
            OutputStream out = socket.getOutputStream();
            out.write(queryData);
            out.flush();

            // 接收响应
            InputStream in = socket.getInputStream();
            byte[] responseData = new byte[1024];
            int bytesRead = in.read(responseData);

            if (bytesRead > 0) {
                Message response = new Message(responseData);
                org.xbill.DNS.Record[] answers = response.getSectionArray(Section.ANSWER);

                for (org.xbill.DNS.Record record : answers) {
                    if (record instanceof ARecord) {
                        ARecord aRecord = (ARecord) record;
                        ipList.add(aRecord.getAddress().getHostAddress());
                    }
                }
            }
        }

        return ipList;
    }
}
