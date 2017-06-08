package com.soouya.springmvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.soouya.auth.SessionUtil;
import com.soouya.auth.SoouyaUser;
import javacommon.base.exception.BusinessException;
import com.soouya.common.util.IpUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.MDC;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 拦截器,用于存放渲染视图时需要的的共享变量
 * 
 * @author leo
 * 
 */
public class LogInterceptor extends HandlerInterceptorAdapter {
	static Log log = LogFactory.getLog(LogInterceptor.class);

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		String param = getParameter(request);
		request.setAttribute("startTime", System.currentTimeMillis());
		request.setAttribute("loginUser", SessionUtil.getSoouyaUser(request));
		request.setAttribute("jsessionid", SessionUtil.getSessionId(request));
		request.setAttribute("param", param);
		return super.preHandle(request, response, handler);
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		Long startTime = (Long) request.getAttribute("startTime");
		String param = (String) request.getAttribute("param");
		long responseTime = System.currentTimeMillis() - startTime;
		SoouyaUser loginUser = (SoouyaUser) request.getAttribute("loginUser");
		log(request, param, response.getStatus(), response.toString(), responseTime, loginUser, (String) request.getAttribute("jsessionid"));
	}

	private String getParameter(HttpServletRequest request) {
		String parameter = "";
		Map map = request.getParameterMap();
		Set keSet = map.entrySet();
		for (Iterator itr = keSet.iterator(); itr.hasNext();) {
			Map.Entry me = (Map.Entry) itr.next();
			Object key = me.getKey();
			Object ov = me.getValue();
			String[] value = new String[1];
			if (ov instanceof String[]) {
				value = (String[]) ov;
			} else {
				value[0] = ov.toString();
			}
			for (int k = 0; k < value.length; k++) {
				if (!parameter.equals(""))
					parameter += "&";
				parameter += ("[" + key + "=" + value[k] + "]");
			}
		}
		if (request instanceof DefaultMultipartHttpServletRequest) {
			DefaultMultipartHttpServletRequest defaultMultipartHttpServletRequest = (DefaultMultipartHttpServletRequest) request;
			Iterator<String> it = defaultMultipartHttpServletRequest.getFileNames();
			while (it.hasNext()) {
				it.next();
				if (!parameter.equals(""))
					parameter += "&";
				parameter += ("[file=tmp.f]");
			}
		}
		return parameter;
	}

	private void log(HttpServletRequest request, String param, int status, String result, long responseTime, SoouyaUser loginUser, String jsessionid) {
		try {
			if (loginUser == null) {
				MDC.put("userId", "-");
			} else {
				MDC.put("userId", loginUser.getId());
			}
			if (jsessionid == null)
				MDC.put("jsessionid", "-");
			else
				MDC.put("jsessionid", jsessionid);

			String userAgent = StringUtils.defaultString(request.getHeader("User-Agent"), "-");
			userAgent = userAgent.replace(" ", "%20");
			if (userAgent.length() > 2000) {
				userAgent = userAgent.substring(0, 2000) + "...";
			}
			MDC.put("userAgent", userAgent);
			MDC.put("method", StringUtils.defaultString(request.getMethod(), "-"));
			param = param.replace(" ", "%20");
			if(StringUtils.isBlank(param)){
				StringBuilder sb = new StringBuilder();
				sb.append("[");
				String paramMdc = (String) MDC.get("param");
				if(StringUtils.isNotBlank(paramMdc)){
					sb.append(paramMdc);
				}
				sb.append("]");
				param = sb.toString();
			}
			String uri = request.getRequestURI() + (param == null || param.equals("") ? "" : "?" + param);
			if (uri.length() > 10000) {
				uri = uri.substring(0, 10000) + "...";
			}
			if (uri.endsWith(".jsp") || uri.endsWith(".css") || uri.endsWith(".html") || uri.endsWith(".js")) {
				return;
			}
			MDC.put("uri", uri);
			MDC.put("remoteIP", StringUtils.defaultString(IpUtil.getRemoteIP(request), "-"));
			MDC.put("localIP", StringUtils.defaultString(IpUtil.getLocalIP(), "-"));
			MDC.put("protocol", StringUtils.defaultString(request.getProtocol(), "-"));
			MDC.put("id", StringUtils.remove(UUID.randomUUID().toString(), "-"));
			ObjectMapper objectMapper = new ObjectMapper();
			String s=(String)request.getAttribute("success");
			String success = "10001";
			if (!result.equals("")) {
				HashMap map = objectMapper.readValue(result, HashMap.class);
				success = (String) map.get("success");
			}
			if(s!=null)
				success=s;
			if (success == null || uri.contains("oper=excel")) {
				success = "1";
			}
			MDC.put("success", success);
			MDC.put("length", result.length());
			MDC.put("takenTime", responseTime);
			MDC.put("status", status);
			log.debug("");
		} catch (Exception e) {
//			ErrorUtil.errorHandle(e);
		} finally {
			Map m = MDC.getContext();
			if (m != null) {
				m.clear();
			}
		}
	}

	private void checkUtf8(String param) {
		Pattern pattern = Pattern.compile("[^\\u4E00-\\u9FA5|\\u3000｜\\uFF00-\\uFFEF|\\u0007-\\u000d|\\u0020-\\u00ff|\\u3000-\\u303f|\\u2000-\\u206f]");
		/*if (param != null && param.contains("[wechat=")) {// 忽略微信名
			int begin = param.indexOf("[wechat=");
			String t = param.substring(begin + 8);
			param = param.replace(param.substring(begin, begin + 8 + t.indexOf("]")), "[wechat=tmp");
		}*/
		Matcher matcher = pattern.matcher(param);
		if (matcher.find()) {
			int start = matcher.start();
			int end = matcher.end();
			log.info("含有特殊字符:" + param.substring(start, end));
			throw new BusinessException("10005", "含有特殊字符：" + param.substring(start, end));
		}
	}
}
