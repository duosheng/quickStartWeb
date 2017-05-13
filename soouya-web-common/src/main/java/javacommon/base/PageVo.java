package javacommon.base;


import javacommon.util.JsonDoc;

/**
 * Created by xuyuli on 2016/10/30.
 */
public class PageVo<T> extends BaseVo{

    @JsonDoc(description = "分页数据", def = "")
    private Page<T> page;

    public Page<T> getPage() {
        return page;
    }

    public void setPage(Page<T> page) {
        this.page = page;
    }
}
