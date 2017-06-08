package javacommon.base;


import java.util.List;

/**
 * 分页信息
 * 第一页从1开始
 * Created by xuyuli on 2016/4/27.
 */
public class SimplePage<T>
{
    @JsonDoc(description = "结果", def = "")
    private List<T> result;
    @JsonDoc(description = "每页数量", def = "")
    private Integer pageSize;
    @JsonDoc(description = "页码", def = "")
    private Integer pageNumber;
    @JsonDoc(description = "总条数", def = "")
    private Integer totalCount;

    public List<T> getResult() {
        return result;
    }

    public void setResult(List<T> result) {
        this.result = result;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }
}

