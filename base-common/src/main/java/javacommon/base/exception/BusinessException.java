package javacommon.base.exception;

/**
 * 业务异常。 业务异常为出现违反业务规则的情景时抛出的异常。 例如用户提交的表单违反唯一性约束，用户访问权限以外的URL等。
 * 当用户调整输入表单信息，或者取得访问权限后则不再出现此类异常。
 *
 * @author xuyuli
 *
 */
public class BusinessException extends RuntimeException {

    private final static long serialVersionUID = 4514817182497108609L;

    public final static String SUCCESSFUL_CODE = "1";

    /**
     * 异常编号。用于快速定位异常发生的位置，缺省值为1（成功）。
     */
    protected String errorCode = SUCCESSFUL_CODE;

    public String getErrorCode() {
        return errorCode;
    }

    public BusinessException(Exception exception) {
        super(exception);
    }

    public BusinessException(String errorCode) {
        this.errorCode = errorCode;
    }

    public BusinessException(String errorCode, String msg) {
        super(msg);
        this.errorCode = errorCode;
    }

    /**
     * 获得异常编号和信息
     *
     * @return 形如：(异常编号)异常信息
     */
    public String getCodeAndMessage() {
        return "(" + errorCode + ") " + getMessage();
    }
}