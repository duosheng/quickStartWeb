package javacommon.base;

/**
 * 所有的自定义输入参数类都必须继承该类，否则不能进入参数认证类
 * @author badqiu
 */
public class BaseEntity implements java.io.Serializable {

    private static final long serialVersionUID = -7200095849148417467L;

    protected static final String DATE_FORMAT = "yyyy-MM-dd";

    protected static final String TIME_FORMAT = "HH:mm:ss";

    protected static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    protected static final String TIMESTAMP_FORMAT = "yyyy-MM-dd HH:mm:ss.S";

}
