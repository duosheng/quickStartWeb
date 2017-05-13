package com.soouya.common.model;


import java.util.ArrayList;
import java.util.List;

/**
 * 分页信息
 * 第一页从1开始
 * Created by xuyuli on 2016/4/27.
 */
public class Page<T>
{

    private List<T> result;

    private Integer pageSize;

    private Integer pageNumber;

    private Integer totalCount;

    private Boolean hasNextPage;  //TODO


    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public Page() {
    }

    public Page(PageRequest p, int totalCount) {
        this(p.getPageNumber(),p.getPageSize(),totalCount);
    }

    public Page(int pageNumber, int pageSize, int totalCount) {
        this(pageNumber, pageSize, totalCount, new ArrayList(0));
    }

    public Page(int pageNumber, int pageSize, int totalCount, List<T> result) {
        if(pageSize <= 0) throw new IllegalArgumentException("[pageSize] must great than zero");
        this.pageSize = pageSize;
        this.pageNumber = computePageNumber(pageNumber, pageSize, totalCount);
        this.totalCount = totalCount;
        setResult(result);
    }

    public void setResult(List<T> elements) {
        if(elements == null) throw new IllegalArgumentException("'result' must be not null");
        this.result = elements;
    }

    public static int computePageNumber(int pageNumber, int pageSize,int totalElements) {
        if(pageNumber <= 1) {
            return 1;
        }
        if (Integer.MAX_VALUE == pageNumber
                || pageNumber > computeLastPageNumber(totalElements,pageSize)) { //last page
            return computeLastPageNumber(totalElements,pageSize);
        }
        return pageNumber;
    }

    public static int computeLastPageNumber(int totalElements,int pageSize) {
        int result = totalElements % pageSize == 0 ?
                totalElements / pageSize
                : totalElements / pageSize + 1;
        if(result <= 1)
            result = 1;
        return result;
    }

    /**
     * 当前页包含的数据
     *
     * @return 当前页数据源
     */
    public List<T> getResult() {
        return result;
    }

    /**
     * 是否是首页（第一页），第一页页码为1
     *
     * @return 首页标识
     */
    public boolean isFirstPage() {
        return getThisPageNumber() == 1;
    }

    /**
     * 是否是最后一页
     *
     * @return 末页标识
     */
    public boolean isLastPage() {
        return getThisPageNumber() >= getLastPageNumber();
    }
    /**
     * 是否有下一页
     *
     * @return 下一页标识
     */

    public Boolean isHasNextPage() {
        return hasNextPage==null?getLastPageNumber() > getThisPageNumber():hasNextPage;
    }

    public void setHasNextPage(Boolean hasNextPage) {
        this.hasNextPage = hasNextPage;
    }

    /**
     * 是否有上一页
     *
     * @return 上一页标识
     */
    public boolean isHasPreviousPage() {
        return getThisPageNumber() > 1;
    }
    /**
     * 获取最后一页页码，也就是总页数
     *
     * @return 最后一页页码
     */
    public Integer getLastPageNumber() {
        return PageUtils.computeLastPageNumber(totalCount, pageSize);
    }
    /**
     * 总的数据条目数量，0表示没有数据
     *
     * @return 总数量
     */
    public Integer getTotalCount() {
        return totalCount;
    }
    /**
     * 获取当前页的首条数据的行编码
     *
     * @return 当前页的首条数据的行编码
     */
    public Integer getThisPageFirstElementNumber() {
        return (getThisPageNumber() - 1) * getPageSize() + 1;
    }
    /**
     * 获取当前页的末条数据的行编码
     *
     * @return 当前页的末条数据的行编码
     */
    public Integer getThisPageLastElementNumber() {
        int fullPage = getThisPageFirstElementNumber() + getPageSize() - 1;
        return getTotalCount() < fullPage ? getTotalCount() : fullPage;
    }
    /**
     * 获取下一页编码
     *
     * @return 下一页编码
     */
    public Integer getNextPageNumber() {
        return getThisPageNumber() + 1;
    }
    /**
     * 获取上一页编码
     *
     * @return 上一页编码
     */
    public Integer getPreviousPageNumber() {
        return getThisPageNumber() - 1;
    }
    /**
     * 每一页显示的条目数
     *
     * @return 每一页显示的条目数
     */
    public Integer getPageSize() {
        return pageSize;
    }
    /**
     * 当前页的页码
     *
     * @return 当前页的页码
     */
    public Integer getThisPageNumber() {
        return pageNumber;
    }

    /**
     * 得到用于多页跳转的页码
     * @return
     */
    public List<Integer> getLinkPageNumbers() {
        return PageUtils.generateLinkPageNumbers(getThisPageNumber(), getLastPageNumber(),10);
    }

    /**
     * 得到数据库的第一条记录号
     * @return
     */
    public Integer getFirstResult() {
        return PageUtils.getFirstResult(pageNumber, pageSize);
    }


}

