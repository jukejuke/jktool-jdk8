package io.github.jukejuke.tool.dns;

import org.xbill.DNS.*;
import org.xbill.DNS.ARecord;
import org.xbill.DNS.Record;
import org.xbill.DNS.Type;

import java.util.ArrayList;
import java.util.List;

/**
 * DNS解析工具类，用于通过DNS服务器获取域名对应的IP地址
 */
public class DnsResolver {

    public static List<String> resolveARecord(String hostname, String dnsServer) throws Exception {
        // 设置自定义 DNS 服务器
        SimpleResolver resolver = new SimpleResolver(dnsServer);

        // 创建查询
        Record rec = Record.newRecord(Name.fromString(hostname + "."), Type.A, DClass.IN);
        Message query = Message.newQuery(rec);
        Message response = resolver.send(query);

        List<String> results = new ArrayList<>();
        if (response != null && response.getRcode() == Rcode.NOERROR) {
            Record[] answers = response.getSectionArray(Section.ANSWER);
            for (Record answer : answers) {
                if (answer.getType() == Type.A) {
                    ARecord a = (ARecord) answer;
                    results.add(a.getAddress().getHostAddress());
                }
            }
        }
        return results;
    }
}