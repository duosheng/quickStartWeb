package javacommon.base.mybatis.interceptor;


import javacommon.base.mybatis.paging.dialetc.Dialect;
import javacommon.base.mybatis.paging.dialetc.MySqlDialect;
import javacommon.base.mybatis.paging.dialetc.OracleDialect;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.RowBounds;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Properties;


/**
 *
 * 描述：连接处理Sql 实现物理分页的功能
 *
 */
@Intercepts({@Signature(type=StatementHandler.class,method="prepare",args={Connection.class})})
public class PaginationInterceptor extends AbstractInterceptor {

	//日志服务对象
//    static Logger log = Logger.getLogger(PaginationInterceptor.class);

	@Override
    public Object intercept(Invocation invocation) throws Throwable {

//    	    System.out.println("...................PaginationInterceptor...............");

		    StatementHandler statementHandler = (StatementHandler)invocation.getTarget();

		    MetaObject metaStatementHandler = MetaObject.forObject(statementHandler, SystemMetaObject.DEFAULT_OBJECT_FACTORY, SystemMetaObject.DEFAULT_OBJECT_WRAPPER_FACTORY);


		    Object t = statementHandler.getParameterHandler().getParameterObject();

		    PreparedStatement st = null;


		    RowBounds rowBounds = (RowBounds)metaStatementHandler.getValue("delegate.rowBounds");
		    if(rowBounds == null || rowBounds == RowBounds.DEFAULT){
		        return invocation.proceed();
		    }

		    String originalSql = (String)metaStatementHandler.getValue("delegate.boundSql.sql");


		    Configuration configuration = (Configuration)metaStatementHandler.getValue("delegate.configuration");

		    Dialect.Type databaseType  = null;
		    try{
		        databaseType = Dialect.Type.valueOf(configuration.getVariables().getProperty("dialect").toUpperCase());
		    } catch(Exception e){
		        //ignore
		    }
		    if(databaseType == null){
		        throw new RuntimeException("the value of the dialect property in configuration.xml is not defined : " + configuration.getVariables().getProperty("dialect"));
		    }
		    Dialect dialect = null;
		    switch(databaseType){
		        case ORACLE:
		            dialect = new OracleDialect();
		            break;
		        case MYSQL://需要实现MySQL的分页逻辑
		        	dialect = new MySqlDialect();
		            break;

		    }

		    metaStatementHandler.setValue("delegate.boundSql.sql", dialect.getLimitString(originalSql, rowBounds.getOffset(), rowBounds.getLimit()) );
		    metaStatementHandler.setValue("delegate.rowBounds.offset", RowBounds.NO_ROW_OFFSET );
		    metaStatementHandler.setValue("delegate.rowBounds.limit", RowBounds.NO_ROW_LIMIT);
//		    if(log.isDebugEnabled()){
//		        BoundSql boundSql = statementHandler.getBoundSql();
//		        //log.debug("生成分页SQL : " + boundSql.getSql());
//		    }
//		BoundSql boundSql = statementHandler.getBoundSql();
//		System.out.println("生成分页SQL : " + boundSql.getSql());
		return invocation.proceed();
    }



	/* (non-Javadoc)
         * @see org.apache.ibatis.plugin.Interceptor#plugin(java.lang.Object)
         */
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }



    /* (non-Javadoc)
     * @see org.apache.ibatis.plugin.Interceptor#setProperties(java.util.Properties)
     */
    public void setProperties(Properties arg0) {
        // TODO Auto-generated method stub

    }

}