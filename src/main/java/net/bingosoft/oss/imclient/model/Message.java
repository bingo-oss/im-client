package net.bingosoft.oss.imclient.model;

import net.bingosoft.oss.imclient.model.msg.Content;

/**
 * 
 * 发送的消息
 * @author kael.
 */
public abstract class Message {
    /**
     * task_id
     * 每次请求的唯一标识，由客户端产生
     */
    protected String taskId;
    /**
     * msg_id
     * 消息唯一标识，由客户端产生
     */
    protected String msgId;
    /**
     * msg_type
     * 消息类别
     * @see MsgType
     */
    protected int msgType;
    /**
     * content
     * 不允许为null，为空时需要设置为{@link Content#EMPTY}
     * 消息内容：发送前/接收后需要加密/解密
     */
    protected Content content;
    /**
     * from_type
     * 发送者类别
     * @see ObjectType
     */
    protected int fromType;
    /**
     * from_id
     * 发送者编号，唯一标识
     */
    protected String fromId;
    /**
     * from_name
     * 发送者名称
     */
    protected String fromName;

    /**
     * from_company
     * 消息发送者企业，企业互连必须字段
     */
    protected String fromCompany;

    /**
     * to_type
     * 接收者类别
     * @see ObjectType
     */
    protected int toType;

    /**
     * to_id
     * 消息接收者标识
     */
    protected String toId;

    /**
     * to_name
     * 消息接收者名称
     */
    protected String toName;

    /**
     * to_company
     * 消息接收者企业，企业互连必须字段
     */
    protected String toCompany;

    /**
     * to_device_types
     * 消息接收者终端类型。
     * @see DeviceType
     */
    protected String toDeviceTypes;



    /**
     * rec_receipt
     * 是否需要发送回执给UAM，默认false。
     */
    protected Boolean recReceipt=false;
    /**
     * is_count_unread
     * 是否计算未读数。默认为true。为true时，会推送apns通知，也会计算未读消息数。为false时，只要终端接收成功后此离线消息即时删除。
     */
    protected Boolean isCountUnread=true;

    /**
     * is_delete_after_read
     * 阅读后将自动删除。默认false。
     */
    protected Boolean isDeleteAfterRead=false;

    /**
     * is_need_read_receipt
     * 是否需要已读回执，默认false。
     */
    protected Boolean isNeedReadReceipt=false;

    /**
     * at_user_ids
     * 艾特用户ID，多个人用逗号分隔；ALL代表全体成员
     */
    protected String atUserIds;

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }

    public int getFromType() {
        return fromType;
    }

    public void setFromType(int fromType) {
        this.fromType = fromType;
    }

    public String getFromId() {
        return fromId;
    }

    public void setFromId(String fromId) {
        this.fromId = fromId;
    }

    public String getFromName() {
        return fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    public String getFromCompany() {
        return fromCompany;
    }

    public void setFromCompany(String fromCompany) {
        this.fromCompany = fromCompany;
    }

    public int getToType() {
        return toType;
    }

    public void setToType(int toType) {
        this.toType = toType;
    }

    public String getToId() {
        return toId;
    }

    public void setToId(String toId) {
        this.toId = toId;
    }

    public String getToName() {
        return toName;
    }

    public void setToName(String toName) {
        this.toName = toName;
    }

    public String getToCompany() {
        return toCompany;
    }

    public void setToCompany(String toCompany) {
        this.toCompany = toCompany;
    }

    public String getToDeviceTypes() {
        return toDeviceTypes;
    }

    public void setToDeviceTypes(String toDeviceTypes) {
        this.toDeviceTypes = toDeviceTypes;
    }

    public boolean isRecReceipt() {
        return recReceipt;
    }

    public void setRecReceipt(boolean recReceipt) {
        this.recReceipt = recReceipt;
    }

    public Boolean getRecReceipt() {
        return recReceipt;
    }

    public void setRecReceipt(Boolean recReceipt) {
        this.recReceipt = recReceipt;
    }

    public Boolean getIsCountUnread() {
        return isCountUnread;
    }

    public void setIsCountUnread(Boolean countUnread) {
        isCountUnread = countUnread;
    }

    public Boolean getIsDeleteAfterRead() {
        return isDeleteAfterRead;
    }

    public void setIsDeleteAfterRead(Boolean deleteAfterRead) {
        isDeleteAfterRead = deleteAfterRead;
    }

    public Boolean getIsNeedReadReceipt() {
        return isNeedReadReceipt;
    }

    public void setIsNeedReadReceipt(Boolean needReadReceipt) {
        isNeedReadReceipt = needReadReceipt;
    }

    public String getAtUserIds() {
        return atUserIds;
    }

    public void setAtUserIds(String atUserIds) {
        this.atUserIds = atUserIds;
    }
    
}
