/**
 * DSAUtil测试类，验证DSA密钥生成、签名和验证功能
 */
package io.github.jukejuke.tool.crypto.dsa;

import org.junit.Test;
import static org.junit.Assert.*;
import java.security.KeyPair;

public class DSAUtilTest {
    
    /**
     * 测试DSA密钥对生成功能
     */
    @Test
    public void testGenerateKeyPair() throws Exception {
        KeyPair keyPair = DSAUtil.generateKeyPair();
        assertNotNull("密钥对不应为null", keyPair);
        assertNotNull("公钥不应为null", keyPair.getPublic());
        assertNotNull("私钥不应为null", keyPair.getPrivate());
    }
    
    /**
     * 测试DSA签名和验证功能（正常情况）
     */
    @Test
    public void testSignAndVerify() throws Exception {
        KeyPair keyPair = DSAUtil.generateKeyPair(2048);
        String data = "test dsa signature";
        //MD4CHQC5uA45Jertw4PG45veZpGKOJtWo6piaWnTy9CfAh0AqKM5ZODAnxI9/3KbVECZkjNC0BWm3y8Ycme14Q==
        //MD0CHQCXfGbJjlfD62iG0kLLew/bAyQ4EYlW/d9tGAcGAhxGpQ3aZNq4uCNR6e3XMNn2VS79PTQecTq+vTpJ
        //MD0CHG77FbYxIvB4ppNjc6CTkQJ0GVi/OZnyVKIOl/sCHQCH+5+jtAy63LCe5JCxhCNKy1h7+wnvWbGLPbnS
        //MEUCIAhxiLl7/sSsX7QgfKkBRJKQ+8uTgOZfw71SLrPzo4v5AiEArnhJMtK2vKnsYBWku8wSWXKERV9INPiJRXomVbONuRk=
        //MEUCIQCx9vvuj0Xxnee1AbElOILiG3qyXacaTWzXSsR8XMBCdgIgB0Dl5BDyfSP/pYUtlHbABJCVfu25gKvWYzXJfdwJXh0=
        //MEYCIQCA0vsiMKG5VDWtlCDiZZYesoGX5vuJDcuafil+BfJhXgIhAJa2HKqM7a9BKU2eI619RIVt0VQbbrWjIJOM4aFCgvgq
        
        // 生成签名
        String signature = DSAUtil.sign(data, keyPair.getPrivate());
        System.out.println("签名结果: " + signature);
        assertNotNull("签名结果不应为null", signature);
        assertFalse("签名结果不应为空字符串", signature.isEmpty());
        
        // 验证签名
        boolean verifyResult = DSAUtil.verify(data, keyPair.getPublic(), signature);
        assertTrue("签名验证应通过", verifyResult);
    }
    
    /**
     * 测试DSA签名验证功能（数据篡改情况）
     */
    @Test
    public void testVerifyTamperedData() throws Exception {
        KeyPair keyPair = DSAUtil.generateKeyPair();
        String originalData = "test dsa signature";
        String tamperedData = "test dsa signature tampered";
        
        // 生成原始数据签名
        String signature = DSAUtil.sign(originalData, keyPair.getPrivate());
        
        // 验证篡改后的数据
        boolean verifyResult = DSAUtil.verify(tamperedData, keyPair.getPublic(), signature);
        assertFalse("篡改数据的签名验证应失败", verifyResult);
    }
    
    /**
     * 测试DSA签名验证功能（错误公钥情况）
     */
    @Test
    public void testVerifyWithWrongPublicKey() throws Exception {
        KeyPair keyPair1 = DSAUtil.generateKeyPair();
        KeyPair keyPair2 = DSAUtil.generateKeyPair(); // 不同的密钥对
        String data = "test dsa signature";
        
        // 使用keyPair1的私钥签名
        String signature = DSAUtil.sign(data, keyPair1.getPrivate());
        
        // 使用keyPair2的公钥验证
        boolean verifyResult = DSAUtil.verify(data, keyPair2.getPublic(), signature);
        assertFalse("错误公钥的签名验证应失败", verifyResult);
    }
    
    /**
     * 测试指定密钥长度的密钥对生成
     */
    @Test
    public void testGenerateKeyPairWithSize() throws Exception {
        KeyPair keyPair = DSAUtil.generateKeyPair(3072);
        assertNotNull("3072位密钥对不应为null", keyPair);
        assertEquals("密钥算法应为DSA", "DSA", keyPair.getPublic().getAlgorithm());
    }
}