package io.dev.jobprep.util;

import jakarta.servlet.http.HttpServletRequest;

public class IpAddressHelper {

    private static final String HEADER = "X-Forwarded-For";
    private static final String UNKNOWN = "unknown";
    private static final String LOCALHOST = "0:0:0:0:0:0:0:1";
    private static final String LOCAL = "127.0.0.1";

    private static final String[] IP_HEADER_CANDIDATES = {
        "Proxy-Client-IP",
        "WL-Proxy-Client-IP",
        "HTTP_X_FORWARDED_FOR",
        "HTTP_X_FORWARDED",
        "HTTP_X_CLUSTER_CLIENT_IP",
        "HTTP_CLIENT_IP",
        "HTTP_FORWARDED_FOR",
        "HTTP_FORWARDED",
        "HTTP_VIA",
        "REMOTE_ADDR"
    };

    public static String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader(HEADER);

        for (String header : IP_HEADER_CANDIDATES) {
            if (ip == null || ip.isEmpty() || UNKNOWN.equalsIgnoreCase(ip)) {
                ip = request.getHeader(header);
            }
        }

        if (ip == null || ip.isEmpty() || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        if (ip.equals(LOCALHOST)) {
            ip = LOCAL;
        }

        return ip;
    }

}
