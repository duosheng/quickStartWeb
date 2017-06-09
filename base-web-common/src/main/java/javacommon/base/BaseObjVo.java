package javacommon.base;


/**
 * Created by xuyuli on 2016/10/30.
 */
public class BaseObjVo extends BaseVo{

    @JsonDoc(description = "返回的实体",def = "1")
    private Object obj;

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }
}
