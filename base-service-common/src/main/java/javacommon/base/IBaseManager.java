package javacommon.base;


import javacommon.base.exception.BusinessException;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author lixon
 *
 * @param <E>
 * @param <PK>
 */
public interface IBaseManager<E, PK extends Serializable> {

    /**
     * 分页查询
     * @param pageRequest
     * @return
     */
    public Page findPage(PageRequest pageRequest);

    /**
     * 根据statementName分页查询
     * @param statementName
     * @param pageRequest
     * @return
     */
    public Page findPage(String statementName, PageRequest pageRequest);

    /**
     * 不分页按条件查所有
     * @param pageRequest
     * @return
     */
    public List<E> findList(PageRequest pageRequest);

    /**
     * 根据id查询实体
     *
     * @param id
     * @return
     * @throws BusinessException
     */
    E getById(PK id) throws BusinessException;

    /**
     * 返回所有
     *
     * @return
     * @throws BusinessException
     */
    List<E> findAll() throws BusinessException;

    /**
     * 保存或者更新
     *
     * @param entity
     * @throws BusinessException
     */
    void saveOrUpdate(E entity) throws BusinessException;

    /**
     * 新增
     *
     * @param entity
     * @throws BusinessException
     */
    void save(E entity) throws BusinessException;

    /**
     * 根据id删除
     *
     * @param id
     * @throws BusinessException
     */
    void deleteById(PK id) throws BusinessException;

    /**
     * 更新
     *
     * @param entity
     * @throws BusinessException
     */
    void update(E entity) throws BusinessException;

    /**
     * 是否唯一记录
     *
     * @param entity
     * @param uniquePropertyNames
     * @return
     * @throws BusinessException
     */
    boolean isUnique(E entity, String uniquePropertyNames) throws BusinessException;
}
