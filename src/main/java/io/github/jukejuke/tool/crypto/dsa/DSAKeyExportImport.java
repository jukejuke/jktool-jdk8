package io.github.jukejuke.tool.crypto.dsa;

import java.io.*;
import java.security.*;
import java.security.spec.*;
import java.util.Base64;

/**
 * DSA密钥对导出导入工具类
 */
public class DSAKeyExportImport {

    /**
     * 导出公钥为 Base64 字符串
     */
    public static String exportPublicKey(PublicKey publicKey) {
        byte[] keyBytes = publicKey.getEncoded();
        return Base64.getEncoder().encodeToString(keyBytes);
    }

    /**
     * 导出私钥为 Base64 字符串
     */
    public static String exportPrivateKey(PrivateKey privateKey) {
        byte[] keyBytes = privateKey.getEncoded();
        return Base64.getEncoder().encodeToString(keyBytes);
    }

    /**
     * 从 Base64 字符串导入公钥 (X.509 格式)
     */
    public static PublicKey importPublicKey(String base64Key) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(base64Key);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("DSA");
        return keyFactory.generatePublic(keySpec);
    }

    /**
     * 从 Base64 字符串导入私钥 (PKCS#8 格式)
     */
    public static PrivateKey importPrivateKey(String base64Key) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(base64Key);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("DSA");
        return keyFactory.generatePrivate(keySpec);
    }

    /**
     * 导出密钥对到文件
     */
    public static void exportKeyPairToFiles(KeyPair keyPair, String publicKeyFile,
                                            String privateKeyFile) throws Exception {

        // 导出公钥
        try (FileWriter writer = new FileWriter(publicKeyFile)) {
            writer.write("-----BEGIN PUBLIC KEY-----\n");
            writer.write(exportPublicKey(keyPair.getPublic()));
            writer.write("\n-----END PUBLIC KEY-----");
        }

        // 导出私钥
        try (FileWriter writer = new FileWriter(privateKeyFile)) {
            writer.write("-----BEGIN PRIVATE KEY-----\n");
            writer.write(exportPrivateKey(keyPair.getPrivate()));
            writer.write("\n-----END PRIVATE KEY-----");
        }

        System.out.println("密钥已导出到文件:");
        System.out.println("公钥: " + publicKeyFile);
        System.out.println("私钥: " + privateKeyFile);
    }

    /**
     * 从文件导入密钥对
     */
    public static KeyPair importKeyPairFromFiles(String publicKeyFile,
                                                 String privateKeyFile) throws Exception {

        // 读取公钥文件
        String publicKeyBase64 = readKeyFile(publicKeyFile, "PUBLIC KEY");

        // 读取私钥文件
        String privateKeyBase64 = readKeyFile(privateKeyFile, "PRIVATE KEY");

        // 导入密钥
        PublicKey publicKey = importPublicKey(publicKeyBase64);
        PrivateKey privateKey = importPrivateKey(privateKeyBase64);

        return new KeyPair(publicKey, privateKey);
    }

    private static String readKeyFile(String filename, String keyType) throws IOException {
        StringBuilder content = new StringBuilder();
        boolean inKeyBlock = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("BEGIN " + keyType)) {
                    inKeyBlock = true;
                    continue;
                }
                if (line.contains("END " + keyType)) {
                    break;
                }
                if (inKeyBlock) {
                    content.append(line.trim());
                }
            }
        }

        return content.toString();
    }
}
