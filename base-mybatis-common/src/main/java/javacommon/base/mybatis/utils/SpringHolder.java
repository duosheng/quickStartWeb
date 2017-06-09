package javacommon.base.mybatis.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * SPRING上下文工具类
 *
 * @author shadow
 *
 */
public class SpringHolder implements ApplicationContextAware {

	private static ApplicationContext context;

	public void setApplicationContext(ApplicationContext contex) throws BeansException {
		setContext(contex);
	}

	public static ApplicationContext getContext() {
		return context;
	}

	public static void setContext(ApplicationContext context) {
		SpringHolder.context = context;
	}

	public static <T> T getBean(Class<T> clazz) {
		return context.getBean(clazz);
	}

	public static <T> T getBean(Class<T> clazz, String name) {
		return context.getBean(name, clazz);
	}

	public static Object getBean(String name) {
		return context.getBean(name);
	}

}