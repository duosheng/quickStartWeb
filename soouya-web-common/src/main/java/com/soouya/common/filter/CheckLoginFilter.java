package com.soouya.common.filter;//package com.soouya.common.filter;
//
//import com.soouya.cm.type.RoleCodeEnum;
//import com.soouya.common.exception.BusinessException;
//import com.soouya.common.util.Code;
//import com.soouya.common.util.ErrorUtil;
//import com.soouya.common.util.ResponseWrapper;
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
//
//import javax.servlet.*;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.util.HashMap;
//import java.util.HashSet;
//import java.util.Map;
//import java.util.Set;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
//public class CheckLoginFilter implements Filter {
//
//	private final static Log log = LogFactory.getLog(CheckLoginFilter.class);
//
//	// 无需登陆的链接
//	public static Set<String> NO_NEED_AUTH_SET = new HashSet<String>();
//
//	// 本地接口
//	public static Map<String, RoleCodeEnum[]> authMap = new HashMap<String, RoleCodeEnum[]>();
//
//	// 大清接口
//	public static Map<String, RoleCodeEnum[]> soouyaAuthMap = new HashMap<String, RoleCodeEnum[]>();
//
//	@Override
//	public void destroy() {
//
//	}
//
//	static {
//		NO_NEED_AUTH_SET.add("/cm/Seed/login.do");
//	}
//	static {
//		// 用户管理
//		authMap.put("/cm/Seed/modifyPwd.do", new RoleCodeEnum[] { RoleCodeEnum.ALL });// 修改密码
//	}
//	static {
//		// 运营位
//		soouyaAuthMap.put("^/soouya/pages/Operate/(.*).do$",
//				new RoleCodeEnum[] { RoleCodeEnum.OPERATOR, RoleCodeEnum.OPERATOR_LEADER });
//	}
//
//	@Override
//	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) {
//		long responseTime = System.currentTimeMillis();
//		HttpServletRequest request = (HttpServletRequest) servletRequest;
//		ResponseWrapper response = new ResponseWrapper((HttpServletResponse) servletResponse);
//
//		// api权限
//		if (checkUri(request, response)) {
//			try {
//				filterChain.doFilter(servletRequest, response);
//			} catch (IOException e) {
//				log.error(e.getMessage());
//				ErrorUtil.errorHandle(response, new BusinessException(Code.FATAL, "后台请求过滤发生异常"));
//			} catch (ServletException e1) {
//				log.error(e1.getMessage());
//				ErrorUtil.errorHandle(response, new BusinessException(Code.FATAL, "后台请求发生异常"));
//			}
//		}
//		try {
//			servletResponse.getWriter().write(response.toString());
//		} catch (IOException e) {
//			// ErrorUtil.errorHandle(e);
//			log.error(e.getMessage());
//		}
//	}
//
//
//	/**
//	 *
//	 * api权限校验
//	 *
//	 * @param request
//	 * @param response
//	 * @return
//	 */
//	private boolean checkUri(HttpServletRequest request, ResponseWrapper response) {
//
//		return true;
//	}
//
//	/**
//	 * 获取匹配的权限
//	 *
//	 * @param uri
//	 * @return
//	 */
//	private RoleCodeEnum[] getRoleCodeEnmu(String uri) {
//		String key = null;
//		if ((key = matchUrl(soouyaAuthMap, uri)) != null) {
//			return soouyaAuthMap.get(key);
//		}
//		key = null;
//		if ((key = matchUrl(authMap, uri)) != null) {
//			return authMap.get(key);
//		}
//		return null;
//	}
//
//	/**
//	 * 判断是否大清的链接
//	 *
//	 * @param urlMap
//	 * @param request
//	 * @return
//	 */
//	private String matchUrl(Map<String, RoleCodeEnum[]> urlMap, HttpServletRequest request) {
//		String uri = getUri(request);
//		return matchUrl(urlMap, uri);
//	}
//
//	/**
//	 * 判断是否大清的链接
//	 *
//	 * @param urlMap
//	 * @param uri
//	 * @return
//	 */
//	private String matchUrl(Map<String, RoleCodeEnum[]> urlMap, String uri) {
//		Map<String, RoleCodeEnum[]> thatMap_ = new HashMap<String, RoleCodeEnum[]>();
//		thatMap_.putAll(urlMap);
//		Set<String> keySet = thatMap_.keySet();
//		if (keySet == null || keySet.isEmpty()) {
//			return null;
//		}
//		for (String key : keySet) {
//			String regEx = key;
//			boolean match = Pattern.compile(regEx).matcher(uri).find();
//			if (match) {
//				return key;
//			}
//		}
//		return null;
//	}
//
//	@Override
//	public void init(FilterConfig arg0) throws ServletException {
//	}
//
//	public static void main(String[] args) {
//		String str = "/soouya/pages/Operate/list.do";
//		String regEx = "/soouya/pages/Operate/list.do";
//		Pattern p = Pattern.compile(regEx);
//		Matcher m = p.matcher(str);
//		boolean result = m.find();
//		System.out.println(result);
//	}
//
//	public static String getUri(HttpServletRequest request) {
//		String uri = request.getServletPath();
//		uri = uri.replaceAll("/{2,}", "/").replaceFirst(".*/crm/", "/");
//		return uri;
//	}
//
//}
