package javacommon.base;

import javacommon.base.exception.BusinessException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

/**
 * @author badqiu
 */
@Transactional
public abstract class BaseManager<E, PK extends Serializable> implements IBaseManager<E, PK> {

    protected Log log = LogFactory.getLog(getClass());

    protected abstract EntityDao getEntityDao();

    @Override
    @Transactional(readOnly = true)
    public Page findPage(PageRequest pageRequest){
        return getEntityDao().findPage(pageRequest);
    }

    @Override
    public Page findPage(String statementName, PageRequest pageRequest) {
        return getEntityDao().findPage(statementName,pageRequest);
    }

    @Transactional(readOnly = true)
    @Override
    public E getById(PK id) throws BusinessException {
        return (E) getEntityDao().getById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<E> findAll() throws BusinessException {
        return getEntityDao().findAll();
    }

    @Override
    public List<E> findList(PageRequest pageRequest) {
        return getEntityDao().findList(pageRequest);
    }

    /** 根据id检查是否插入或是更新数据 */
    @Override
    public void saveOrUpdate(E entity) throws BusinessException {
        getEntityDao().saveOrUpdate(entity);
    }

    /** 插入数据 */
    @Override
    public void save(E entity) throws BusinessException {
        getEntityDao().save(entity);
    }

    @Override
    public void deleteById(PK id) throws BusinessException {
        getEntityDao().deleteById(id);
    }

    @Override
    public void update(E entity) throws BusinessException {
        getEntityDao().update(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isUnique(E entity, String uniquePropertyNames) throws BusinessException {
        return getEntityDao().isUnique(entity, uniquePropertyNames);
    }




}
