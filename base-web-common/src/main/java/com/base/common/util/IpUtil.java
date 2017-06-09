package com.base.common.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class IpUtil {
	protected static Log log = LogFactory.getLog(IpUtil.class);


	public static String getRemoteIP(HttpServletRequest request) {
		String ip = request.getHeader("X-Forwarded-For");
		if (ip == null || ip.equals(""))
			return request.getRemoteAddr();
		if (ip.contains(",")) {
			return ip.substring(0, ip.indexOf(","));
		}
		return ip;
	}

//	public static String getRemoteIP() {
//		HttpServletRequest request = ServletActionContext.getRequest();
//		String ip = request.getHeader("X-Forwarded-For");
//		if (ip == null || ip.equals(""))
//			return request.getRemoteAddr();
//		if (ip.contains(",")) {
//			return ip.substring(0, ip.indexOf(","));
//		}
//		return ip;
//	}


	public static String getLocalIP() {
		StringBuilder sb = new StringBuilder();
		try {
			Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
			while (en.hasMoreElements()) {
				NetworkInterface intf = en.nextElement();
				Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses();
				while (enumIpAddr.hasMoreElements()) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress() && !inetAddress.isLinkLocalAddress()
							&& inetAddress.isSiteLocalAddress()) {
						if (sb.length() == 0) {
							sb.append(inetAddress.getHostAddress().toString());
						} else {
							sb.append("," + inetAddress.getHostAddress().toString());
						}
					}
				}
			}
		} catch (SocketException e) {
		}
		return sb.toString();
	}










}
