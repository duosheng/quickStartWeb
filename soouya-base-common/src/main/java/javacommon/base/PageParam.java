package javacommon.base;


public class PageParam implements java.io.Serializable {

    public static final int DEFAULT_PAGE_SIZE = 10;

    public static final int MAX_PAGE_SIZE = 3000;// 最大3000条

    /**
     * 页号码,页码从1开始
     */
    @JsonDoc(description = "页号码", def = "1")
    private int pageNumber;
    /**
     * 分页大小
     */
    @JsonDoc(description = "分页大小", def = "10")
    private int pageSize;
    /**
     * 排序的多个列,如: username desc
     */
//    @JsonDoc(description = "排序的多个列", def = "username desc")
//    private String sortColumns;

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

}
