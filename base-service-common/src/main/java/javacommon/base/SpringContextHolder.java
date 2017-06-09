package javacommon.base;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Map;

/**
 * @author Administrator
 *
 */
public class SpringContextHolder implements ApplicationContextAware {
    private static Logger logger = Logger.getLogger(SpringContextHolder.class);

    private static ApplicationContext applicationContext;

    /**
     * 实现ApplicationContextAware接口的context注入函数, 将其存入静态变量.
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContextHolder.applicationContext = applicationContext;
    }

    /**
     * 取得存储在静态变量中的ApplicationContext.
     *
     * @return
     */
    public static ApplicationContext getApplicationContext() {
        checkApplicationContext();
        return applicationContext;
    }

    /**
     * 从静态变量ApplicationContext中取得Bean, 自动转型为所赋值对象的类型.
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBean(String name) {
        checkApplicationContext();
        try {
            T bean = (T) applicationContext.getBean(name);
            if (bean != null) {
                return bean;
            }
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * 从静态变量ApplicationContext中取得Bean, 自动转型为所赋值对象的类型. 如果有多个Bean符合Class, 取出第一个.
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBean(Class<T> clazz) {
        checkApplicationContext();
        Map beanMaps = null;
        try {
            beanMaps = applicationContext.getBeansOfType(clazz);
        } catch (Exception e) {
            logger.error("encounter exception when get bean(class=" + clazz.getName() + ")");
            logger.error(null, e);
        }
        if (beanMaps != null && !beanMaps.isEmpty()) {
            return (T) beanMaps.values().iterator().next();
        } else {
            return null;
        }
    }

    private static void checkApplicationContext() {
        if (applicationContext == null) {
            throw new IllegalStateException("applicaitonContext未注入,请在applicationContext.xml中定义SpringContextHolder");
        }
    }
}
