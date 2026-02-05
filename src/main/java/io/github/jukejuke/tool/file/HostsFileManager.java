package io.github.jukejuke.tool.file;

import java.io.*;
import java.nio.file.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.*;

/**
 * 本地hosts文件管理器
 * 用于管理Windows系统的hosts文件（C:\Windows\System32\drivers\etc\hosts）
 */
public class HostsFileManager {

    /**
     * 默认Windows hosts文件路径
     */
    public static final String DEFAULT_HOSTS_PATH = "C:\\Windows\\System32\\drivers\\etc\\hosts";

    private final String hostsPath;

    /**
     * 使用默认hosts文件路径创建管理器
     */
    public HostsFileManager() {
        this(DEFAULT_HOSTS_PATH);
    }

    /**
     * 使用指定hosts文件路径创建管理器
     * @param hostsPath hosts文件路径
     */
    public HostsFileManager(String hostsPath) {
        if (hostsPath == null || hostsPath.isEmpty()) {
            throw new IllegalArgumentException("Hosts file path cannot be null or empty");
        }
        this.hostsPath = hostsPath;
    }

    /**
     * 读取hosts文件内容
     * @return hosts文件的所有行
     * @throws IOException 如果读取失败
     */
    public List<String> readHostsFile() throws IOException {
        Path path = Paths.get(hostsPath);
        return Files.readAllLines(path);
    }

    /**
     * 写入hosts文件内容
     * @param lines 要写入的行
     * @throws IOException 如果写入失败
     */
    public void writeHostsFile(List<String> lines) throws IOException {
        Path path = Paths.get(hostsPath);
        Files.write(path, lines);
    }

    /**
     * 备份hosts文件
     * @return 备份文件路径
     * @throws IOException 如果备份失败
     */
    public String backupHostsFile() throws IOException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String backupPath = hostsPath + ".bak." + dateFormat.format(new Date());
        
        Path source = Paths.get(hostsPath);
        Path target = Paths.get(backupPath);
        
        Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
        return backupPath;
    }

    /**
     * 恢复hosts文件
     * @param backupPath 备份文件路径
     * @throws IOException 如果恢复失败
     */
    public void restoreHostsFile(String backupPath) throws IOException {
        Path source = Paths.get(backupPath);
        Path target = Paths.get(hostsPath);
        
        Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
    }

    /**
     * 添加域名映射
     * @param ip IP地址
     * @param domain 域名
     * @return 如果添加成功返回true，如果映射已存在返回false
     * @throws IOException 如果操作失败
     */
    public boolean addHostMapping(String ip, String domain) throws IOException {
        if (ip == null || ip.isEmpty() || domain == null || domain.isEmpty()) {
            throw new IllegalArgumentException("IP address and domain cannot be null or empty");
        }

        if (!isValidIpAddress(ip)) {
            throw new IllegalArgumentException("Invalid IP address format: " + ip);
        }

        List<String> lines = readHostsFile();
        
        // 检查是否已存在该域名的映射
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i).trim();
            if (!line.isEmpty() && !line.startsWith("#")) {
                String[] parts = line.split("\\s+");
                if (parts.length >= 2 && parts[1].equals(domain)) {
                    return false; // 映射已存在
                }
            }
        }

        // 添加新的映射行
        lines.add(ip + "\t" + domain);
        writeHostsFile(lines);
        return true;
    }

    /**
     * 删除域名映射
     * @param domain 域名
     * @return 如果删除成功返回true，如果映射不存在返回false
     * @throws IOException 如果操作失败
     */
    public boolean removeHostMapping(String domain) throws IOException {
        if (domain == null || domain.isEmpty()) {
            throw new IllegalArgumentException("Domain cannot be null or empty");
        }

        List<String> lines = readHostsFile();
        List<String> newLines = new ArrayList<>();
        boolean found = false;

        for (String line : lines) {
            String trimmedLine = line.trim();
            if (!trimmedLine.isEmpty() && !trimmedLine.startsWith("#")) {
                String[] parts = trimmedLine.split("\\s+");
                if (parts.length >= 2 && parts[1].equals(domain)) {
                    found = true;
                    continue; // 跳过这一行
                }
            }
            newLines.add(line);
        }

        if (found) {
            writeHostsFile(newLines);
        }
        return found;
    }

    /**
     * 修改域名映射
     * @param oldDomain 旧域名
     * @param newIp 新IP地址
     * @param newDomain 新域名
     * @return 如果修改成功返回true，如果映射不存在返回false
     * @throws IOException 如果操作失败
     */
    public boolean modifyHostMapping(String oldDomain, String newIp, String newDomain) throws IOException {
        if (oldDomain == null || oldDomain.isEmpty() || newIp == null || newIp.isEmpty() || newDomain == null || newDomain.isEmpty()) {
            throw new IllegalArgumentException("Old domain, new IP address and new domain cannot be null or empty");
        }

        if (!isValidIpAddress(newIp)) {
            throw new IllegalArgumentException("Invalid new IP address format: " + newIp);
        }

        List<String> lines = readHostsFile();
        boolean found = false;

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i).trim();
            if (!line.isEmpty() && !line.startsWith("#")) {
                String[] parts = line.split("\\s+");
                if (parts.length >= 2 && parts[1].equals(oldDomain)) {
                    lines.set(i, newIp + "\t" + newDomain);
                    found = true;
                    break;
                }
            }
        }

        if (found) {
            writeHostsFile(lines);
        }
        return found;
    }

    /**
     * 查找域名映射
     * @param domain 域名
     * @return 如果找到返回IP地址，否则返回null
     * @throws IOException 如果读取失败
     */
    public String findHostMapping(String domain) throws IOException {
        if (domain == null || domain.isEmpty()) {
            throw new IllegalArgumentException("Domain cannot be null or empty");
        }

        List<String> lines = readHostsFile();

        for (String line : lines) {
            String trimmedLine = line.trim();
            if (!trimmedLine.isEmpty() && !trimmedLine.startsWith("#")) {
                String[] parts = trimmedLine.split("\\s+");
                if (parts.length >= 2 && parts[1].equals(domain)) {
                    return parts[0];
                }
            }
        }

        return null;
    }

    /**
     * 获取所有域名映射
     * @return 域名映射的Map，key为域名，value为IP地址
     * @throws IOException 如果读取失败
     */
    public Map<String, String> getAllHostMappings() throws IOException {
        List<String> lines = readHostsFile();
        Map<String, String> mappings = new LinkedHashMap<>();

        for (String line : lines) {
            String trimmedLine = line.trim();
            if (!trimmedLine.isEmpty() && !trimmedLine.startsWith("#")) {
                String[] parts = trimmedLine.split("\\s+");
                if (parts.length >= 2) {
                    mappings.put(parts[1], parts[0]);
                }
            }
        }

        return mappings;
    }

    /**
     * 检查IP地址格式是否有效
     * @param ip IP地址
     * @return 如果格式有效返回true，否则返回false
     */
    private boolean isValidIpAddress(String ip) {
        if (ip == null || ip.isEmpty()) {
            return false;
        }

        // 简单的IPv4格式验证
        String ipv4Pattern = "^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";
        Pattern pattern = Pattern.compile(ipv4Pattern);
        Matcher matcher = pattern.matcher(ip);
        return matcher.matches();
    }

    /**
     * 检查hosts文件是否可写
     * @return 如果可写返回true，否则返回false
     */
    public boolean isHostsFileWritable() {
        File hostsFile = new File(hostsPath);
        return hostsFile.exists() && hostsFile.isFile() && hostsFile.canWrite();
    }

    /**
     * 检查hosts文件是否存在
     * @return 如果存在返回true，否则返回false
     */
    public boolean isHostsFileExists() {
        File hostsFile = new File(hostsPath);
        return hostsFile.exists() && hostsFile.isFile();
    }

    /**
     * 获取hosts文件路径
     * @return hosts文件路径
     */
    public String getHostsPath() {
        return hostsPath;
    }
}
