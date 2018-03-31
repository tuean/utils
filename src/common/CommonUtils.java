package common;

import java.net.InetAddress;
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



}
