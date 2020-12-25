package zzz.ares.remoting.framework.helper;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.*;
import java.util.Enumeration;
import java.util.List;

/**
 * @Author: milkteazzz
 * @Data: 2020-12-11 9:39
 * @Version: 1.0
 */
public class IPHelper {

    private static final Logger logger = LoggerFactory.getLogger(IPHelper.class);

    private static String hostIp = StringUtils.EMPTY;

    public static String localIp() {
        return hostIp;
    }

    static {
        String ip = null;
        Enumeration allNetInterfaces;
        try {
            allNetInterfaces = NetworkInterface.getNetworkInterfaces();
            while (allNetInterfaces.hasMoreElements()) {
                NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();
                List<InterfaceAddress> InterfaceAddress = netInterface.getInterfaceAddresses();
                for (InterfaceAddress add : InterfaceAddress) {
                    InetAddress Ip = add.getAddress();
                    if (Ip != null && Ip instanceof Inet4Address) {
                        if (StringUtils.equals(Ip.getHostAddress(), "127.0.0.1")) {
                            continue;
                        }
                        ip = Ip.getHostAddress();
                        break;
                    }
                }
            }
        } catch (SocketException e) {
            logger.warn("获取本机Ip失败:异常信息:" + e.getMessage());
            throw new RuntimeException(e);
        }
        hostIp = ip;
    }
}
