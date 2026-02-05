package io.github.jukejuke.tool.license;

import com.nimbusds.jose.JOSEException;
import io.github.jukejuke.tool.date.DateUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class LicenseUtilsTest {
    private static final String SECRET_KEY = "60fda43b";
    private LicenseUtils licenseUtils;

    @BeforeEach
    void setUp() throws JOSEException {
        licenseUtils = new LicenseUtils(SECRET_KEY);
    }

    @Test
    void testGenerateAndValidateLicense() throws Exception {
        // 创建授权信息
        LicenseInfo licenseInfo = new LicenseInfo();
        licenseInfo.setProductName("TestProduct");
        licenseInfo.setProductVersion("1.0.0");
        licenseInfo.setLicensee("Test User");
        // 设置1天后过期
        Date expirationDate = new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000);
        licenseInfo.setExpirationDate(expirationDate);
        licenseInfo.setHardwareId("test-hardware-id-123");

        // 生成授权码
        String licenseCode = licenseUtils.generateLicense(licenseInfo);
        assertNotNull(licenseCode);
        assertFalse(licenseCode.isEmpty());

        // 验证授权码
        boolean isValid = licenseUtils.validateLicense(licenseCode);
        assertTrue(isValid);

        // 解析授权码
        LicenseInfo parsedLicense = licenseUtils.parseLicense(licenseCode);
        assertNotNull(parsedLicense);
        assertEquals(licenseInfo.getProductName(), parsedLicense.getProductName());
        assertEquals(licenseInfo.getProductVersion(), parsedLicense.getProductVersion());
        assertEquals(licenseInfo.getLicensee(), parsedLicense.getLicensee());
        assertEquals(licenseInfo.getHardwareId(), parsedLicense.getHardwareId());
    }

    @Test
    void testHardwareBinding() throws Exception {
        // 创建授权信息并绑定硬件
        LicenseInfo licenseInfo = new LicenseInfo();
        licenseInfo.setProductName("TestProduct");
        licenseInfo.setHardwareId("test-hardware-id-123");

        // 生成授权码
        String licenseCode = licenseUtils.generateLicense(licenseInfo);

        // 验证正确的硬件ID
        boolean isValid = licenseUtils.validateHardwareBinding(licenseCode, "test-hardware-id-123");
        assertTrue(isValid);

        // 验证错误的硬件ID
        isValid = licenseUtils.validateHardwareBinding(licenseCode, "wrong-hardware-id");
        assertFalse(isValid);
    }

    @Test
    void testNoHardwareBinding() throws Exception {
        // 创建授权信息，不绑定硬件
        LicenseInfo licenseInfo = new LicenseInfo();
        licenseInfo.setProductName("TestProduct");
        licenseInfo.setHardwareId(null);

        // 生成授权码
        String licenseCode = licenseUtils.generateLicense(licenseInfo);

        // 任何硬件ID都应该通过验证
        boolean isValid = licenseUtils.validateHardwareBinding(licenseCode, "any-hardware-id");
        assertTrue(isValid);
    }

    @Test
    void testFeaturePermissions() throws Exception {
        // 创建功能权限映射
        Map<String, Boolean> features = new HashMap<>();
        features.put("feature1", true);
        features.put("feature2", true);
        features.put("feature3", false);

        // 创建授权信息
        LicenseInfo licenseInfo = new LicenseInfo();
        licenseInfo.setProductName("TestProduct");
        licenseInfo.setFeatures(features);

        // 生成授权码
        String licenseCode = licenseUtils.generateLicense(licenseInfo);

        // 检查功能权限
        boolean hasFeature1 = licenseUtils.hasFeature(licenseCode, "feature1");
        boolean hasFeature2 = licenseUtils.hasFeature(licenseCode, "feature2");
        boolean hasFeature3 = licenseUtils.hasFeature(licenseCode, "feature3");
        boolean hasFeature4 = licenseUtils.hasFeature(licenseCode, "feature4");

        assertTrue(hasFeature1);
        assertTrue(hasFeature2);
        assertFalse(hasFeature3);
        assertFalse(hasFeature4);
    }

    @Test
    void testLicenseExpiration() throws Exception {
        // 创建已过期的授权信息
        LicenseInfo expiredLicense = new LicenseInfo();
        expiredLicense.setProductName("TestProduct");
        // 设置1天前过期
        Date expiredDate = new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000);
        expiredLicense.setExpirationDate(expiredDate);

        // 生成授权码
        String licenseCode = licenseUtils.generateLicense(expiredLicense);

        // 验证授权码应该失败
        boolean isValid = licenseUtils.validateLicense(licenseCode);
        assertFalse(isValid);

        // 解析授权码应该抛出异常
        assertThrows(SecurityException.class, () -> licenseUtils.parseLicense(licenseCode));
    }

    @Test
    void testGenerateSimpleLicense() throws Exception {
        // 生成简单授权码
        Date expirationDate  = DateUtils.toDate(DateUtils.addHours(DateUtils.now(), 24*30));
        String licenseCode = licenseUtils.generateSimpleLicense("test", "SimpleUser", expirationDate);
        System.out.println(licenseCode);
        assertNotNull(licenseCode);
        assertFalse(licenseCode.isEmpty());

        // 验证授权码
        boolean isValid = licenseUtils.validateLicense(licenseCode);
        assertTrue(isValid);

        // 解析授权码
        LicenseInfo licenseInfo = licenseUtils.parseLicense(licenseCode);
        assertNotNull(licenseInfo);
        assertEquals("test", licenseInfo.getProductName());
        assertEquals("SimpleUser", licenseInfo.getLicensee());
        assertNotNull(licenseInfo.getExpirationDate());
    }

    @Test
    void testInvalidLicense() {
        // 验证无效的授权码
        String invalidLicense = "invalid-license-code-1234567890";
        boolean isValid = licenseUtils.validateLicense(invalidLicense);
        assertFalse(isValid);

        // 解析无效的授权码
        assertThrows(Exception.class, () -> licenseUtils.parseLicense(invalidLicense));
    }

    @Test
    void testLicenseInfoExpiredMethod() {
        // 测试LicenseInfo的isExpired方法
        LicenseInfo licenseInfo = new LicenseInfo();
        
        // 未设置过期时间
        assertFalse(licenseInfo.isExpired());
        
        // 设置未来的过期时间
        Date futureDate = new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000);
        licenseInfo.setExpirationDate(futureDate);
        assertFalse(licenseInfo.isExpired());
        
        // 设置过去的过期时间
        Date pastDate = new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000);
        licenseInfo.setExpirationDate(pastDate);
        assertTrue(licenseInfo.isExpired());
    }

    @Test
    void testLicenseInfoHasFeatureMethod() {
        // 测试LicenseInfo的hasFeature方法
        LicenseInfo licenseInfo = new LicenseInfo();
        
        // 未设置功能
        assertFalse(licenseInfo.hasFeature("any-feature"));
        
        // 设置功能映射
        Map<String, Boolean> features = new HashMap<>();
        features.put("enabled-feature", true);
        features.put("disabled-feature", false);
        licenseInfo.setFeatures(features);
        
        assertTrue(licenseInfo.hasFeature("enabled-feature"));
        assertFalse(licenseInfo.hasFeature("disabled-feature"));
        assertFalse(licenseInfo.hasFeature("non-existent-feature"));
    }
}