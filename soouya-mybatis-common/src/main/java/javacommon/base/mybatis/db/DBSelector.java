package javacommon.base.mybatis.db;

import com.google.common.base.Strings;
import javacommon.base.mybatis.paging.dialetc.Dialect;
import javacommon.base.mybatis.paging.dialetc.MySqlDialect;
import javacommon.base.mybatis.utils.SpringHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * 数据库方言选择,优先指定方言,后则自动查询数据库类型
 * 根据spring的配置，自动选择数据库类型
 * @author xuyuli
 * 
 */
public class DBSelector extends SpringHolder {
	private String dbType;

	private final static Logger logger = LoggerFactory.getLogger(DBSelector.class);

	private static Map<String, DBType> DATABASES = new HashMap<String, DBType>();

	static {
		DATABASES.put("mysql", DBType.MYSQL);
		DATABASES.put("sqlserver", DBType.SQLSERVER);
		DATABASES.put("sql server", DBType.SQLSERVER);
		DATABASES.put("sqlserver2005", DBType.SQLSERVER2005);
		DATABASES.put("oracle", DBType.ORACLE);
		DATABASES.put("db2", DBType.DB2);
		DATABASES.put("h2", DBType.H2);
	}

	/** 选择数据库方言,优先指定数据库方言,后则自动查询数据库选择 */
	public Dialect selectDialect() {
		return createDialect(selectDBType());
	}

	/** 获取数据库类型 */
	public DBType selectDBType() {
		if (!Strings.isNullOrEmpty(dbType)) {
			DBType dbType = DATABASES.get(this.dbType);
			if (dbType != null) {
				return dbType;
			}
		}
		return judgeDbTypeByDBName(getDBProductName().toLowerCase());
	}

	/**
	 * 通过数据库类型创建方言
	 * 暂时只支持mysql  后期考虑是否加入
	 * */
	private Dialect createDialect(DBType dbType) {
		Dialect dialect = new MySqlDialect();
//		if (DBType.MYSQL.equals(dbType)) {
//			dialect = new MySQLDialect();
//		} else if (DBType.ORACLE.equals(dbType)) {
//			dialect = new OracleDialect();
//		} else if (DBType.SQLSERVER.equals(dbType)) {
//			dialect = new SQLServerDialect();
//		} else if (DBType.SQLSERVER2005.equals(dbType)) {
//			dialect = new SQLServer2005Dialect();
//		} else if (DBType.DB2.equals(dbType)) {
//			dialect = new DB2Dialect();
//		} else if (DBType.H2.equals(dbType)) {
//			dialect = new H2Dialect();
//		} else {
//			dialect = new OracleDialect();
//		}
		return dialect;
	}

	/** 数据库描述选择数据库类型 */
	private DBType judgeDbTypeByDBName(String productName) {
		if (productName.indexOf("mysql") != -1) {
			return DBType.MYSQL;
		}
		if (productName.indexOf("sqlserver") != -1) {
			return DBType.SQLSERVER;
		}
		if (productName.indexOf("sql server") != -1) {
			return DBType.SQLSERVER;
		}
		if (productName.indexOf("sqlserver2005") != -1) {
			return DBType.SQLSERVER2005;
		}
		if (productName.indexOf("oracle") != -1) {
			return DBType.ORACLE;
		}
		if (productName.indexOf("db2") != -1) {
			return DBType.DB2;
		}
		if (productName.indexOf("h2") != -1) {
			return DBType.H2;
		}
		if (productName.indexOf("sysbase") != -1) {
			return DBType.SYSBASE;
		}
		return DBType.ORACLE;
	}

	/** 获取数据库类型描述 */
	private String getDBProductName() {
		DataSource ds = SpringHolder.getBean(DataSource.class);
		if (ds == null) {
			ds = SpringHolder.getBean(DataSource.class, "dataSource");
		}
		Connection conn = null;
		try {
			conn = ds.getConnection();
			return conn.getMetaData().getDatabaseProductName();
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				logger.error(e.getMessage(), e);
			} finally {
				conn = null;
			}
		}
		return null;
	}

	public String getDbType() {
		return dbType;
	}

	public void setDbType(String dbType) {
		this.dbType = dbType;
	}

	public static boolean isOracle() {
		return DBType.ORACLE.equals(getCurrDBType());
	}

	public static boolean isMySQL() {
		return DBType.MYSQL.equals(getCurrDBType());
	}

	public static boolean isSqlServer() {
		return DBType.SQLSERVER.equals(getCurrDBType());
	}

	public static boolean isSqlServer2005() {
		return DBType.SQLSERVER2005.equals(getCurrDBType());
	}

	public static boolean isDB2() {
		return DBType.DB2.equals(getCurrDBType());
	}

	public static boolean isH2() {
		return DBType.H2.equals(getCurrDBType());
	}

	public static boolean isSysbase() {
		return DBType.SYSBASE.equals(getCurrDBType());
	}

	private static DBType getCurrDBType() {
		return SpringHolder.getBean(DBSelector.class).selectDBType();
	}
}
