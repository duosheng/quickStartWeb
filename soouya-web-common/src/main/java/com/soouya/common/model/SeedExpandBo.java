package com.soouya.common.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SeedExpandBo implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String icon;//头像
    private String position;//职位
    private String companyName;//公司名称
    private String companyProvince;//公司所在省
    private String companyCity;//公司所在市
    private String companyArea;//公司所在区
    private String companyAddress;//公司地址
}
