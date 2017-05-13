package javacommon.dao.dialetc;


/**
 *
 * 描述：MyBatis实现物理分页  MySql
 * @author Ethan.Lam
 * @dateTime 2012-6-2
 *
 */
public class MySqlDialect extends Dialect{

    /**
     * (non-Javadoc)
     * @see esfw.core.framework.dao.extend.dialect.Dialect#getLimitString(String, int, int)
     * 功能描述：
     * @author Ethan.Lam
     * @dateTime 2012-6-2
     * @param sql
     * @param offset
     * @param limit
     * @return
     *
     */
    @Override
    public String getLimitString(String sql, int offset, int limit) {
        sql = sql.trim();

        StringBuffer pagingSelect = new StringBuffer(sql.length() + 30);

        //pagingSelect.append("select * from ( ");

        pagingSelect.append(sql);

        //pagingSelect.append(" ) a LIMIT  ").append(offset).append(" , ").append(limit);
        pagingSelect.append(" LIMIT  ").append(offset).append(" , ").append(limit);

//        System.out.println("SQL:*********************************************************");
//        System.out.println(sql);
//        System.out.println("SQL:*********************************************************");

        return pagingSelect.toString();
    }

}