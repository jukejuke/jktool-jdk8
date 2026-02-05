package io.github.jukejuke.tool.license;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HardwareUtilsTest {

    @Test
    void testGenerateHardwareId() {
        // 测试生成硬件ID
        String hardwareId = HardwareUtils.generateHardwareId();
        assertNotNull(hardwareId);
        assertFalse(hardwareId.isEmpty());
        // 硬件ID应该是32位的十六进制字符串
        assertEquals(32, hardwareId.length());
        // 验证是否只包含十六进制字符
        assertTrue(hardwareId.matches("[0-9A-F]{32}"));
        
        // 多次调用应该返回相同的硬件ID
        String hardwareId2 = HardwareUtils.generateHardwareId();
        System.out.println(hardwareId);
        assertEquals(hardwareId, hardwareId2);
    }

    @Test
    void testGetOsName() {
        // 测试获取操作系统名称
        String osName = HardwareUtils.getOsName();
        System.out.println(osName);
        assertNotNull(osName);
        assertFalse(osName.isEmpty());
        // 应该包含操作系统的基本信息
        assertTrue(osName.contains("windows") || osName.contains("linux") || osName.contains("mac"));
    }

    @Test
    void testGetMacAddresses() {
        // 测试获取MAC地址列表
        List<String> macAddresses = HardwareUtils.getMacAddresses();
        System.out.println(macAddresses);
        assertNotNull(macAddresses);
        // 至少应该有一个MAC地址
        assertFalse(macAddresses.isEmpty(), "至少应该能获取到一个MAC地址");
        
        // 验证每个MAC地址的格式是否正确
        for (String macAddress : macAddresses) {
            assertNotNull(macAddress);
            assertFalse(macAddress.isEmpty());
            // MAC地址格式验证：XX:XX:XX:XX:XX:XX
            assertTrue(macAddress.matches("([0-9A-Fa-f]{2}:){5}[0-9A-Fa-f]{2}"),
                    "MAC地址格式不正确: " + macAddress);
        }
    }

    @Test
    void testGetFirstMacAddress() {
        // 测试获取第一个MAC地址
        String firstMacAddress = HardwareUtils.getFirstMacAddress();
        System.out.println(firstMacAddress);
        assertNotNull(firstMacAddress);
        assertFalse(firstMacAddress.isEmpty());
        
        // 验证MAC地址格式是否正确
        assertTrue(firstMacAddress.matches("([0-9A-Fa-f]{2}:){5}[0-9A-Fa-f]{2}"),
                "MAC地址格式不正确: " + firstMacAddress);
    }

    @Test
    void testGetCpuSerial() {
        // 测试获取CPU序列号
        String cpuSerial = HardwareUtils.getCpuSerial();
        System.out.println(cpuSerial);
        // CPU序列号可能获取失败（取决于操作系统和权限），但不应该抛出异常
        assertNotNull(cpuSerial);
    }

    @Test
    void testGetHardDiskSerial() {
        // 测试获取硬盘序列号
        String hardDiskSerial = HardwareUtils.getHardDiskSerial();
        System.out.println(hardDiskSerial);
        // 硬盘序列号可能获取失败（取决于操作系统和权限），但不应该抛出异常
        assertNotNull(hardDiskSerial);
    }
}