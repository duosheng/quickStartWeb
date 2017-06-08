package javacommon.base.mybatis.interceptor;

import javacommon.base.mybatis.utils.MybatisUtils;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

/**
 * 
 * 基于Mybatis实现的SQL监视器，实现对象SQL拦截，以便可以将每个操作SQL记入日志中
 * 
 */
@Intercepts({
		@Signature(type = Executor.class, method = "update", args = { MappedStatement.class, Object.class }),
		@Signature(type = Executor.class, method = "query", args = { MappedStatement.class, Object.class,
				RowBounds.class, ResultHandler.class }) })
public class SqlMonitorInterceptor extends AbstractInterceptor {

	static int MAPPED_STATEMENT_INDEX = 0;
	static int PARAMETER_INDEX = 1;

	private Boolean enableMonitor = Boolean.TRUE;

	public Object intercept(Invocation invoker) throws Throwable {
		long time = System.currentTimeMillis();
		try {
			return invoker.proceed();
		} finally {
			if (enableMonitor) {
				printsql(invoker);
				logger.debug("Runing time: " + String.valueOf(System.currentTimeMillis() - time) + "ms");
			}
		}
	}

	protected void printsql(Invocation invoker) {
		Object[] queryArgs = invoker.getArgs();
		MappedStatement ms = (MappedStatement) queryArgs[MAPPED_STATEMENT_INDEX];
		Object parameterObject = queryArgs[PARAMETER_INDEX];
		String sql = MybatisUtils.getFilledSql(ms, parameterObject);
		logger.debug("\nSQL MESSAGE:" + ms.getId() + ":" + sql);
	}

	public Boolean getEnableMonitor() {
		return enableMonitor;
	}

	public void setEnableMonitor(Boolean enableMonitor) {
		this.enableMonitor = enableMonitor;
	}
}
