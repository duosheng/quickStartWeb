package com.soouya.qs.cmc.service;

import com.soouya.qs.cmc.dao.BizSystemDao;
import com.soouya.qs.domain.BizSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


@Service
public class BizSystemService{

    @Resource
    @Autowired
    private BizSystemDao bizSystemDao;

    public int insert(BizSystem pojo){
        return bizSystemDao.insert(pojo);
    }

    public int insertSelective(BizSystem pojo){
        return bizSystemDao.insertSelective(pojo);
    }

    public int insertList(List<BizSystem> pojos){
        return bizSystemDao.insertList(pojos);
    }

    public int update(BizSystem pojo){
        return bizSystemDao.update(pojo);
    }
}
