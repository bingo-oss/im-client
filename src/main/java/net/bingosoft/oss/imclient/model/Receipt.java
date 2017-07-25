package net.bingosoft.oss.imclient.model;

/**
 * 
 * 消息发送后的回执
 * @author kael.
 */
public class Receipt {
    /**
     * 成功为true,失败为false
     */
    protected boolean success=true;
    /**
     * 成功时为空，失败时为错误信息
     */
    protected String err;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getErr() {
        return err;
    }

    public void setErr(String err) {
        this.err = err;
    }
}
