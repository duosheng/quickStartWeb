package javacommon.base.mybatis.paging.dialetc;


/**
 * 
 * 描述：MyBatis实现物理分页  Oracle
 * @author Ethan.Lam
 * @dateTime 2012-6-2
 *
 */
public class OracleDialect extends Dialect{    
    
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
        
        StringBuffer pagingSelect = new StringBuffer(sql.length() + 100);    
            
        pagingSelect.append("select * from ( select row_.*, rownum rownum_ from ( ");    
            
        pagingSelect.append(sql);    
            
        pagingSelect.append(" ) row_ ) where rownum_ >= ").append(offset).append(" and rownum_ <= ").append(offset + limit - 1);    
            
        return pagingSelect.toString();    
    }    
    
}    