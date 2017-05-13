package com.soouya.common.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public final class UserRoleInfo implements java.io.Serializable {

    private static final long serialVersionUID = 1L;
    /** 登录用户信息 */
    private SeedBo user;

    // 用户的角色列表
    private List<RoleBo> roleList;
}