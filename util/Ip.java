package com.qin.strem;

import java.util.Enumeration;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

/**
 * 参考：RemoteIpValve
 */
public class Ip {

    private static final String X_FORWARDED_FOR = "X-Forwarded-For";
    private static final String X_FORWARDED_FOR_SPLIT_SYMBOL = ",";

    /**
     * @see #setInternalProxies(String)
     */
    private static final Pattern internalProxies = Pattern.compile(
            "10\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}|" +
                    "192\\.168\\.\\d{1,3}\\.\\d{1,3}|" +
                    "169\\.254\\.\\d{1,3}\\.\\d{1,3}|" +
                    "127\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}|" +
                    "172\\.1[6-9]{1}\\.\\d{1,3}\\.\\d{1,3}|" +
                    "172\\.2[0-9]{1}\\.\\d{1,3}\\.\\d{1,3}|" +
                    "172\\.3[0-1]{1}\\.\\d{1,3}\\.\\d{1,3}|" +
                    "0:0:0:0:0:0:0:1|::1");

    /**
     * {@link Pattern} for a comma delimited string that support whitespace characters
     */
    private static final Pattern commaSeparatedValuesPattern = Pattern.compile("\\s*,\\s*");

    /**
     * 内网
     */
    public static boolean isInternalIp(String ip) {
        return internalProxies.matcher(ip).matches();
    }


    public static String remoteIp(HttpServletRequest request) {

        String remoteIp ;


        final String originalRemoteAddr = request.getRemoteAddr();
        remoteIp = originalRemoteAddr;
        // 是否内网
        if (internalProxies.matcher(originalRemoteAddr).matches()) {

            StringBuilder concatRemoteIpHeaderValue = new StringBuilder();

            for (Enumeration<String> e = request.getHeaders(X_FORWARDED_FOR); e.hasMoreElements();) {
                if (concatRemoteIpHeaderValue.length() > 0) {
                    concatRemoteIpHeaderValue.append(", ");
                }
                concatRemoteIpHeaderValue.append(e.nextElement());
            }

            String[] remoteIpHeaderValue = commaDelimitedListToStringArray(concatRemoteIpHeaderValue.toString());

            int idx;
            // loop on remoteIpHeaderValue to find the first trusted remote ip and to build the proxies chain
            for (idx = remoteIpHeaderValue.length - 1; idx >= 0; idx--) {
                String currentRemoteIp = remoteIpHeaderValue[idx];
                remoteIp = currentRemoteIp;
                if (internalProxies.matcher(currentRemoteIp).matches()) {
                    // do nothing, internalProxies IPs are not appended to the
                    // 内网
                } else {
                    idx--; // decrement idx because break statement doesn't do it
                    break;
                }
            }
        }

        return remoteIp;
    }

    /**
     * Convert a given comma delimited String into an array of String
     * @param commaDelimitedStrings The string to convert
     * @return array of String (non <code>null</code>)
     */
    protected static String[] commaDelimitedListToStringArray(String commaDelimitedStrings) {
        return (commaDelimitedStrings == null || commaDelimitedStrings.length() == 0) ? new String[0] : commaSeparatedValuesPattern
                .split(commaDelimitedStrings);
    }

    public static void main(String[] args) {
        System.out.println(isInternalIp("192.168.9.25"));
    }

}
