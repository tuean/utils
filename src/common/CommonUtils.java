package common;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.InetAddress;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zhongxiaotian on 2018/3/29.
 */
public class CommonUtils {


    /**
     * 字符在字符串中出现的次数
     *
     * @param string
     * @param a
     * @return
     */
    public static int occurTimes(String string, String a) {
        int pos = -2;
        int n = 0;

        while (pos != -1) {
            if (pos == -2) {
                pos = -1;
            }
            pos = string.indexOf(a, pos + 1);
            if (pos != -1) {
                n++;
            }
        }
        return n;
    }

    /**
     * 获取本机的IP
     * @return Ip地址
     */
    public static String getLocalHostIP() {
        String ip;
        try {
            /**返回本地主机。*/
            InetAddress addr = InetAddress.getLocalHost();
            /**返回 IP 地址字符串（以文本表现形式）*/
            ip = addr.getHostAddress();
        } catch(Exception ex) {
            ip = "";
        }

        return ip;
    }

    /**
     * 获取post请求中的数据
     *
     * @param is
     * @return
     */
    public static String getContent(InputStream is) {
        String pageString = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        StringBuffer sb = null;
        try {
            isr = new InputStreamReader(is, "utf-8");
            br = new BufferedReader(isr);
            sb = new StringBuffer();
            String line = null;
            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }
            pageString = sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (isr != null) {
                    isr.close();
                }
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            sb = null;
        }
        return pageString;
    }

    /**
     * 首字母大写
     *
     * @param name
     * @return
     */
    public static String captureName(String name) {
        char[] cs = name.toCharArray();
        cs[0] -= (cs[0] > 96 && cs[0] < 123) ? 32 : 0;
        return String.valueOf(cs);

    }

    /**
     * 不考虑编码的情况下截取字符串长度
     *
     * @param str
     * @param length
     * @return
     */
    public static String subStringByLength(String str, int length) {
        if (str != null && str.length() > length) {
            return StringUtils.substring(str, 0, length) + "...";
        } else {
            return str;
        }
    }

    /**
     * 字符串根据位数补0 不够位数的在前面补0，保留num长度位数字
     * @param code
     * @return
     */
    public static String autoGenericCode(String code, int num) {
        String result = "";
        // 保留num的位数
        // 0 代表前面补充0
        // d 代表参数为正数型
        result = String.format("%0" + num + "d", Integer.parseInt(code));
        return result;
    }

    /**
     * 全角转半角
     * @param input String.
     * @return 半角字符串
     */
    public static String ToDBC(String input) {
        if(StringUtils.isEmpty(input)){
            return null;
        }

        char c[] = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == '\u3000') {
                c[i] = ' ';
            } else if (c[i] > '\uFF00' && c[i] < '\uFF5F') {
                c[i] = (char) (c[i] - 65248);

            }
        }
        String returnString = new String(c);

        return returnString;
    }

    /**
     * 半角转全角
     * @param input String.
     * @return 全角字符串.
     */
    public static String ToSBC(String input) {
        if(StringUtils.isEmpty(input)){
            return null;
        }
        char c[] = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == ' ') {
                c[i] = '\u3000';
            } else if (c[i] < '\177') {
                c[i] = (char) (c[i] + 65248);

            }
        }
        return new String(c);
    }


    public static String randomString(int num){
        if(num <= 0){
            throw new IllegalArgumentException();
        }
        String source = "abcdefghijklmnopqrstuvwxyz0123456789";
        char[] cs = new char[num];
        for(int x = 0; x < num; x++){
            cs[x] = source.charAt(new Random().nextInt(source.length()));
        }
        return new String(cs);
    }

    public static void main(String[] args) {
        System.out.println(randomString(10));
    }


    public static String getCaller() {
        StackTraceElement stackTraceElement[] = Thread.currentThread().getStackTrace();
        String callName = stackTraceElement[2].getClassName();
        return callName;
    }

}
