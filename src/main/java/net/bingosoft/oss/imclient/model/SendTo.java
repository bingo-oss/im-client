package net.bingosoft.oss.imclient.model;

import java.util.LinkedList;

/**
 * 服务号消息发送目标对象
 * @author kael.
 */
public class SendTo {
    /**
     * id_type
     * 用户ID类型
     */
    private final int idType;
    /**
     * 接收消息的对象
     */
    private final LinkedList<Receiver> receivers = new LinkedList<Receiver>();

    /**
     * 构造一个使用UserId指定接收用户的对象
     */
    public static SendTo useUserId(){
        return new SendTo(IdTypes.USER_ID);
    }

    /**
     * 构造一个使用LoginId指定接收用户的对象
     * @return
     */
    public static SendTo useLoginId(){
        return new SendTo(IdTypes.LOGIN_ID);
    }
    
    public SendTo(int idType) {
        this.idType = idType;
    }

    public SendTo addReceiver(String id, String name, String msgId){
        return addReceiver(new Receiver(id,name,msgId));
    }
    
    public SendTo addReceiver(Receiver receiver){
        receivers.push(receiver);
        return this;
    }

    public int getIdType() {
        return idType;
    }

    public LinkedList<Receiver> getReceivers() {
        return receivers;
    }

    /**
     * 消息接收者。
     */
    public class Receiver{
        /**
         * 接收消息的用户标识，可能是user_id也可能是login_id，由{@link SendTo#idType}决定
         */
        private String id;
        /**
         * 消息id列表
         */
        private String name;
        /**
         * 用户名列表
         */
        private String msgId;

        public Receiver(String id, String name, String msgId) {
            if(null == id || null == name || null == msgId){
                throw new NullPointerException("argument can not be null.");
            }
            this.id = id;
            this.name = name;
            this.msgId = msgId;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getMsgId() {
            return msgId;
        }

        public void setMsgId(String msgId) {
            this.msgId = msgId;
        }
    }
}
