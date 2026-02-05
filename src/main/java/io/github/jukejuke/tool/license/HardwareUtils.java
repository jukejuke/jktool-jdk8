package io.github.jukejuke.tool.license;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.NetworkInterface;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * 硬件信息工具类
 * 提供获取CPU、硬盘、网卡等硬件信息的功能
 */
public class HardwareUtils {
    
    private static final String OS_NAME = System.getProperty("os.name").toLowerCase();
    
    /**
     * 获取CPU序列号
     * @return CPU序列号
     */
    public static String getCpuSerial() {
        String result = "";
        try {
            if (OS_NAME.contains("windows")) {
                // Windows系统获取CPU序列号
                Process process = Runtime.getRuntime().exec(new String[]{"wmic", "cpu", "get", "ProcessorId"});
                process.getOutputStream().close();
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(), "GBK"));
                String line;
                while ((line = reader.readLine()) != null) {
                    line = line.trim();
                    if (line.length() > 0 && !"ProcessorId".equals(line)) {
                        result = line;
                        break;
                    }
                }
                reader.close();
            } else if (OS_NAME.contains("linux")) {
                // Linux系统获取CPU序列号
                Process process = Runtime.getRuntime().exec(new String[]{"cat", "/proc/cpuinfo"});
                process.getOutputStream().close();
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.startsWith("serial")) {
                        String[] parts = line.split(":");
                        if (parts.length > 1) {
                            result = parts[1].trim();
                            break;
                        }
                    }
                }
                reader.close();
            } else if (OS_NAME.contains("mac")) {
                // Mac系统获取CPU序列号
                Process process = Runtime.getRuntime().exec(new String[]{"system_profiler", "SPHardwareDataType"});
                process.getOutputStream().close();
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.contains("Serial Number (system)") || line.contains("Processor ID")) {
                        String[] parts = line.split(":");
                        if (parts.length > 1) {
                            result = parts[1].trim();
                            break;
                        }
                    }
                }
                reader.close();
            }
        } catch (IOException e) {
            // 获取失败时返回空字符串
        }
        return result;
    }
    
    /**
     * 获取硬盘序列号
     * @return 硬盘序列号
     */
    public static String getHardDiskSerial() {
        String result = "";
        try {
            if (OS_NAME.contains("windows")) {
                // Windows系统获取硬盘序列号
                Process process = Runtime.getRuntime().exec(new String[]{"wmic", "diskdrive", "get", "SerialNumber"});
                process.getOutputStream().close();
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(), "GBK"));
                String line;
                while ((line = reader.readLine()) != null) {
                    line = line.trim();
                    if (line.length() > 0 && !"SerialNumber".equals(line)) {
                        result = line;
                        break;
                    }
                }
                reader.close();
            } else if (OS_NAME.contains("linux")) {
                // Linux系统获取硬盘序列号
                Process process = Runtime.getRuntime().exec(new String[]{"fdisk", "-l"});
                process.getOutputStream().close();
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.contains("Disk identifier") || line.contains("UUID")) {
                        String[] parts = line.split(":");
                        if (parts.length > 1) {
                            result = parts[1].trim();
                            break;
                        }
                    }
                }
                reader.close();
            } else if (OS_NAME.contains("mac")) {
                // Mac系统获取硬盘序列号
                Process process = Runtime.getRuntime().exec(new String[]{"diskutil", "info", "/dev/disk0"});
                process.getOutputStream().close();
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.contains("Volume UUID") || line.contains("Serial Number")) {
                        String[] parts = line.split(":");
                        if (parts.length > 1) {
                            result = parts[1].trim();
                            break;
                        }
                    }
                }
                reader.close();
            }
        } catch (IOException e) {
            // 获取失败时返回空字符串
        }
        return result;
    }
    
    /**
     * 获取所有网卡的MAC地址
     * @return MAC地址列表
     */
    public static List<String> getMacAddresses() {
        List<String> macAddresses = new ArrayList<>();
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaces.nextElement();
                // 跳过虚拟网卡和回环接口
                if (networkInterface.isVirtual() || networkInterface.isLoopback() || !networkInterface.isUp()) {
                    continue;
                }
                byte[] macBytes = networkInterface.getHardwareAddress();
                if (macBytes != null && macBytes.length == 6) {
                    StringBuilder macBuilder = new StringBuilder();
                    for (int i = 0; i < macBytes.length; i++) {
                        macBuilder.append(String.format("%02X", macBytes[i]));
                        if (i < macBytes.length - 1) {
                            macBuilder.append(":");
                        }
                    }
                    macAddresses.add(macBuilder.toString());
                }
            }
        } catch (Exception e) {
            // 获取失败时返回空列表
        }
        return macAddresses;
    }
    
    /**
     * 获取第一个有效的MAC地址
     * @return MAC地址
     */
    public static String getFirstMacAddress() {
        List<String> macAddresses = getMacAddresses();
        return macAddresses.isEmpty() ? "" : macAddresses.get(0);
    }
    
    /**
     * 生成唯一的硬件ID
     * 基于CPU序列号、硬盘序列号和MAC地址的组合
     * @return 硬件ID
     */
    public static String generateHardwareId() {
        try {
            // 获取硬件信息
            String cpuSerial = getCpuSerial();
            String hardDiskSerial = getHardDiskSerial();
            String macAddress = getFirstMacAddress();
            
            // 组合硬件信息
            StringBuilder hardwareInfoBuilder = new StringBuilder();
            hardwareInfoBuilder.append(cpuSerial).append("-");
            hardwareInfoBuilder.append(hardDiskSerial).append("-");
            hardwareInfoBuilder.append(macAddress);
            
            // 使用MD5生成唯一ID
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] hashBytes = md5.digest(hardwareInfoBuilder.toString().getBytes());
            
            // 转换为十六进制字符串
            StringBuilder hardwareIdBuilder = new StringBuilder();
            for (byte hashByte : hashBytes) {
                hardwareIdBuilder.append(String.format("%02X", hashByte));
            }
            
            return hardwareIdBuilder.toString();
        } catch (NoSuchAlgorithmException e) {
            // 如果MD5算法不可用，返回空字符串
            return "";
        }
    }
    
    /**
     * 获取操作系统名称
     * @return 操作系统名称
     */
    public static String getOsName() {
        return OS_NAME;
    }
}