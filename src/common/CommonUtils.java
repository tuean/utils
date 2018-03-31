package common;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.InetAddress;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zhongxiaotian on 2018/3/29.
 */
public class CommonUtils {


    /**
     * 计算 keyword 在 srcText 中出现的次数
     * @param srcText
     * @param keyword
     * @return
     */
    public static int countStr(String srcText, String keyword){

        String[] spcSysmbols = new String[]{"+", "*", "|", "/", "?"};

        int count = 0;
        for(int x = 0; spcSysmbols.length > x; x++){
            keyword = keyword.replace(spcSysmbols[x], "\\" + spcSysmbols[x]);
        }
        Pattern p = Pattern.compile(keyword);
        Matcher m = p.matcher(srcText);
        while (m.find()) {
            count++;
        }
        return count;
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



}
