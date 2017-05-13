package com.soouya.qs.cmc;

import com.soouya.common.model.RoleBo;
import com.soouya.springmvc.IRoleListGet;

import java.util.List;

/**
 * Created by xuyuli on 17-5-11.
 */
public class RoleListGet implements IRoleListGet{
    @Override
    public List<RoleBo> get() {
        return null;
    }

    @Override
    public boolean isAdmin() {
        return false;
    }
}
