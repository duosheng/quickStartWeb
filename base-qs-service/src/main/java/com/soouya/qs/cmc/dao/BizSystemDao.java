package com.base.qs.cmc.dao;

import com.base.qs.domain.BizSystem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BizSystemDao {

    int insert(@Param("pojo") BizSystem pojo);

    int insertSelective(@Param("pojo") BizSystem pojo);

    int insertList(@Param("pojos") List<BizSystem> pojo);

    int update(@Param("pojo") BizSystem pojo);

}
