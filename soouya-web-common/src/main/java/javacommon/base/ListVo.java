package javacommon.base;


import javacommon.util.JsonDoc;

import java.util.List;

/**
 * Created by xuyuli on 2016/10/30.
 */
public class ListVo<T> extends BaseVo{

    @JsonDoc(description = "不分页数据", def = "")
    private List<T> list;

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}
