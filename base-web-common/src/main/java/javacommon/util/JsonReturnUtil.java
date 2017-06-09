package javacommon.util;

import net.sf.json.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Created by xuyuli on 2016/6/14.
 */
public class JsonReturnUtil {

    protected static Log log = LogFactory.getLog(JsonReturnUtil.class);

    /**
     * msg国际化
     *
     * @param request
     * @param code
     * @return
     */
    public static String getMsg(HttpServletRequest request, String code) {
        try {
            String requestLocale = request.getParameter("request_locale");
            if (requestLocale == null || requestLocale.indexOf("_") < 0) {
                requestLocale = "zh_CN";
            }
            String[] locales = requestLocale.split("_");
            Locale locale = new Locale(locales[0], locales[1]);
            ResourceBundle messages = ResourceBundle.getBundle("i18n.messages", locale);
            String[] codes = code.split(",");
            String msg = messages.getString(codes[0]);
            for (int i = 1; i < codes.length; i++) {
                msg = msg.replaceAll("\\{" + (i - 1) + "\\}", codes[i]);
            }
            return msg;
        } catch (Exception e) {
            return code;
        }
    }

    /**
     * 如果含有jsonp参数，返回jsonP,否则返回普通json
     *
     * @param jsonStr
     * @param request
     * @param response
     */
    public static void returnJsonP(String jsonStr, HttpServletRequest request, HttpServletResponse response) {
        String jsonpCallback = request.getParameter("jsonpcallback");

        if (jsonpCallback == null || jsonpCallback.isEmpty()) {
            returnJson(jsonStr, response);
        } else {
            String resStr = String.format("%s(%s)", jsonpCallback, jsonStr);
            returnJson(resStr, response);
        }
    }

    /**
     *
     * @param jsonStr
     *            要返回的json对象
     * @param response
     */
    public static void returnJson(String jsonStr, HttpServletResponse response) {

        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");

        try {
            response.getWriter().flush();
            // System.out.println(jsonStr.toString());
            response.getWriter().write(jsonStr);
        } catch (IllegalStateException e) {
            log.error("1---------------------------------------");
            e.printStackTrace();
            log.error("1---------------------------------------");
        } catch (Exception e) {
            log.error("2---------------------------------------");
            e.printStackTrace();
            log.error("2---------------------------------------");
        }
    }

    /**
     * 组装msg和code到json
     *
     * @param resJson
     * @param code
     */
    public static void accumulateCodeAndMsg(JSONObject resJson, String code, HttpServletRequest request) {
        resJson.accumulate("code", code);
        resJson.accumulate("message", getMsg(request, code));
        resJson.accumulate("success", code);
        resJson.accumulate("msg", getMsg(request, code));
    }

    /**
     * 组装msg和code到json
     *
     * @param resJson
     * @param code
     * @param msg
     * @param request
     */
    public static void accumulateCodeAndMsg(JSONObject resJson, String code, String msg, HttpServletRequest request) {
        resJson.accumulate("code", code);
        resJson.accumulate("success", code);
        if (org.apache.commons.lang.StringUtils.isNotBlank(msg)) {
            resJson.accumulate("message",  msg);
            resJson.accumulate("msg",  msg);
        } else {
            resJson.accumulate("message", getMsg(request, code));
            resJson.accumulate("msg", getMsg(request, code));
        }
    }

    /**
     * 组装msg和code到json
     *
     * @param resJson
     * @param code
     * @param msg
     * @param request
     */
    public static void putCodeAndMsg(JSONObject resJson, String code, String msg, HttpServletRequest request) {
        resJson.put("code", code);
        resJson.put("success", code);
        if (org.apache.commons.lang.StringUtils.isNotBlank(msg)) {
            resJson.put("message",  msg);
            resJson.put("msg", msg);
        } else {
            resJson.put("message", getMsg(request, code));
            resJson.put("msg", getMsg(request, code));
        }
    }

    /**
     * msg 中文
     *
     * @param code
     * @return
     */
    public static String getCnMsg(String code) {
        try {
            String requestLocale = requestLocale = "zh_CN";
            String[] locales = requestLocale.split("_");
            Locale locale = new Locale(locales[0], locales[1]);
            ResourceBundle messages = ResourceBundle.getBundle("i18n.messages", locale);
            String[] codes = code.split(",");
            String msg = messages.getString(codes[0]);
            for (int i = 1; i < codes.length; i++) {
                msg = msg.replaceAll("\\{" + (i - 1) + "\\}", codes[i]);
            }
            return msg;
        } catch (Exception e) {
            return code;
        }
    }

    public static void returnErrorJsonP(HttpServletRequest request, HttpServletResponse response, String errCode){
        JSONObject obj = new JSONObject();
        JsonReturnUtil.accumulateCodeAndMsg(obj, errCode, request);
        JsonReturnUtil.returnJsonP(obj.toString(), request, response);
    }

}
