package javacommon.aspect;

import com.soouya.auth.OmsUser;
import com.soouya.auth.SessionUtil;
import com.soouya.auth.SoouyaUser;
import com.soouya.common.util.ErrorUtil;
import com.soouya.common.util.FileUploadWrapper;
import com.soouya.common.util.IpUtil;
import com.soouya.common.util.ResponseWrapper;
import net.sf.json.JSONObject;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.MDC;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *  本类作为业务日志的切片
 * @author aht
 *
 */
@Aspect
public class LogAspect {

	private static Log log = LogFactory.getLog(LogAspect.class);
	
	//配置切入点,该方法无方法体,主要为方便同类中其他方法使用此处配置的切入点
	// ) && !(public void com.soouya..action.*.prepare())
	@Pointcut("execution(public void com.soouya..action.*.*()) && !execution(public void com.soouya..action.*.prepare())")
	public void aspect(){	}
	
	/*
	 * 配置前置通知,使用在方法aspect()上注册的切入点
	 * 同时接受JoinPoint切入点对象,可以没有该参数
	 */
/*	@Before("aspect()")
	public void before(JoinPoint joinPoint){
		if(log.isInfoEnabled()){
			log.info("before " + joinPoint);
		}
	}*/
	
	//配置后置通知,使用在方法aspect()上注册的切入点
/*	@After("aspect()")
	public void after(JoinPoint joinPoint){
		if(log.isInfoEnabled()){
			log.info("after " + joinPoint);
		}
	}*/
	
	//配置环绕通知,使用在方法aspect()上注册的切入点
	@Around("aspect()")
	public void around(JoinPoint joinPoint){
		HttpServletRequest request = null;
		HttpServletResponse response = null;
		long end = 0;
		long start = 0;
		try {
			Object action = joinPoint.getTarget();
			request = (HttpServletRequest) action.getClass().getMethod("getRequest", new Class[] {}).invoke(action, new Object[] {});
			response = (HttpServletResponse) action.getClass().getMethod("getResponse", new Class[] {}).invoke(action, new Object[] {});

			start = System.currentTimeMillis();


			((ProceedingJoinPoint) joinPoint).proceed();

			end = System.currentTimeMillis();
			
		} catch (Throwable e) {
			end = System.currentTimeMillis();
			ErrorUtil.errorHandle(response, e);
		}
		log(request, response, end-start);
	}

	/**
	 * 参数注入(仿spring的注解绑定,但struts不能使用,因为action方法必须是无参的)
	 * @param
	 * @param
	 * @throws NoSuchMethodException
     */
//	private Object[] paramInject(JoinPoint joinPoint, Object action,HttpServletRequest request) throws NoSuchMethodException {
//		Signature signature = joinPoint.getSignature();
//		String methodName = signature.getName();
//		Class<?> actionCalss = action.getClass();
//		Method method = actionCalss.getMethod(methodName);
////		ReqParam annotation = method.getAnnotation(ReqParam.class);
//		Annotation[][] parameterAnnotations  = method.getParameterAnnotations();
//		Object[] objects = joinPoint.getArgs();
//		for (int i = 0; i < objects.length; i++) {
//			if (parameterAnnotations.length > 0) {
//				for (Annotation annotation : parameterAnnotations[i]) {
//					if (annotation.annotationType() == ReqParam.class) {
//						ReqParam pa = (ReqParam) annotation;
//						String name = pa.name();//获得参数名称
//						String value = request.getParameter(name);
//						objects[1] = value;
//					}
//				}
//			}
//		}
//		return objects;
//	}

	//配置后置返回通知,使用在方法aspect()上注册的切入点
/*	@AfterReturning("aspect()")
	public void afterReturn(JoinPoint joinPoint){
		if(log.isInfoEnabled()){
			log.info("afterReturn " + joinPoint);
		}
	}*/
	
	//配置抛出异常后通知,使用在方法aspect()上注册的切入点
/*	@AfterThrowing(pointcut="aspect()", throwing="ex")
	public void afterThrow(JoinPoint joinPoint, Exception ex){
		if(log.isInfoEnabled()){
			log.info("afterThrow " + joinPoint + "\t" + ex.getMessage());
		}
	}*/
	
	private boolean isFileUploadRequest(HttpServletRequest request) {
		return request.getMethod().equalsIgnoreCase("POST") && request.getContentType() != null
				&& request.getContentType().startsWith("multipart/form-data");
	}
	
	
	private String getParameter(HttpServletRequest request) {
		Map map = request.getParameterMap();
		Set keSet = map.entrySet();
		String parameter = "";
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
				if (!parameter.equals("")) {
					parameter += "&";
				}
				parameter += ("[" + key + "=" + value[k] + "]");
			}
		}
		if (isFileUploadRequest(request)) {
			FileUploadWrapper fileUploadWrapper = null;
			try {
				fileUploadWrapper = new FileUploadWrapper(request);
			} catch (IOException e) {
				e.printStackTrace();
			}
			List<FileItem> fileItems = fileUploadWrapper.getFileItems();
			for (int k = 0; k < fileItems.size(); k++) {
				if (!parameter.equals("")) {
					parameter += "&";
				}
				parameter += ("[file=" + fileItems.get(k).getName() + "]");
			}
		}
		return parameter;
	}
	
	private String pathRoot;
	public void setPathRoot(String root){
		pathRoot = root;
	}
	
	private String getUri(HttpServletRequest request) {
		//request = getMultiReadHttpServletRequest(request);
		String uri = request.getServletPath();
		uri = uri.replaceAll("/{2,}", "/").replaceFirst(".*/" + pathRoot + "/", "/");
		return uri;
	}
	
	private Integer getSuccess(String result) {
		if(StringUtils.isBlank(result)){
			return 10001;
		}
		if("true".equals(result)||"false".equals(result)){
			return 1;
		}
		if(!result.startsWith("{")){
			result = result.substring(result.indexOf("{"),result.lastIndexOf("}")+1);
		}
		JSONObject jsonObject = JSONObject.fromObject(result);
		Map map = (Map) JSONObject.toBean(jsonObject, Map.class);
		return Integer.parseInt((map.get("success").toString()));
	}


	
	/**
	 * -uri-userAgent<=250字节，最大512kb
	 * 
	 * @param responseTime
	 */
	private void log(HttpServletRequest request_, HttpServletResponse response1, long responseTime) {
		try {
			HttpServletRequest request = request_;
			ResponseWrapper response = (ResponseWrapper) response1;

			Map<String, String[]> parameterMap = request.getParameterMap();
			String result = response.toString();
			int success = getSuccess(result);

			String param = "";
			if (isFileUploadRequest(request)) {
				try {
					FileUploadWrapper fileUploadWrapper = new FileUploadWrapper(request);
					param = getParameter(fileUploadWrapper);
					if(StringUtils.isBlank(param)){
						param = getParameter(request);
					}
				} catch (Exception e) {
					ErrorUtil.errorHandle(e);
				}
			} else {
				param = getParameter(request);
			}

			MDC.put("jsessionid", StringUtils.defaultString(SessionUtil.getSessionId(request), "-"));
			MDC.put("userId", getUserId(request));

			String userAgent = StringUtils.defaultString(request.getHeader("User-Agent"), "-");
			userAgent = userAgent.replace(" ", "%20");
			if (userAgent.length() > 2000) {
				userAgent = userAgent.substring(0, 2000) + "...";
			}
			MDC.put("userAgent", userAgent);
			MDC.put("method", StringUtils.defaultString(request.getMethod(), "-"));
			param = param.replace(" ", "%20");
			String uri = getUri(request) + (param == null || param.equals("") ? "" : "?" + param);
			if (uri.length() > 10000) {
				uri = uri.substring(0, 10000) + "...";
			}
			MDC.put("uri", uri);
			MDC.put("remoteIP", StringUtils.defaultString(IpUtil.getRemoteIP(request), "-"));
			MDC.put("localIP", StringUtils.defaultString(IpUtil.getLocalIP(), "-"));
			MDC.put("protocol", StringUtils.defaultString(request.getProtocol(), "-"));
			//MDC.put("id", StringUtils.remove(UUID.randomUUID().toString(), "-"));
			
			//MDC.put("result", StringUtils.defaultString(result, "-"));
			
/*			if (success == 10001 && uri.contains("oper=excel"))
				success = 1;*/
			MDC.put("success", success);
			MDC.put("length", result.length());
			MDC.put("takenTime", responseTime);
			MDC.put("status", response.getStatus());
			log.debug("");
		} catch (Exception e) {
			ErrorUtil.errorHandle(e);
		} finally {
			Map m = MDC.getContext();
			if (m != null) {
				m.clear();
			}
		}
	}

	private String getUserId(HttpServletRequest request){
		if(!StringUtils.isBlank(pathRoot)){
			switch (pathRoot){
				case "oms":
					OmsUser omsUser = SessionUtil.getOmsUser(request);
					return omsUser!=null?omsUser.getUserId():"-";
				case "soouya":
					SoouyaUser soouyaUser = SessionUtil.getSoouyaUser(request);
					return soouyaUser!=null?soouyaUser.getId():"-";
				case "redwood":
					OmsUser redwoodUser = SessionUtil.getOmsUser(request);
					return redwoodUser!=null?redwoodUser.getUserId():"-";
				case "pay":
					SoouyaUser payUser = SessionUtil.getSoouyaUser(request);
					return payUser!=null?payUser.getId():"-";
				default:
					return "-";
			}
		}else {
			return "-";
		}
	}



}
