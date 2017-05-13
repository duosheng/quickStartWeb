package com.soouya.webservice.exception;

/**
 * Created by xuyuli on 2016/3/24.
 */
public class CXFCode {

    // 成功
    public static final String SUCCESS = "1";

    // 系统错误
    public static final int FATAL_Code = 10001;
    // 系统错误
    public static final String FATAL_MSG = "系统错误";

    // 请求参数无效
    public static final String INVALID_PARAM = "10005";

    // 接口访问权限受限
    public static final String NO_LOGIN = "10009";

    // 缺失必选参数
    public static final String PARAM_LOST = "10010";

    // 用户名或者密码错误
    public static final String PASSWORD_NOT_MATCH = "20004";

    // 商户还未注册
    public static final String SHOP_NOT_REGIST = "21001";

    // 重复申报
    public static final String DUPLICATE_DECLARE = "21002";

    // 添加拜访记录时商户不存在
    public static final String SHOP_NOT_EXIST = "21003";

    // 找不到对应的记录
    public static final String ENTITY_NOT_EXIST = "10030";

    // 没有权限处理该数据
    public static final String ACTION_NOT_ALLOWED = "10040";
}
