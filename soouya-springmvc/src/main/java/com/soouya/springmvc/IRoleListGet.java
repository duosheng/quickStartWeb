package com.soouya.springmvc;

import com.soouya.common.model.RoleBo;

import java.util.List;

/**
 * Created by xuyuli on 17-4-1.
 */
public interface IRoleListGet {


    List<RoleBo> get();

    boolean isAdmin();

}
