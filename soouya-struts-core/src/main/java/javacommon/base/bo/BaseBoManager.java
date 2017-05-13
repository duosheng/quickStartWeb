package javacommon.base.bo;

import com.soouya.common.exception.BusinessException;
import com.soouya.common.model.Page;
import com.soouya.common.model.PageRequest;
import javacommon.base.IEntityDao;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author badqiu
 */
@Transactional
public abstract class BaseBoManager<B, PK extends Serializable,P> implements IBaseBoManager<B, PK,P> {

    protected Log log = LogFactory.getLog(getClass());

    protected abstract IEntityDao<P,PK> getEntityDao();

    protected abstract B po2bo(P p);

    protected abstract P bo2po(B b);

    protected List<B> po2bo(List<P> pList){
        List<B> bList = null;
        if (pList != null && !pList.isEmpty()) {
            bList = new ArrayList<B>();
            for (P p : pList) {
                if (p != null) {
                    bList.add(po2bo(p));
                }
            }
        }
        return bList;
    }

    protected List<P> bo2po(List<B> bList){
        List<P> pList = null;
        if (bList != null && !bList.isEmpty()) {
            pList = new ArrayList<P>();
            for (B b : bList) {
                if (b != null) {
                    pList.add(bo2po(b));
                }
            }
        }
        return pList;
    }

    @Transactional(readOnly = true)
    protected Page<B> findPage(PageRequest pageRequest){
        return findPage("findPage",pageRequest);
    }



    protected Page<B> findPage(String statementName, PageRequest pageRequest) {
        Page<B> boPage = new Page<>();
        List<B> boList = new ArrayList<>();
        Page<P> page = getEntityDao().findPage("."+statementName, pageRequest);
        boPage.setTotalCount(page.getTotalCount());
        boPage.setHasNextPage(page.isHasNextPage());
        boPage.setPageNumber(page.getPageNumber());
        boPage.setPageSize(page.getPageSize());
        if (page!=null && page.getResult()!=null && page.getResult().size()>0 ) {
            for (P p : page.getResult()) {
                B b = po2bo(p);
                boList.add(b);
            }
        }
        boPage.setResult(boList);
        return boPage;
    }

    protected List<B> findList(PageRequest pageRequest) {
        List<B> boList = new ArrayList<>();
        List<P> list = getEntityDao().findList(pageRequest);
        if (list!=null && list.size()>0 ) {
            for (P p : list) {
                B b = po2bo(p);
                boList.add(b);
            }
        }
        return boList;
    }

    @Transactional(readOnly = true)
    @Override
    public  B getById(PK id) throws BusinessException {
        P p = getEntityDao().getById(id);
        if (p!=null) {
            return po2bo(p);
        }else {
            return null;
        }
    }



}
