package javacommon.base;

import org.springframework.dao.DataAccessException;

import java.io.Serializable;
import java.util.List;

/**
 * @author badqiu
 */
public interface EntityDao<E, PK extends Serializable> {

    public E getById(PK id) throws DataAccessException;

    public void deleteById(PK id) throws DataAccessException;

    /** 插入数据 */
    public void save(E entity) throws DataAccessException;

    /** 更新数据 */
    public void update(E entity) throws DataAccessException;

    /** 根据id检查是否插入或是更新数据 */
    public void saveOrUpdate(E entity) throws DataAccessException;

    public boolean isUnique(E entity, String uniquePropertyNames) throws DataAccessException;

    /** 用于hibernate.flush() 有些dao实现不需要实现此类 */
    public void flush() throws DataAccessException;

    public Page findPage(PageRequest pageRequest);

    public Page findPage(String statementName, PageRequest pageRequest);

    public List<E> findList(PageRequest pageRequest);

    public List<E> findAll() throws DataAccessException;

    public void batchDelete(List<E> voList);

    public void batchUpdate(List<E> voList);

    public void batchCreate(List<E> voList);

}
