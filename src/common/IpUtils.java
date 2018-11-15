package common;

import org.springframework.util.StopWatch;

import javax.servlet.http.HttpServletRequest;
import java.net.*;
import java.util.Enumeration;


public class IpUtils {

    public static String getIps(String split) {
        Enumeration en = null;
        String localIps="";
        try {
            en = NetworkInterface.getNetworkInterfaces();

            while(en.hasMoreElements()) {
                NetworkInterface ni = (NetworkInterface) en.nextElement();
                Enumeration ee = ni.getInetAddresses();
                while (ee.hasMoreElements()) {
                    InetAddress ia = (InetAddress) ee.nextElement();
                    // 只关心 IPv4 地址
                    if (ia instanceof Inet4Address) {
                        localIps = localIps + ia.getHostAddress() + split;
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }

        return localIps;

    }


    public static String getLocalIp(HttpServletRequest request) {
        String remoteAddr = request.getRemoteAddr();
        String forwarded = request.getHeader("X-Forwarded-For");
        String realIp = request.getHeader("X-Real-IP");

        String ip = null;
        if (realIp == null) {
            if (forwarded == null) {
                ip = remoteAddr;
            } else {
                ip = remoteAddr + "/" + forwarded.split(",")[0];
            }
        } else {
            if (realIp.equals(forwarded)) {
                ip = realIp;
            } else {
                if (forwarded != null) {
                    forwarded = forwarded.split(",")[0];
                }
                ip = realIp + "/" + forwarded;
            }
        }
        return ip;
    }


    public static String getLocalIp() throws UnknownHostException {
        // todo getLocalHost 在mac环境下会出现数秒的延迟
        try {
            StopWatch stopWatch = new StopWatch();
            stopWatch.start();
            String ip = InetAddress.getLocalHost().getHostAddress();
            stopWatch.stop();
            return ip;
        } catch (UnknownHostException e) {
            e.printStackTrace();
            throw e;
        }
    }

}
