package io.github.jukejuke.tool.jwt;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Slf4j
public class JwtUtils {
    private final JWSSigner signer;
    private final JWSVerifier verifier;

    /**
     * 使用默认MAC算法(HmacSHA256)初始化JwtTool
     * @param secret 256位以上的密钥
     * @throws JOSEException 密钥长度不足时抛出
     */
    public JwtUtils(String secret) throws JOSEException {
        this.signer = new MACSigner(secret);
        this.verifier = new MACVerifier(secret);
    }

    /**
     * 自定义签名器和验证器的构造方法
     * @param signer 签名器
     * @param verifier 验证器
     */
    public JwtUtils(JWSSigner signer, JWSVerifier verifier) {
        this.signer = signer;
        this.verifier = verifier;
    }

    /**
     * 创建JWT令牌
     * @param userId 用户标识
     * @param customClaims 自定义声明
     * @param expirationTime 过期时间
     * @return 生成的JWT令牌
     * @throws JOSEException 签名异常
     */
    public String createToken(String userId, Map<String, Object> customClaims, Date expirationTime) throws JOSEException {
        // 创建JWT声明集
        JWTClaimsSet.Builder claimsBuilder = new JWTClaimsSet.Builder()
                .subject(userId)
                .expirationTime(expirationTime)
                .issueTime(new Date())
                .jwtID(UUID.randomUUID().toString());

        // 添加自定义声明
        if (customClaims != null && !customClaims.isEmpty()) {
            for (Map.Entry<String, Object> entry : customClaims.entrySet()) {
                claimsBuilder.claim(entry.getKey(), entry.getValue());
            }
        }

        // 创建JWS头
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS256);

        // 创建签名JWT
        SignedJWT signedJWT = new SignedJWT(header, claimsBuilder.build());
        signedJWT.sign(signer);

        return signedJWT.serialize();
    }

    /**
     * 验证JWT令牌有效性
     * @param token JWT令牌
     * @return 验证结果
     */
    public boolean validateToken(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            // 验证签名和过期时间
            boolean isValid = signedJWT.verify(verifier) && 
                   new Date().before(signedJWT.getJWTClaimsSet().getExpirationTime());
            if (!isValid) {
                log.warn("JWT token validation failed");
            }
            return isValid;
        } catch (Exception e) {
            log.error("Error occurred while validating JWT token: {}", e.getMessage(), e);
            return false;
        }
    }

    /**
     * 获取自定义声明值
     * @param token JWT令牌
     * @param claimName 声明名称
     * @return 声明值
     * @throws Exception 解析异常
     */
    public Object getClaim(String token, String claimName) throws Exception {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            if (!signedJWT.verify(verifier)) {
                log.warn("Invalid JWT signature for token: {}", token);
                throw new SecurityException("Invalid JWT signature");
            }
            return signedJWT.getJWTClaimsSet().getClaim(claimName);
        } catch (SecurityException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error occurred while getting claim '{}' from JWT token: {}", claimName, e.getMessage(), e);
            throw e;
        }
    }

    /**
     * 获取令牌过期时间
     * @param token JWT令牌
     * @return 过期时间
     * @throws Exception 解析异常
     */
    public Date getExpirationTime(String token) throws Exception {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            if (!signedJWT.verify(verifier)) {
                log.warn("Invalid JWT signature for token: {}", token);
                throw new SecurityException("Invalid JWT signature");
            }
            return signedJWT.getJWTClaimsSet().getExpirationTime();
        } catch (SecurityException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error occurred while getting expiration time from JWT token: {}", e.getMessage(), e);
            throw e;
        }
    }
}