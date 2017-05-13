package com.soouya.common.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleBo implements java.io.Serializable {
    private static final long serialVersionUID = 5454155825314635342L;

    private String id;//id
    private String code;// 编码
    private String name;// 名称
    private Integer displayOrder;//显示顺序
    private Integer status;//状态
    private String comments;//备注
}
