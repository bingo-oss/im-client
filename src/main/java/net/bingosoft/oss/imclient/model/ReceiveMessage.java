package net.bingosoft.oss.imclient.model;

/**
 * @author kael.
 */
public class ReceiveMessage extends Message {
    /**
     * send_time
     * 消息发送时间，只有接收消息才需要此字段，发送不需要。
     */
    protected long sendTime;
    /**
     * is_read
     * 消息是否阅读,1是,0否，默认0只有接收消息才需要此字段，发送不需要
     */
    protected int isRead;
    
    public long getSendTime() {
        return sendTime;
    }

    public void setSendTime(long sendTime) {
        this.sendTime = sendTime;
    }

    public int getIsRead() {
        return isRead;
    }

    public void setIsRead(int isRead) {
        this.isRead = isRead;
    }
}
