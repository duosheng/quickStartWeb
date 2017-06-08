package javacommon.base.mybatis.interceptor;

import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Plugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public abstract class AbstractInterceptor implements Interceptor {

	protected Logger logger;
	protected Properties properties;

	public AbstractInterceptor() {
		logger = LoggerFactory.getLogger(getClass());
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
		afterSetProperties();
	}

	protected void afterSetProperties() {
	}

	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

}