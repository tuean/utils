package common;

import java.util.Formatter;
import java.util.Random;

/**
 * Created by zhongxiaotian on 2018/7/4.
 */
public class StringUtil {

    private final static char[] hexArray = "0123456789ABCDEF".toCharArray();

    /**
     * copy from
     *      https://stackoverflow.com/questions/9655181/how-to-convert-a-byte-array-to-a-hex-string-in-java
     *
     * 大写
     *
     * @param bytes
     * @return
     */
    public static String bytesToHexUpperCase(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    /**
     * from dingding example
     *
     * 小写
     *
     * @param hash
     * @return
     */
    public static String bytesToHexLowerCase(byte[] hash) {
        Formatter formatter = new Formatter();
        byte[] var2 = hash;
        int var3 = hash.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            byte b = var2[var4];
            formatter.format("%02x", new Object[]{Byte.valueOf(b)});
        }

        String result = formatter.toString();
        formatter.close();
        return result;
    }

    /**
     * 生成指定长度 包含 0-9 a-z 的随机字符串
     *
     * @param length
     * @return
     */
    public static String randomString(int length){
        if(length < 1){
            return "";
        }
        StringBuffer stringBuffer = new StringBuffer();
        Random random = new Random();
        double next;
        for(int x = 0; x < length; x++){
            next = random.nextDouble();
            if(next < 0.5){
                // numbers
                Double d = next * 2 * 10;
                stringBuffer.append(d.intValue());
            }else{
                // alphabet
                int alt = random.nextInt(26);
                stringBuffer.append((char) ('a' + alt));
            }
        }
        return stringBuffer.toString();
    }


    /**
     * 生成指定长度 包含 0-9 a-z A-Z 的随机字符串
     *
     * @param length
     * @return
     */
    public static String randomFullString(int length){
        if(length < 1){
            return "";
        }
        StringBuffer stringBuffer = new StringBuffer();
        Random random = new Random();
        double next;
        for(int x = 0; x < length; x++){
            next = random.nextDouble();
            if(next < (float) 1 / 3){
                // numbers
                Double d = next * 2 * 10;
                stringBuffer.append(d.intValue());
            }else if(next > (float) 2 / 3){
                // alphabet uppercase
                int alt = random.nextInt(26);
                stringBuffer.append((char) ('a' + alt));
            }else{
                // alphabet lowercase
                int alt = random.nextInt(26);
                stringBuffer.append((char) ('A' + alt));
            }
        }
        return stringBuffer.toString();
    }

    public static void main(String[] args) {
        System.out.println(randomFullString(50));
    }
}
