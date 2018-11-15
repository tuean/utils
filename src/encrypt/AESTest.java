package encrypt;


/**
 * SymmetricEncoder 类使用示例
 *
 * Created by zhongxiaotian on 2018/4/3.
 */
public class AESTest {

    public static void main(String[] args) throws Exception {

        String data = "这是一行需要加密的数据";

        System.out.println("使用默认crypt_key 加密解密\r\n");
        String a = AES.encrypt(data);
        System.out.println("默认key加密后：");
        System.out.println(a);
        String a1 = AES.decrypt(a);
        System.out.println("默认key解密后：");
        System.out.println(a1);
        System.out.println(data.equals(a1) + "\r\n");

        System.out.println("使用自定义 crypt_key 加密解密\r\n");
        String key  = "defaultkey123456";
        String b = AES.encrypt(data, key);
        System.out.println("自定义key加密后：");
        System.out.println(b);
        String b1 = AES.decrypt(b, key);
        System.out.println("自定义key解密后：");
        System.out.println(b1);
        System.out.println(data.equals(b1));

    }
}
