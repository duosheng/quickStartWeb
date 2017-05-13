package com.soouya.springmvc;

import com.soouya.common.util.ResponseWrapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 拦截器,用于存放渲染视图时需要的的共享变量
 * 
 * @author leo
 * 
 */
public class ResponseWrapperFilter implements Filter {
	static Log log = LogFactory.getLog(ResponseWrapperFilter.class);

	
	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws ServletException,IOException {
		ResponseWrapper response = new ResponseWrapper((HttpServletResponse) servletResponse);
		ServletOutputStream outputStream = response.getOutputStream();
		filterChain.doFilter(servletRequest, response);
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
	}
}
