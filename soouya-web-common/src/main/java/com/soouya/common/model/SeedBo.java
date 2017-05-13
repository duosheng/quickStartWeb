package com.soouya.common.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SeedBo implements java.io.Serializable {
    private static final long serialVersionUID = 5454155825314635342L;
    private String id;
    private String deptId;//部门id（多个部门，则对应主部门）
    private String deptName;//部门名称(冗余)
    private String userName;//用户姓名
    private String nickName;//用户别名
    private String realName;//真实姓名
    private String pwd;//用户密码
    private Integer isLocked;//是否锁定（0-否，1-是）
    private Integer isExpired;//是否过期(0-否，1-是)
    private Integer status;//是否启用（状态，1：有效，-1：无效/删除）
    private Integer gender;//性别（0-未知，1-男，2-女）
    private Integer userType;//用户类型(1-系统管理员，2-主管，3-组长，4-组员，5-普通用户)
    private String userNo;//用户编号
    private java.util.Date createTime;//创建时间
    private java.util.Date editTime;//编辑时间
    private String email;//邮箱
    private String mobile;//手机号码
    private String tel;//办公电话(多个用英文逗号隔开)
    private String addr;//家庭住址
    private String weixin;//微信
    private String weibo;//微博短链接
    private String qq;//QQ
    private String fax;//传真
    private String comments;//备注
    private String serialName;//跟单员编号
    private String xsSerialName;//销售员编号
    private SeedExpandBo seedExpand;// 扩展信息

    /**
     * 微信的openid
     */
    private String openId;
}

