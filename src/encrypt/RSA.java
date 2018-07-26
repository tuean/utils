package encrypt;

import sun.misc.BASE64Decoder;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 * @author
 * @create 2017-08-08 10:04
 **/
public class RSA {

    /**
     * 加密算法RSA
     */
    public static final String KEY_ALGORITHM = "RSA";

    /**
     * 签名算法
     */
    public static final String SIGNATURE_ALGORITHM = "MD5withRSA";

    /**
     * 获取公钥的key
     */
    private static final String PUBLIC_KEY = "RSAPublicKey";

    /**
     * 获取私钥的key
     */
    private static final String PRIVATE_KEY = "RSAPrivateKey";

    /**
     * RSA最大加密明文大小
     */
    private static final int MAX_ENCRYPT_BLOCK = 117;

    /**
     * RSA最大解密密文大小
     */
    private static final int MAX_DECRYPT_BLOCK = 128;

    /**
     * 生成公钥&私钥
     *
     * @return keyMap
     * @throws Exception
     */
    public static Map<String, Object> genKeyPair() throws Exception {
        try {
            KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
            keyPairGen.initialize(1024);
            KeyPair keyPair = keyPairGen.generateKeyPair();
            RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
            RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
            Map<String, Object> keyMap = new HashMap<String, Object>(2);
            keyMap.put(PUBLIC_KEY, publicKey);
            keyMap.put(PRIVATE_KEY, privateKey);
            return keyMap;
        } catch (NoSuchAlgorithmException var) {
            var.printStackTrace();
            throw new Exception("没有对应的算法");
        }
    }

    /**
     * 用私钥对信息生成数字签名
     *
     * @param data       已加密数据
     * @param privateKey 私钥(BASE64编码)
     * @return
     * @throws Exception
     */
    public static String sign(byte[] data, String privateKey) throws Exception {
        try {
            PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(getBASE642Byte(privateKey));
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            PrivateKey privateK = keyFactory.generatePrivate(pkcs8KeySpec);
            Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
            signature.initSign(privateK);
            signature.update(data);
            return RSA.getBASE64(signature.sign());
        } catch (IOException var1) {
            var1.printStackTrace();
            throw new Exception("解密过程中io异常");
        } catch (NoSuchAlgorithmException var2) {
            var2.printStackTrace();
            throw new Exception("解密算法异常");
        } catch (InvalidKeySpecException | InvalidKeyException var3) {
            var3.printStackTrace();
            throw new Exception("解密key设置异常");
        } catch (SignatureException var4) {
            var4.printStackTrace();
            throw new Exception("签名异常");
        }
    }

    /**
     * 校验数字签名
     *
     * @param data      已加密数据
     * @param publicKey 公钥(BASE64编码)
     * @param sign      数字签名
     * @return
     * @throws Exception
     */
    public static boolean verify(byte[] data, String publicKey, String sign) throws Exception {
        try {
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(getBASE642Byte(publicKey));
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            PublicKey publicK = keyFactory.generatePublic(keySpec);
            Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
            signature.initVerify(publicK);
            signature.update(data);
            return signature.verify(getBASE642Byte(sign));
        } catch (IOException var1) {
            var1.printStackTrace();
            throw new Exception("解密过程中io异常");
        } catch (NoSuchAlgorithmException var2) {
            var2.printStackTrace();
            throw new Exception("解密算法异常");
        } catch (InvalidKeySpecException | InvalidKeyException var3) {
            var3.printStackTrace();
            throw new Exception("解密key设置异常");
        } catch (SignatureException var4) {
            var4.printStackTrace();
            throw new Exception("签名异常");
        }
    }

    /**
     * 私钥解密
     *
     * @param encryptedData 已加密数据
     * @param privateKey    私钥(BASE64编码)
     * @return
     * @throws Exception
     */
    public static byte[] decryptByPrivateKey(byte[] encryptedData, String privateKey) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(getBASE642Byte(privateKey));
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            PrivateKey privateK = keyFactory.generatePrivate(pkcs8KeySpec);
            Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
            cipher.init(Cipher.DECRYPT_MODE, privateK);
            int inputLen = encryptedData.length;
            int offSet = 0;
            byte[] cache;
            int i = 0;
            // 对数据分段解密
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                    cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
                } else {
                    cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * MAX_DECRYPT_BLOCK;
            }
            byte[] decryptedData = out.toByteArray();
            out.close();
            return decryptedData;
        } catch (IOException var1){
            var1.printStackTrace();
            throw new Exception("解密过程中io异常");
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException var2){
            var2.printStackTrace();
            throw new Exception("解密算法异常");
        } catch (InvalidKeySpecException | InvalidKeyException var3){
            var3.printStackTrace();
            throw new Exception("解密key设置异常");
        } finally {
            if (out != null){
                try{
                    out.close();
                } catch (Exception var){

                }
            }
        }
    }

    /**
     * 公钥解密
     *
     * @param encryptedData 已加密数据
     * @param publicKey     公钥(BASE64编码)
     * @return
     * @throws Exception
     */
    public static byte[] decryptByPublicKey(byte[] encryptedData, String publicKey) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(getBASE642Byte(publicKey));
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            PublicKey publicK = keyFactory.generatePublic(x509KeySpec);
            Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
            cipher.init(Cipher.DECRYPT_MODE, publicK);
            int inputLen = encryptedData.length;
            int offSet = 0;
            byte[] cache;
            int i = 0;
            // 对数据分段解密
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                    cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
                } else {
                    cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * MAX_DECRYPT_BLOCK;
            }
            byte[] decryptedData = out.toByteArray();
            out.close();
            return decryptedData;
        } catch (IOException var1){
            var1.printStackTrace();
            throw new Exception("解密过程中io异常");
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException var2){
            var2.printStackTrace();
            throw new Exception("解密算法异常");
        } catch (InvalidKeySpecException | InvalidKeyException var3){
            var3.printStackTrace();
            throw new Exception("解密key设置异常");
        } finally {
            if (out != null){
                try{
                    out.close();
                } catch (Exception var){

                }
            }
        }
    }


    /**
     * 公钥加密
     *
     * @param data      源数据
     * @param publicKey 公钥(BASE64编码)
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPublicKey(byte[] data, String publicKey) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(getBASE642Byte(publicKey));
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            PublicKey publicK = keyFactory.generatePublic(x509KeySpec);
            // 对数据加密
            Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
            cipher.init(Cipher.ENCRYPT_MODE, publicK);
            int inputLen = data.length;

            int offSet = 0;
            byte[] cache;
            int i = 0;
            // 对数据分段加密
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                    cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
                } else {
                    cache = cipher.doFinal(data, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * MAX_ENCRYPT_BLOCK;
            }
            byte[] encryptedData = out.toByteArray();
            out.close();
            return encryptedData;
        } catch (IOException var1){
            var1.printStackTrace();
            throw new Exception("加密过程中io异常");
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException var2){
            var2.printStackTrace();
            throw new Exception("加密算法异常");
        } catch (InvalidKeySpecException | InvalidKeyException var3){
            var3.printStackTrace();
            throw new Exception("加密key设置异常");
        } finally {
            if (out != null){
                try{
                    out.close();
                } catch (Exception var){

                }
            }
        }
    }

    /**
     * 私钥加密
     *
     * @param data       源数据
     * @param privateKey 私钥(BASE64编码)
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPrivateKey(byte[] data, String privateKey) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(getBASE642Byte(privateKey));
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            PrivateKey privateK = keyFactory.generatePrivate(pkcs8KeySpec);
            Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
            cipher.init(Cipher.ENCRYPT_MODE, privateK);
            int inputLen = data.length;
            int offSet = 0;
            byte[] cache;
            int i = 0;
            // 对数据分段加密
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                    cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
                } else {
                    cache = cipher.doFinal(data, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * MAX_ENCRYPT_BLOCK;
            }
            byte[] encryptedData = out.toByteArray();
            out.close();
            return encryptedData;
        }  catch (IOException var1){
            var1.printStackTrace();
            throw new Exception("加密过程中io异常");
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException var2){
            var2.printStackTrace();
            throw new Exception("加密算法异常");
        } catch (InvalidKeySpecException | InvalidKeyException var3){
            var3.printStackTrace();
            throw new Exception("加密key设置异常");
        } finally {
            if (out != null){
                try{
                    out.close();
                } catch (Exception var){

                }
            }
        }
    }

    /**
     * 获取私钥
     *
     * @param keyMap 密钥对
     * @return
     * @throws Exception
     */
    public static String getPrivateKey(Map<String, Object> keyMap) throws Exception {
        Key key = (Key) keyMap.get(PRIVATE_KEY);
        return RSA.getBASE64(key.getEncoded());
    }

    /**
     * 获取公钥
     *
     * @param keyMap 密钥对
     * @return
     * @throws Exception
     */
    public static String getPublicKey(Map<String, Object> keyMap)
            throws Exception {
        Key key = (Key) keyMap.get(PUBLIC_KEY);
        return RSA.getBASE64(key.getEncoded());
    }


    public static String getBASE64(String str) {
        byte[] b = str.getBytes();
        String s = null;
        if (b != null) {
            s = new sun.misc.BASE64Encoder().encode(b);
        }
        return s;
    }

    public static String getBASE64(byte[] str) {
        String s = null;
        if (str != null) {
            s = new sun.misc.BASE64Encoder().encode(str);
        }
        return s;
    }
    public static byte[] getBASE642Byte(String str) throws IOException {
        byte[] s = null;
        if (str != null && !"".equals(str)) {
            s = new BASE64Decoder().decodeBuffer(str);
        }
        return s;
    }

}
