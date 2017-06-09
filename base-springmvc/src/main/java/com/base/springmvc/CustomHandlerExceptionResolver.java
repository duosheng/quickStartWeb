package com.base.springmvc;

import javacommon.base.exception.BusinessException;
import lombok.extern.log4j.Log4j;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerExceptionResolverComposite;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

@Log4j
public class CustomHandlerExceptionResolver extends HandlerExceptionResolverComposite {
	@Override
    public ModelAndView resolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) {
        Map<String, String> model = new HashMap<String, String>();
        if(e instanceof BusinessException){
        	BusinessException e2=(BusinessException)e;
        	model.put("success", e2.getErrorCode());
        	String msg=e2.getMessage();
        	ResourceBundle messages = ResourceBundle.getBundle("i18n.messages",new Locale("zh","CN"));
    		try {
    			msg = msg == null ? messages.getString(e2.getErrorCode()) : messages.getString(msg);
    		} catch (Exception et) {
    		}
        	model.put("msg", msg);
        	httpServletRequest.setAttribute("success", e2.getErrorCode());
        }else{
        	model.put("success", "10001");
        	model.put("msg", "系统未知异常");
        	log.error(e.getMessage(),e);
        	httpServletRequest.setAttribute("success", "10001");
        }
        MappingJackson2JsonView view=new MappingJackson2JsonView();
        view.setAttributesMap(model);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setView(view);
        return modelAndView;
    }
}
