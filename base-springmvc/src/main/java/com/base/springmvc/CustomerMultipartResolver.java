package com.base.springmvc;

import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

public class CustomerMultipartResolver extends CommonsMultipartResolver {
	@Override
	public boolean isMultipart(HttpServletRequest request) {
		return (request != null && request.getContentType()!=null && request.getContentType().startsWith("multipart/form-data"));
	}
	
	public CustomerMultipartResolver() {
		super();
	}
	
	public CustomerMultipartResolver(ServletContext servletContext) {
		this();
		setServletContext(servletContext);
	}
}