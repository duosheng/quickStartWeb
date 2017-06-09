package javacommon.base;

/**
 * Created by xuyuli on 2016/10/30.
 */
public class BaseVo {

    @JsonDoc(def = "1")
    private String success = "1";
    @JsonDoc(def = "操作成功")
    private String msg = "操作成功";

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
