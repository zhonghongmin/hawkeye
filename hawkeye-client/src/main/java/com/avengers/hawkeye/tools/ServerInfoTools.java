package com.avengers.hawkeye.tools;

import com.avengers.hawkeye.utils.StringUtils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * Created by 鸿敏 on 2017/2/22.
 *
 * 系统工具类，在启动的时候初始化好各信息
 */
public class ServerInfoTools {
    /**
     * 服务器原IP
     */
    public static String IP = "";

    /**
     * 格式化IP（12位）
     */
    public static String FORMAT_IP = "";

    /**
     * 服务器原端口，需要在WEB环境启动中赋值
     */
    public static String PORT = "";

    /**
     * 格式化端口（5位）
     */
    public static String FORMAT_PORT = "";

    /**
     * 服务器名称
     */
    public static String HOST_NAME = "";

    /**
     * 初始化本地IP地址
     *
     * @throws SocketException
     */
    public static void initLocalIp() {
        if (StringUtils.isEmpty(IP)) {
            synchronized (IP) {
                if (StringUtils.isEmpty(IP)) {
                    try {
                        if (isWindowsOS()) {
                            IP = InetAddress.getLocalHost().getHostAddress();
                        } else {
                            IP = getLinuxLocalIp();
                        }
                    } catch (Throwable e) {
                        throw new RuntimeException("Get IP fail.");
                    }
                }
            }
        }
    }

    /**
     * 初始化格式化的IP AAABBBCCCDDD
     *
     * @return
     */
    public static void initFormatIp() {
        if (StringUtils.isEmpty(FORMAT_IP)) {
            synchronized (FORMAT_IP) {
                if (StringUtils.isEmpty(FORMAT_IP)) {
                    String[] items = IP.split("\\.");
                    StringBuilder sb = new StringBuilder();
                    for (String s : items) {
                        s = StringUtils.fillStrLeft(s, 3, '0');
                        sb.append(s);
                    }
                    FORMAT_IP = sb.toString();
                }
            }
        }
    }

    /**
     * 初始化本机端口，在WEB环境启动时获取
     * @param serverPort
     */
    public static void initLocalPort(String serverPort) {
        PORT = serverPort;
    }

    /**
     * 初始化本机格式化端口 AAAAA
     *
     * @return
     */

    public static void initFormatPort() {
        if (StringUtils.isEmpty(FORMAT_PORT)) {
            synchronized (FORMAT_PORT) {
                if (StringUtils.isEmpty(FORMAT_PORT)) {
                    FORMAT_PORT = StringUtils.fillStrLeft(PORT, 5, '0');
                }
            }
        }
    }


    /**
     * 初始化本地Host名称
     */
    public static void initLocalHostName() {
        if (StringUtils.isEmpty(HOST_NAME)) {
            synchronized (HOST_NAME) {
                if (StringUtils.isEmpty(HOST_NAME)) {
                    try {
                        HOST_NAME = InetAddress.getLocalHost().getHostName();
                    } catch (Throwable e) {
                        throw new RuntimeException("Get host name fail.");
                    }
                }
            }
        }
    }

    /**
     * 判断操作系统是否是Windows
     *
     * @return
     */
    private static boolean isWindowsOS() {
        boolean isWindowsOS = false;
        String osName = System.getProperty("os.name");
        if (osName.toLowerCase().indexOf("windows") > -1) {
            isWindowsOS = true;
        }
        return isWindowsOS;
    }

    /**
     * 初始化Linux下的IP地址
     *
     * @return IP地址
     * @throws SocketException
     */
    private static String getLinuxLocalIp() throws SocketException {
        String ip = "";
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                String name = intf.getName();
                if (!name.contains("docker") && !name.contains("lo")) {
                    for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                        InetAddress inetAddress = enumIpAddr.nextElement();
                        if (!inetAddress.isLoopbackAddress()) {
                            String ipAddress = inetAddress.getHostAddress().toString();
                            if (!ipAddress.contains("::") && !ipAddress.contains("0:0:") && !ipAddress.contains("fe80")) {
                                ip = ipAddress;
                                break;
                            }
                        }
                    }
                }
            }
        } catch (SocketException ex) {
            ip = "127.0.0.1";
            ex.printStackTrace();
        }
        return ip;
    }
}
