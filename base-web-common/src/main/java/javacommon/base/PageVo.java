package javacommon.base;


/**
 * Created by xuyuli on 2016/10/30.
 */
public class PageVo<T> extends BaseVo{

    @JsonDoc(description = "分页数据", def = "")
    private SimplePage<T> page;

    public SimplePage<T> getPage() {
        return page;
    }

    public void setPage(SimplePage<T> page) {
        this.page = page;
    }
}
