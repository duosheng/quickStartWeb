package javacommon.base.bo;

import javacommon.base.exception.BusinessException;

import java.io.Serializable;

/**
 *
 * @author xuyuli
 *
 * @param <E>
 * @param <PK>
 */
public interface IBaseBoManager<E, PK extends Serializable,P> {

//    /**
//     * 分页查询
//     * @param pageRequest
//     * @return
//     */
//    public Page<E> findPage(PageRequest pageRequest);
//
//    /**
//     * 根据statementName分页查询
//     * @param statementName
//     * @param pageRequest
//     * @return
//     */
//    public Page<E> findPage(String statementName, PageRequest pageRequest);

    /**
     * 根据id查询实体
     *
     * @param id
     * @return
     * @throws BusinessException
     */
    E getById(PK id) throws BusinessException;

}
