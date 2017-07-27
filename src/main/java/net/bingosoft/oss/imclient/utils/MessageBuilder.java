package net.bingosoft.oss.imclient.utils;

import net.bingosoft.oss.imclient.model.ObjectType;
import net.bingosoft.oss.imclient.model.SendMessage;
import net.bingosoft.oss.imclient.model.msg.Content;
import net.bingosoft.oss.imclient.model.msg.FileContent;
import net.bingosoft.oss.imclient.model.msg.Text;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * 
 * 用于构造{@link SendMessage}
 * 
 * @author kael.
 */
public class MessageBuilder {
    protected String taskId;
    protected String msgId;
    protected int msgType;
    
    protected Content content;
    
    protected int fromType;
    protected String fromId;
    protected String fromName;
    protected String fromCompany;
    
    protected int toType;
    protected String toId;
    protected String toName;
    protected String toCompany;
    
    protected boolean recReceipt=false;
    protected boolean isCountUnread=true;
    protected boolean isDeleteAfterRead=false;
    protected boolean isNeedReadReceipt=false;
    
    protected Set<String> toDeviceTypes = new HashSet<String>();
    protected Set<String> atUserIds = new HashSet<String>();

    /**
     * 自定义消息
     */
    public static MessageBuilder custom(){
        return new MessageBuilder();
    }
    
    /**
     * 用户消息
     */
    public static MessageBuilder userMessage(){
        return new MessageBuilder().setFromType(ObjectType.USER);
    }

    /**
     * 服务号消息
     */
    public static MessageBuilder snoMessage(){
        return new MessageBuilder().setFromType(ObjectType.SNO);
    }

    /**
     * 文本消息
     */
    public static MessageBuilder textMessage(String text){
        return new MessageBuilder().setTextContent(text);
    }

    /**
     * 文件类的消息
     */
    public static <T extends FileContent> MessageBuilder fileMessage(T content){
        return new MessageBuilder().setContent(content);
    }
    
    public MessageBuilder setTaskId(String taskId){
        this.taskId = taskId;
        return this;
    }
    
    public MessageBuilder setMsgId(String msgId){
        this.msgId = msgId;
        return this;
    }
    
    public MessageBuilder setMsgType(int msgType){
        this.msgType = msgType;
        return this;
    }
    
    public MessageBuilder setContent(Content content){
        this.content = content;
        return this;
    }
    
    public MessageBuilder setTextContent(String text){
        return setContent(new Text(text));
    }
    
    public <T extends FileContent> MessageBuilder setFileContent(T content){
        return setFileContent(content);
    }
    
    public MessageBuilder setFromType(int fromType){
        this.fromType = fromType;
        return this;
    }
    
    public MessageBuilder setFromId(String fromId){
        this.fromId = fromId;
        return this;
    }
    
    public MessageBuilder setFromName(String fromName){
        this.fromName = fromName;
        return this;
    }
    
    public MessageBuilder setFromCompany(String fromCompany){
        this.fromCompany = fromCompany;
        return this;
    }
    
    public MessageBuilder setToType(int toType){
        this.toType = toType;
        return this;
    }
    
    public MessageBuilder setToId(String toId){
        this.toId = toId;
        return this;
    }

    public MessageBuilder setToName(String toName){
        this.toName = toName;
        return this;
    }
    
    public MessageBuilder setToCompany(String toCompany){
        this.toCompany = toCompany;
        return this;
    }
    
    public MessageBuilder addToDevciceTypes(String toDeviceTypes){
        this.toDeviceTypes.add(toDeviceTypes);
        return this;
    }
    
    public MessageBuilder setRecReceipt(boolean recReceipt){
        this.recReceipt = recReceipt;
        return this;
    }
    
    public MessageBuilder setIsCountUnread(boolean isCountUnread){
        this.isCountUnread = isCountUnread;
        return this;
    }
    
    public MessageBuilder setIsDeleteAfterRead(boolean isDeleteAfterRead){
        this.isDeleteAfterRead = isDeleteAfterRead;
        return this;
    }
    
    public MessageBuilder setIsNeedReadReceipt(boolean isNeedReadReceipt){
        this.isNeedReadReceipt = isNeedReadReceipt;
        return this;
    }
    
    public MessageBuilder addAtUser(String userId){
        atUserIds.add(userId);
        return this;
    }
    public SendMessage build(){
        SendMessage message = new SendMessage();
        message.setTaskId(taskId);
        message.setMsgId(null == msgId? UUID.randomUUID().toString():msgId);
        message.setMsgType(msgType);
        message.setContent(content);
        message.setFromType(fromType);
        message.setFromId(fromId);
        message.setFromName(fromName);
        message.setFromCompany(fromCompany);
        message.setToType(toType);
        message.setToId(toId);
        message.setToName(toName);
        message.setToCompany(toCompany);
        message.setRecReceipt(recReceipt);
        message.setIsCountUnread(isCountUnread);
        message.setIsDeleteAfterRead(isDeleteAfterRead);
        message.setIsNeedReadReceipt(isNeedReadReceipt);
        if(toDeviceTypes.size() > 0){
            StringBuilder dt = new StringBuilder();
            for (String s : toDeviceTypes){
                dt.append(s+",");
            }
            dt.deleteCharAt(dt.length()-1);
            message.setToDeviceTypes(dt.toString());
        }
        if(atUserIds.size() > 0){
            StringBuilder atIds = new StringBuilder();
            for (String s : atUserIds){
                atIds.append(s+",");
            }
            atIds.deleteCharAt(atIds.length()-1);
            message.setAtUserIds(atIds.toString());
        }
        return message;
    }
    
}
