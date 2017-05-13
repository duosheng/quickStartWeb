package javacommon.base;


import com.soouya.common.model.PageRequest;

public class BaseQuery extends PageRequest implements java.io.Serializable {
    private static final long serialVersionUID = -360860474471966681L;
    public static final int DEFAULT_PAGE_SIZE = 10;

    public static final int MAX_PAGE_SIZE = 3000;// 最大3000条

    static {
        System.out.println("BaseQuery.DEFAULT_PAGE_SIZE=" + DEFAULT_PAGE_SIZE);
    }

    public BaseQuery() {
        setPageSize(DEFAULT_PAGE_SIZE);
    }

}
