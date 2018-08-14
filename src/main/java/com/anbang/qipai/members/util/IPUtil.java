package com.anbang.qipai.members.util;

import javax.servlet.http.HttpServletRequest;

public class IPUtil {

	public static String getRealIp(HttpServletRequest request) {
		String ip;
		ip = request.getHeader("X-Real-IP");
		if (ip == null) {
			String[] ips = request.getHeader("x-forwarded-for").split(",");
			if (ips.length > 0) {
				ip = ips[0];
			} else {
				ip = request.getRemoteAddr();
			}
		}
		return ip;
	}
}
