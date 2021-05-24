/*
 * Copyright 1999-2018 Alibaba Group Holding Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.qin.enable.one.web;

import org.apache.catalina.valves.RemoteIpValve;
import org.apache.commons.lang3.StringUtils;
import sun.net.util.IPAddressUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 参考：RemoteIpValve
 *
 * @author qinjp
 */
public class IpUtil {

    private static final Pattern INTERNAL_PROXIES = Pattern.compile(
            "10\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}|" +
                    "192\\.168\\.\\d{1,3}\\.\\d{1,3}|" +
                    "169\\.254\\.\\d{1,3}\\.\\d{1,3}|" +
                    "127\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}|" +
                    "172\\.1[6-9]{1}\\.\\d{1,3}\\.\\d{1,3}|" +
                    "172\\.2[0-9]{1}\\.\\d{1,3}\\.\\d{1,3}|" +
                    "172\\.3[0-1]{1}\\.\\d{1,3}\\.\\d{1,3}|" +
                    "0:0:0:0:0:0:0:1|::1");

    private static final String X_REAL_IP = "X-Real-IP";
    private static final String X_FORWARDED_FOR = "X-Forwarded-For";
    private static final String X_FORWARDED_FOR_SPLIT_SYMBOL = ",";


    /**
     * 获取真实IP
     * 首先 判断 getRemoteAddr 是否内网
     * 接着 用 X-Forwarded-For header  倒序寻找第一个不是内网的  https://zh.wikipedia.org/wiki/X-Forwarded-For
     * 最后 都没有 就用 nginx X-Real-IP
     * 都没有就用getRemoteAddr()
     */
    public static String getRemoteIp(HttpServletRequest request) {
        // 最真实的Ip
        final String originalRemoteAddr = request.getRemoteAddr();

        // 是否内网--不是内网直接返回
        if (!INTERNAL_PROXIES.matcher(originalRemoteAddr).matches()) {
            return originalRemoteAddr;
        }

        // 内网 --则从 X-Forwarded-For 入手
        String xForwardedFor = request.getHeader(X_FORWARDED_FOR);
        if (!StringUtils.isBlank(xForwardedFor)) {
            String[] remoteIp = xForwardedFor.split(X_FORWARDED_FOR_SPLIT_SYMBOL);
            // 倒序寻找不是内网的IP
            for (int idx = remoteIp.length - 1; idx >= 0; idx--) {
                String currentRemoteIp = remoteIp[idx].trim();
                //  从右边开始第一个不是内网
                if (!INTERNAL_PROXIES.matcher(currentRemoteIp).matches()) {
                    return currentRemoteIp;
                }
            }
        }
        // X-Forwarded-For 为空就找 nginx X-Real-IP
        String nginxHeader = request.getHeader(X_REAL_IP);
        return StringUtils.isBlank(nginxHeader) ? originalRemoteAddr : nginxHeader;
    }


	public static boolean isIPV4(String addr) {
        if (null == addr) {
            return false;
        }
        String rexp = "^((25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){3}(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)$";

        Pattern pat = Pattern.compile(rexp);

        Matcher mat = pat.matcher(addr);

        boolean ipAddress = mat.find();
        return ipAddress;
    }

    public static boolean isIPV6(String addr) {
        if (null == addr) {
            return false;
        }
        String rexp = "^([\\da-fA-F]{1,4}:){7}[\\da-fA-F]{1,4}$";

        Pattern pat = Pattern.compile(rexp);

        Matcher mat = pat.matcher(addr);

        boolean ipAddress = mat.find();
        return ipAddress;
    }


    public static  boolean isIp(String ipStr){
        boolean iPv4LiteralAddress = IPAddressUtil.isIPv4LiteralAddress(ipStr);
        boolean iPv6LiteralAddress = IPAddressUtil.isIPv6LiteralAddress(ipStr);
        //ip有可能是v4,也有可能是v6,滿足任何一种都是合法的ip
        if (!(iPv4LiteralAddress||iPv6LiteralAddress)){
            return false;
        }
        return true;
    }

}
