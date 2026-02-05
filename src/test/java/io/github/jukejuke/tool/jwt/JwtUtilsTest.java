package io.github.jukejuke.tool.jwt;

import com.nimbusds.jose.JOSEException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

public class JwtUtilsTest {
    private static final String TEST_SECRET = "a1b2c3d4e5f6g7h8i9j0k1l2m3n4o5p6";
    private static final String TEST_USER_ID = "user123";
    private JwtUtils jwtUtils;

    @BeforeEach
    void setUp() throws JOSEException {
        jwtUtils = new JwtUtils(TEST_SECRET);
    }

    @Test
    void createToken_ShouldGenerateValidToken() throws JOSEException {
        // 准备测试数据
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", "admin");
        claims.put("permission", "read");
        Date expiration = new Date(System.currentTimeMillis() + 3600000);

        // 执行测试
        String token = jwtUtils.createToken(TEST_USER_ID, claims, expiration);

        // 验证结果
        assertNotNull(token);
        assertTrue(token.length() > 0);
    }

    @Test
    void validateToken_WithValidToken_ReturnsTrue() throws JOSEException {
        String token = jwtUtils.createToken(TEST_USER_ID, null, new Date(System.currentTimeMillis() + 3600000));
        assertTrue(jwtUtils.validateToken(token));
    }

    @Test
    void validateToken_WithExpiredToken_ReturnsFalse() throws JOSEException {
        Date pastExpiration = new Date(System.currentTimeMillis() - 3600000);
        String token = jwtUtils.createToken(TEST_USER_ID, null, pastExpiration);
        assertFalse(jwtUtils.validateToken(token));
    }

    @Test
    void validateToken_WithTamperedToken_ReturnsFalse() throws JOSEException {
        String validToken = jwtUtils.createToken(TEST_USER_ID, null, new Date(System.currentTimeMillis() + 3600000));
        String tamperedToken = validToken.substring(0, validToken.length() - 1) + 'x';
        assertFalse(jwtUtils.validateToken(tamperedToken));
    }

    @Test
    void getClaim_ShouldReturnCorrectValue() throws Exception {
        Map<String, Object> claims = new HashMap<>();
        String testClaimKey = "role";
        String testClaimValue = "admin";
        claims.put(testClaimKey, testClaimValue);

        String token = jwtUtils.createToken(TEST_USER_ID, claims, new Date(System.currentTimeMillis() + 3600000));
        Object result = jwtUtils.getClaim(token, testClaimKey);

        assertEquals(testClaimValue, result);
    }

    @Test
    void getExpirationTime_ShouldReturnSetTime() throws Exception {
        Date expectedExpiration = new Date(System.currentTimeMillis() + 3600000);
        String token = jwtUtils.createToken(TEST_USER_ID, null, expectedExpiration);

        Date actualExpiration = jwtUtils.getExpirationTime(token);

        assertEquals(expectedExpiration.getTime(), actualExpiration.getTime());
    }

    @Test
    void getClaim_WithInvalidSignature_ThrowsException() throws JOSEException {
        String validToken = jwtUtils.createToken(TEST_USER_ID, null, new Date(System.currentTimeMillis() + 3600000));
        String tamperedToken = validToken.substring(0, validToken.length() - 1) + 'x';

        assertThrows(SecurityException.class, () -> jwtUtils.getClaim(tamperedToken, "anyKey"));
    }
}