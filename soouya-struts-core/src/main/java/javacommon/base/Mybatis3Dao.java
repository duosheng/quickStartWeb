package javacommon.base;

import com.soouya.common.model.Page;
import com.soouya.common.model.PageRequest;
import com.soouya.common.util.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.List;
import java.util.Map;


/**
 * @author badqiu
 * @version 1.0
 */
public abstract class Mybatis3Dao<E, PK extends Serializable> implements IEntityDao<E, PK> {
    protected final Log log = LogFactory.getLog(getClass());

    
    @Autowired
	private MyBatisDaoSupport myBatisDaoSupport;



    public void setMyBatisDaoSupport(MyBatisDaoSupport myBatisDaoSupport) {
		this.myBatisDaoSupport = myBatisDaoSupport;
	}

	/**
	 * 功能描述：返回 SqlSession 的方式
	 */
	protected SqlSession getSqlSession() {
		return this.myBatisDaoSupport.getSqlSession();
	}


//    @Autowired
//    private SqlSessionTemplate sqlSessionTemplate;
//
//    public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
//        this.sqlSessionTemplate = sqlSessionTemplate;
//    }
//
//    public SqlSessionTemplate getSqlSession() {
//        return sqlSessionTemplate;
//    }
	

	/**
	 * 功能描述：定义命名空间
	 */
	protected String mapNameSpace() {
		return "";
	}

    @Override
    public E getById(PK primaryKey) {
        Object object = getSqlSession().selectOne(getFindByPrimaryKeyStatement(), primaryKey);
        return (E)object;
    }

    @Override
    public void deleteById(PK id) {
    	getSqlSession().delete(getDeleteStatement(), id);
    }

    @Override
    public void save(E entity) {
        prepareObjectForSaveOrUpdate(entity);
        getSqlSession().insert(getInsertStatement(), entity);
    }

    @Override
    public void update(E entity) {
        // prepareObjectForSaveOrUpdate(entity);
    	getSqlSession().update(getUpdateStatement(), entity);
    }

    protected final String getFullTemplateName(String tempName){
        return getMybatisMapperNamesapce()+"." + tempName;
    }


    /**
     * 用于子类覆盖,在insert,update之前调用
     */
    protected void prepareObjectForSaveOrUpdate(E o) {
    }

    public String getMybatisMapperNamesapce() {
        return this.getClass().getName();
    }

    public String getFindByPrimaryKeyStatement() {
        return getMybatisMapperNamesapce() + ".getById";
    }

    public String getInsertStatement() {
        return getMybatisMapperNamesapce() + ".insert";
    }

    public String getUpdateStatement() {
        return getMybatisMapperNamesapce() + ".update";
    }

    public String getDeleteStatement() {
        return getMybatisMapperNamesapce() + ".delete";
    }

    public String getCountStatementForPaging(String statementName) {
        return statementName + "_count";
    }

    @Override
    public Page findPage(PageRequest pageRequest){
        String fullStatementName = getMybatisMapperNamesapce() + ".findPage";
        return  pageQuery(fullStatementName, pageRequest) ;
    }


    public Page pageQuery(String statementName, PageRequest pageRequest) {
        return pageQuery(getSqlSession(), statementName, getCountStatementForPaging(statementName), pageRequest);
    }

    @Override
    public List<E> findList(PageRequest pageRequest) {
        String fullStatementName = getMybatisMapperNamesapce() + ".findPage";
        List list = getSqlSession().selectList(fullStatementName, pageRequest);
        return list;
    }

    /**
     * 根据statementName分页查询
     * @param statementName
     * @param pageRequest
     * @return
     */
    @Override
    public Page findPage(String statementName, PageRequest pageRequest) {
        String fullStatementName = getMybatisMapperNamesapce() + statementName;
        return  pageQuery(fullStatementName, pageRequest) ;
    }

    //TODO 测试
    private static String getSortColumns(String sortColumns) {
        if(StringUtils.isBlank(sortColumns)){
            return "";
        }
        String newString = "";
        sortColumns = sortColumns.trim();
        try {
            String[] scs = sortColumns.split(",");
            for (int i = 0; i < scs.length; i++) {
                if (i > 0) {
                    newString += ",";
                }
                String[] sc = scs[i].split(" ");
                newString += getColumn(sc[0]);
                if (sc[1] != null && !sc[1].equals("")) {
                    newString += " " + sc[1];
                }
            }
        } catch (Exception e) {
            return "";
        }
        return newString;
    }


    @SuppressWarnings({ "rawtypes", "unused", "unchecked" })
	private Page pageQuery(SqlSession sqlSession, String statementName, String countStatementName, PageRequest pageRequest) {

        Number totalCount = (Number) sqlSession.selectOne(countStatementName, pageRequest);
        if (totalCount == null || totalCount.longValue() <= 0) {
            return new Page(pageRequest, 0);
        }

        Page page = new Page(pageRequest, totalCount.intValue());

        String sortColumns = getSortColumns(pageRequest.getSortColumns());
        if (!sortColumns.equals("")) {
            pageRequest.setSortColumns(sortColumns);
        }


        List list = sqlSession.selectList(statementName, pageRequest, new RowBounds(page.getFirstResult(), page.getPageSize()));
        page.setResult(list);
        return page;
    }
    
    public Page pageQueryNoLimit(String statementName, PageRequest pageRequest) {
        return pageQueryNoLimit(getSqlSession(), statementName, getCountStatementForPaging(statementName), pageRequest);
    }
    
    @SuppressWarnings({ "rawtypes", "unused", "unchecked" })
    private Page pageQueryNoLimit(SqlSession sqlSession, String statementName, String countStatementName, PageRequest pageRequest) {

        Number totalCount = (Number) sqlSession.selectOne(countStatementName, pageRequest);
        if (totalCount == null || totalCount.longValue() <= 0) {
            return new Page(pageRequest, 0);
        }

        Page page = new Page(pageRequest, totalCount.intValue());

        // 其它分页参数,用于不喜欢或是因为兼容性而不使用方言(Dialect)的分页用户使用.
        // 与getSqlMapClientTemplate().queryForList(statementName,
        // parameterObject)配合使用
        pageRequest.setOffset(page.getFirstResult());
        pageRequest.setPageSize(page.getPageSize());
        pageRequest.setLastRows(page.getFirstResult() + page.getPageSize());
        String sortColumns = getSortColumns(pageRequest.getSortColumns());
        if (!sortColumns.equals("")) {
            pageRequest.setSortColumns(sortColumns);
        }

        Map parameterObject = PropertyUtils.describe(pageRequest);

        List list = sqlSession.selectList(statementName, pageRequest);
        page.setResult(list);
        return page;
    }
    
    public Page pageQueryNoLimitNoCount(String statementName, PageRequest pageRequest) {
        return pageQueryNoLimitNoCount(getSqlSession(), statementName, getCountStatementForPaging(statementName), pageRequest);
    }
    
    @SuppressWarnings({ "rawtypes", "unused", "unchecked" })
    private Page pageQueryNoLimitNoCount(SqlSession sqlSession, String statementName, String countStatementName,
                                                 PageRequest pageRequest) {

        if(pageRequest.getPageSize()<=0){
            pageRequest.setPageSize(10);
        }
        Page page = new Page(pageRequest,Integer.MAX_VALUE);
        // 其它分页参数,用于不喜欢或是因为兼容性而不使用方言(Dialect)的分页用户使用.
        // 与getSqlMapClientTemplate().queryForList(statementName,
        // parameterObject)配合使用
        // 为了判断是否有下一页
        // 多取一条
        int prevPageSize = page.getPageSize();
        int realPageSize = page.getPageSize() + 1;
        pageRequest.setOffset(page.getFirstResult());
        pageRequest.setLastRows(page.getFirstResult() + page.getPageSize());
        String sortColumns = getSortColumns(pageRequest.getSortColumns());
        if (!sortColumns.equals("")) {
            pageRequest.setSortColumns(sortColumns);
        }

        pageRequest.setPageSize(realPageSize);

        List list = sqlSession.selectList(statementName, pageRequest);
        page.setResult(list);

        Page pageRet = new Page();
        pageRet.setTotalCount(Integer.MAX_VALUE);
        boolean isHasNextPage = (list.size() == realPageSize);
        pageRet.setHasNextPage(isHasNextPage);
        pageRet.setPageNumber(pageRequest.getPageNumber());
        pageRet.setPageSize(pageRequest.getPageSize());
        if(isHasNextPage){
            pageRet.setResult(list.subList(0, prevPageSize));
        }else{
            pageRet.setResult(list);
        }
        return pageRet;
    }



    /**
     * java类属性转数据库的字段
     *
     * @param field
     * @return
     */
    public static String getColumn(String field) {
        String newString = "";
        try {
            for (int j = 0; j < field.length(); j++) {
                char charCode = field.charAt(j);
                if (charCode >= 'A' && charCode <= 'Z') {
                    newString += "_" + (char) (charCode + 32);
                } else {
                    newString += charCode;
                }
            }
        } catch (Exception e) {
            return "";
        }
        if (newString.equals("create_time_string")) {
            newString = "create_time";
        }
        if (newString.equals("edit_time_string")) {
            newString = "edit_time";
        }
        return newString;
    }






}
