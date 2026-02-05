package io.github.jukejuke.tool.license;

import com.alibaba.fastjson2.JSON;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class LicenseUtils {
    private final JWSSigner signer;
    private final JWSVerifier verifier;

    /**
     * 使用默认MAC算法(HmacSHA256)初始化LicenseUtils
     * @param secret 256位以上的密钥
     * @throws JOSEException 密钥长度不足时抛出
     */
    public LicenseUtils(String secret) throws JOSEException {
        this.signer = new MACSigner(secret);
        this.verifier = new MACVerifier(secret);
    }

    /**
     * 使用RSA算法初始化LicenseUtils
     * @param privateKey 私钥（用于生成授权码）
     * @param publicKey 公钥（用于验证授权码）
     * @throws JOSEException 密钥无效时抛出
     */
    public LicenseUtils(RSAPrivateKey privateKey, RSAPublicKey publicKey) throws JOSEException {
        this.signer = new RSASSASigner(privateKey);
        this.verifier = new RSASSAVerifier(publicKey);
    }

    /**
     * 自定义签名器和验证器的构造方法
     * @param signer 签名器
     * @param verifier 验证器
     */
    public LicenseUtils(JWSSigner signer, JWSVerifier verifier) {
        this.signer = signer;
        this.verifier = verifier;
    }

    /**
     * 生成授权码
     * @param licenseInfo 授权信息
     * @return 生成的授权码
     * @throws JOSEException 签名异常
     */
    public String generateLicense(LicenseInfo licenseInfo) throws JOSEException {
        // 将LicenseInfo转换为JSON
        String licenseJson = JSON.toJSONString(licenseInfo);

        // 创建JWT声明
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(licenseInfo.getProductName())
                .issueTime(licenseInfo.getIssueDate())
                .expirationTime(licenseInfo.getExpirationDate())
                .claim("license", licenseJson)
                .build();

        // 创建JWS头
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS256);

        // 创建签名JWT
        SignedJWT signedJWT = new SignedJWT(header, claimsSet);
        signedJWT.sign(signer);

        return signedJWT.serialize();
    }

    /**
     * 验证授权码有效性
     * @param licenseCode 授权码
     * @return 验证结果
     */
    public boolean validateLicense(String licenseCode) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(licenseCode);
            // 验证签名
            if (!signedJWT.verify(verifier)) {
                return false;
            }

            // 验证过期时间
            Date expirationTime = signedJWT.getJWTClaimsSet().getExpirationTime();
            if (expirationTime != null && new Date().after(expirationTime)) {
                return false;
            }

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 从授权码中提取授权信息
     * @param licenseCode 授权码
     * @return 授权信息
     * @throws Exception 解析异常
     */
    public LicenseInfo parseLicense(String licenseCode) throws Exception {
        SignedJWT signedJWT = SignedJWT.parse(licenseCode);
        if (!signedJWT.verify(verifier)) {
            throw new SecurityException("Invalid license signature");
        }

        // 检查过期时间
        Date expirationTime = signedJWT.getJWTClaimsSet().getExpirationTime();
        if (expirationTime != null && new Date().after(expirationTime)) {
            throw new SecurityException("License has expired");
        }

        // 获取授权信息
        String licenseJson = signedJWT.getJWTClaimsSet().getStringClaim("license");
        return JSON.parseObject(licenseJson, LicenseInfo.class);
    }

    /**
     * 验证授权码是否绑定到指定硬件
     * @param licenseCode 授权码
     * @param hardwareId 硬件ID
     * @return 验证结果
     * @throws Exception 解析异常
     */
    public boolean validateHardwareBinding(String licenseCode, String hardwareId) throws Exception {
        LicenseInfo licenseInfo = parseLicense(licenseCode);
        // 如果授权信息没有绑定硬件ID，则认为验证通过
        if (licenseInfo.getHardwareId() == null || licenseInfo.getHardwareId().isEmpty()) {
            return true;
        }
        return hardwareId.equals(licenseInfo.getHardwareId());
    }

    /**
     * 检查授权码是否包含特定功能
     * @param licenseCode 授权码
     * @param featureName 功能名称
     * @return 功能是否可用
     * @throws Exception 解析异常
     */
    public boolean hasFeature(String licenseCode, String featureName) throws Exception {
        LicenseInfo licenseInfo = parseLicense(licenseCode);
        return licenseInfo.hasFeature(featureName);
    }

    /**
     * 获取授权码的过期时间
     * @param licenseCode 授权码
     * @return 过期时间
     * @throws Exception 解析异常
     */
    public Date getExpirationTime(String licenseCode) throws Exception {
        LicenseInfo licenseInfo = parseLicense(licenseCode);
        return licenseInfo.getExpirationDate();
    }

    /**
     * 生成简单的授权码
     * @param productName 产品名称
     * @param licensee 被授权人
     * @param expirationDate 过期时间
     * @return 生成的授权码
     * @throws JOSEException 签名异常
     */
    public String generateSimpleLicense(String productName, String licensee, Date expirationDate) 
            throws JOSEException {
        LicenseInfo licenseInfo = new LicenseInfo();
        licenseInfo.setProductName(productName);
        licenseInfo.setLicensee(licensee);
        licenseInfo.setExpirationDate(expirationDate);
        return generateLicense(licenseInfo);
    }
}