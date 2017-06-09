<%@page import="com.base.common.util.ErrorUtil"%>
<%@ page language="java" import="java.util.*,net.sf.json.*" pageEncoding="UTF-8"%>
<%
	Map map=new HashMap();
	map.put("success", "10001");
	map.put("msg", "系统未知异常");
	JSONObject jo=JSONObject.fromObject(map);
	String tmp=jo.toString();
	response.setCharacterEncoding("UTF-8");
	
	response.setContentType("application/json");
	try {
		response.getWriter().write(tmp);
	} catch (Exception e) {
		// e.printStackTrace();
		ErrorUtil.errorHandle(e);
	}
%>