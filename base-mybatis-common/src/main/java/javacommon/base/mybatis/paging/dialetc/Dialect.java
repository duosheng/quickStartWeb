package javacommon.base.mybatis.paging.dialetc;

/**
 * 
 * 描述：数据库方言 类型
 * @author Ethan.Lam
 * @dateTime 2012-6-2
 *
 */
public abstract class Dialect {

	public static enum Type {
		MYSQL, ORACLE
	}

	public abstract String getLimitString(String sql, int skipResults,
			int maxResults);

}