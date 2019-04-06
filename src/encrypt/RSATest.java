//package encrypt;
//
//import java.util.Map;
//
///**
// * Created with IntelliJ IDEA.
// * Description:
// * User: hehaixia
// * Date: 2018-04-02 下午7:58
// */
//public class RSATest {
//    static String publicKey;
//    static String privateKey;
//
//    static {
//        try {
//            Map<String, Object> keyMap = RSA.genKeyPair();
//            publicKey = RSA.getPublicKey(keyMap);
//            privateKey = RSA.getPrivateKey(keyMap);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    static void test() throws Exception {
//        System.out.println("公钥加密——私钥解密");
//        String source = "这是一行没有任何意义的文字";
//        System.out.println("\r加密前文字：\r\n" + source);
//        byte[] data = source.getBytes();
//        byte[] encodedData = RSA.encryptByPublicKey(data, publicKey);
//        System.out.println("加密后文字：\r\n" + new String(encodedData));
//        byte[] decodedData = RSA.decryptByPrivateKey(encodedData, privateKey);
//        String target = new String(decodedData);
//        System.out.println("解密后文字: \r\n" + target);
//    }
//
//    static void testSign() throws Exception {
//        System.out.println("私钥加密——公钥解密");
//        String source = "这是一行测试RSA数字签名的无意义文字";
//        System.out.println("原文字：\r\n" + source);
//        byte[] data = source.getBytes();
//        byte[] encodedData = RSA.encryptByPrivateKey(data, privateKey);
//        System.out.println("加密后：\r\n" + new String(encodedData));
//        byte[] decodedData = RSA.decryptByPublicKey(encodedData, publicKey);
//        String target = new String(decodedData);
//        System.out.println("解密后: \r\n" + target);
//
//    }
//
//    static void verify() throws Exception {
//        String source = "这是一行测试RSA数字签名的无意义文字";
//        byte[] data = source.getBytes();
//        byte[] encodedData = RSA.encryptByPrivateKey(data, privateKey);
//        System.out.println("私钥签名——公钥验证签名");
//        String sign = RSA.sign(encodedData, privateKey);
//        System.out.println("签名:\r" + sign);
//        boolean status = RSA.verify(encodedData, publicKey, sign);
//        System.out.println("验证结果:\r" + status);
//    }
//
//    public static void main(String[] args) throws Exception {
//        System.out.println(publicKey);
//        System.out.println(privateKey);
//        test();
//        testSign();
//        verify();
//    }
//}
