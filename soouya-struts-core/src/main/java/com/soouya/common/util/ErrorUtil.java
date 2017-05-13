package com.soouya.common.util;

import com.soouya.common.exception.BusinessException;
import com.soouya.webservice.exception.CXFBusinessException;
import javacommon.util.JsonReturnUtil;
import net.sf.json.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.springframework.transaction.UnexpectedRollbackException;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;

public class ErrorUtil {
    protected static Log log = LogFactory.getLog(ErrorUtil.class);

    public static void errorHandle(HttpServletResponse response, Throwable t){
        if (t instanceof BusinessException) {
            BusinessException be = (BusinessException) t;
            String code = be.getErrorCode();
            String message = be.getMessage();

            JSONObject resJson = new JSONObject();
            JsonReturnUtil.putCodeAndMsg(resJson, code, message, ServletActionContext.getRequest());
            JsonReturnUtil.returnJsonP(resJson.toString(), ServletActionContext.getRequest(), response);
            // t.printStackTrace();
            StringWriter stringWriter = new StringWriter();
            PrintWriter writer = new PrintWriter(stringWriter);
            t.printStackTrace(writer);
            StringBuffer buffer = stringWriter.getBuffer();
            log.warn(buffer.toString());
        } else if(t instanceof CXFBusinessException){
            CXFBusinessException be = (CXFBusinessException) t;
            String code = be.getCode()+"";
            String message = be.getMsg();

            JSONObject resJson = new JSONObject();
            JsonReturnUtil.putCodeAndMsg(resJson, code, message, ServletActionContext.getRequest());
            JsonReturnUtil.returnJsonP(resJson.toString(), ServletActionContext.getRequest(), response);
            // t.printStackTrace();
            StringWriter stringWriter = new StringWriter();
            PrintWriter writer = new PrintWriter(stringWriter);
            t.printStackTrace(writer);
            StringBuffer buffer = stringWriter.getBuffer();
            log.warn(buffer.toString());
        }else if(t instanceof UnexpectedRollbackException){
            log.warn("spring 抛出 UnexpectedRollbackException ,什么都不做");
        }else {

            JSONObject resJson = new JSONObject();
            JsonReturnUtil.putCodeAndMsg(resJson, Code.FATAL, "系统错误", ServletActionContext.getRequest());
            JsonReturnUtil.returnJsonP(resJson.toString(), ServletActionContext.getRequest(), response);

            StringWriter stringWriter = new StringWriter();
            PrintWriter writer = new PrintWriter(stringWriter);
            t.printStackTrace(writer);
            StringBuffer buffer = stringWriter.getBuffer();
            log.error(buffer.toString());
        }
    }

    public static void errorHandle(Throwable t) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        t.printStackTrace(writer);
        StringBuffer buffer = stringWriter.getBuffer();
        t.printStackTrace();
        if (t instanceof BusinessException || t instanceof CXFBusinessException) {
            log.warn(buffer.toString());
        }else {
            log.error(buffer.toString());
        }
    }


}
