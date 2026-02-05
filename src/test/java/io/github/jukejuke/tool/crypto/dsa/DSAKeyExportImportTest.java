/**
 * DSAKeyExportImport测试类，验证DSA密钥导出导入功能
 */
package io.github.jukejuke.tool.crypto.dsa;

import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;
import static org.junit.Assert.*;
import java.security.KeyPair;
import java.io.File;
import java.security.PrivateKey;
import java.security.PublicKey;

public class DSAKeyExportImportTest {
    
    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();
    
    /**
     * 测试公钥导出导入功能
     */
    @Test
    public void testPublicKeyExportImport() throws Exception {
        KeyPair keyPair = DSAUtil.generateKeyPair();
        String publicKeyStr = io.github.jukejuke.tool.crypto.dsa.DSAKeyExportImport.exportPublicKey(keyPair.getPublic());
        System.out.println("公钥: " + publicKeyStr);
        
        // 验证导出结果不为空
        assertNotNull("公钥导出结果不应为null", publicKeyStr);
        assertFalse("公钥导出结果不应为空字符串", publicKeyStr.isEmpty());
        
        // 导入公钥并验证一致性
        PublicKey importedPublicKey = io.github.jukejuke.tool.crypto.dsa.DSAKeyExportImport.importPublicKey(publicKeyStr);
        assertEquals("导入的公钥应与原始公钥一致", keyPair.getPublic(), importedPublicKey);
    }
    
    /**
     * 测试私钥导出导入功能
     */
    @Test
    public void testPrivateKeyExportImport() throws Exception {
        KeyPair keyPair = DSAUtil.generateKeyPair();
        String privateKeyStr = io.github.jukejuke.tool.crypto.dsa.DSAKeyExportImport.exportPrivateKey(keyPair.getPrivate());
        System.out.println("私钥: " + privateKeyStr);
        
        // 验证导出结果不为空
        assertNotNull("私钥导出结果不应为null", privateKeyStr);
        assertFalse("私钥导出结果不应为空字符串", privateKeyStr.isEmpty());
        
        // 导入私钥并验证一致性
        PrivateKey importedPrivateKey = io.github.jukejuke.tool.crypto.dsa.DSAKeyExportImport.importPrivateKey(privateKeyStr);
        assertEquals("导入的私钥应与原始私钥一致", keyPair.getPrivate(), importedPrivateKey);
    }
    
    /**
     * 测试密钥对文件导出导入功能
     */
    @Test
    public void testKeyPairFileExportImport() throws Exception {
        KeyPair originalKeyPair = DSAUtil.generateKeyPair();
        
        // 创建临时文件
        File publicKeyFile = temporaryFolder.newFile("dsa_public.pem");
        //File privateKeyFile = new File("dsa_private.pem");
        File privateKeyFile = temporaryFolder.newFile("dsa_private.pem");
        //File publicKeyFile = new File("dsa_public.pem");
        
        // 导出密钥对到文件
        io.github.jukejuke.tool.crypto.dsa.DSAKeyExportImport.exportKeyPairToFiles(originalKeyPair,
                publicKeyFile.getAbsolutePath(), privateKeyFile.getAbsolutePath());
        
        // 从文件导入密钥对
        KeyPair importedKeyPair = io.github.jukejuke.tool.crypto.dsa.DSAKeyExportImport.importKeyPairFromFiles(
                publicKeyFile.getAbsolutePath(), privateKeyFile.getAbsolutePath());
        
        // 验证导入的密钥对与原始密钥对一致
        assertEquals("导入的公钥应与原始公钥一致", 
                originalKeyPair.getPublic(), importedKeyPair.getPublic());
        assertEquals("导入的私钥应与原始私钥一致", 
                originalKeyPair.getPrivate(), importedKeyPair.getPrivate());
    }
    
    /**
     * 测试无效公钥导入
     */
    @Test(expected = Exception.class)
    public void testImportInvalidPublicKey() throws Exception {
        io.github.jukejuke.tool.crypto.dsa.DSAKeyExportImport.importPublicKey("invalid_base64_key");
    }
    
    /**
     * 测试无效私钥导入
     */
    @Test(expected = Exception.class)
    public void testImportInvalidPrivateKey() throws Exception {
        DSAKeyExportImport.importPrivateKey("invalid_base64_key");
    }
}