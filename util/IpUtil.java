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
package com.alibaba.nacos.config.server.utils;

import javax.servlet.http.HttpServletRequest;

/**
 * Request util
 *
 * @author Nacos
 */
public class IpUtil {

    private static final String X_REAL_IP = "X-Real-IP";
    private static final String X_FORWARDED_FOR = "X-Forwarded-For";
    private static final String X_FORWARDED_FOR_SPLIT_SYMBOL = ",";


    /**
     * get real client ip
     * <p>
     * first use X-Forwarded-For header    https://zh.wikipedia.org/wiki/X-Forwarded-For
     * next nginx X-Real-IP
     * last {@link HttpServletRequest#getRemoteAddr()}
     *
     * @param request {@link HttpServletRequest}
     * @return
     */
    public static String getRemoteIp(HttpServletRequest request) {
        String xForwardedFor = request.getHeader(X_FORWARDED_FOR);
        if (!StringUtils.isBlank(xForwardedFor)) {
            return xForwardedFor.split(X_FORWARDED_FOR_SPLIT_SYMBOL)[0].trim();
        }
        String nginxHeader = request.getHeader(X_REAL_IP);
        return StringUtils.isBlank(nginxHeader) ? request.getRemoteAddr() : nginxHeader;
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

   

}
