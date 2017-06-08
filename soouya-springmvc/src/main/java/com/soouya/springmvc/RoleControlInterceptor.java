package com.soouya.springmvc;

import javacommon.base.exception.BusinessException;
import com.soouya.common.util.Code;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

/**
 * Created by xuyuli on 2016/12/8.
 *
 * 权限管理控制器
 */
public class RoleControlInterceptor extends HandlerInterceptorAdapter {

    private IRoleListGet iRoleListGet;

    public void setiRoleListGet(IRoleListGet iRoleListGet) {
        this.iRoleListGet = iRoleListGet;
    }

    static Log log = LogFactory.getLog(LogInterceptor.class);
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        HttpSession s=request.getSession();
        //角色权限控制访问
        boolean b = roleControl(request, response, handler);
        if(!b){
            throw new BusinessException("10099");
        }
        return b;
    }
    /**角色权限控制访问*/
    private boolean roleControl(HttpServletRequest request,HttpServletResponse response, Object handler){
        HttpSession session=request.getSession();
//        System.out.println(handler.getClass().getName());
        if(handler instanceof HandlerMethod){
            HandlerMethod hm=(HandlerMethod)handler;
            Object target=hm.getBean();
            Class<?> clazz=hm.getBeanType();
            Method m=hm.getMethod();
            String code = Code.NO_AUTH;
            try {
                if (clazz!=null && m != null ) {
                    boolean isClzAnnotation= clazz.isAnnotationPresent(RoleControl.class);
                    boolean isMethondAnnotation=m.isAnnotationPresent(RoleControl.class);
                    RoleControl rc = null;
                    //如果方法和类声明中同时存在这个注解，那么方法中的会覆盖类中的设定。
                    if(isMethondAnnotation){
                        rc=m.getAnnotation(RoleControl.class);
                    }else if(isClzAnnotation){
                        rc=clazz.getAnnotation(RoleControl.class);
                    }
                    //如果没有配置注解,则不需要登陆
                    if(rc == null){
                        return true;
                    }
                    //要求的角色
                    RoleCodeEnum[] value = rc.value();
                    List<RoleCodeEnum> roleCodeList = Arrays.asList(value);
                    //实际的登陆用户
                    // TODO 根据具体的业务
//                    List<RoleBo> roleList = iRoleListGet.get();
//                    if (roleList == null) {
//                        throw new BusinessException(Code.NO_LOGIN, "用户未登录，权限认证失败");
//                    }
//                    if (roleList.size() == 0) {
//                        throw new BusinessException(Code.NO_LOGIN, "用户没有权限，权限认证失败");
//                    }
//
//                    if(roleCodeList.contains(RoleCodeEnum.ALL)){
//                        return true;
//                    }
//
//
//
//                    if (iRoleListGet.isAdmin()) {
//                        return true;// 管理员
//                    }
//
//                    if(roleList!=null && roleList.size()>0){
//                        for (RoleBo role : roleList) {
//                            String roleCode = role.getCode();
//                            if (roleCodeList.contains(RoleCodeEnum.fromCode(roleCode))) {
//                                return true;// auth success
//                            }
//                        }
//                    }

                    return false;// auth fail
                }
            }catch(Exception e){
                e.printStackTrace();
                log.error("权限控制失败",e);
            }
        }else {
            return true;
        }
        return false;
    }




}
