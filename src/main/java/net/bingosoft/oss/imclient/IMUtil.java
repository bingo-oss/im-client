package net.bingosoft.oss.imclient;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import net.bingosoft.oss.imclient.internal.Base64;
import net.bingosoft.oss.imclient.model.MsgType;
import net.bingosoft.oss.imclient.model.ObjectType;
import net.bingosoft.oss.imclient.model.ReceiveMessage;
import net.bingosoft.oss.imclient.model.SendMessage;
import net.bingosoft.oss.imclient.model.msg.Content;
import net.bingosoft.oss.imclient.model.msg.Text;

/**
 * 
 * 消息服务工具类
 * @author kael.
 */
public class IMUtil {

    public static Map<String, Object> toMessageMap(SendMessage message){
        return toMessageMap(message,true);
    }
    
    public static Map<String, Object> toMessageMap(SendMessage message, boolean encrypt){
        Map<String, Object> map = new HashMap<String, Object>();
        
        map.put("task_id",message.getTaskId());
        map.put("msg_id",message.getMsgId());
        map.put("msg_type",message.getMsgType());
        if(encrypt){
            map.put("content",message.getContent().encryptContent());
        }else {
            map.put("content",message.getContent().toContentString());
        }
        
        map.put("from_type",message.getFromType());
        map.put("from_id",message.getFromId());
        map.put("from_company",message.getFromCompany());
        map.put("from_name",message.getFromName());
        
        map.put("to_id",message.getToId());
        map.put("to_type",message.getToType());
        map.put("to_name",message.getToName());
        map.put("to_company",message.getToCompany());
        
        map.put("rec_receipt",message.getRecReceipt());
        map.put("is_count_unread",message.getIsCountUnread());
        map.put("is_delete_after_read",message.getIsDeleteAfterRead());
        map.put("is_need_read_receipt",message.getIsNeedReadReceipt());
        
        map.put("to_device_types",message.getToDeviceTypes());
        map.put("at_user_ids",message.getAtUserIds());
        
        return map;
    }

    /**
     * 将map转换为{@link ReceiveMessage}，不对{@link Content}进行解密，收到的密文直接当成text
     */
    public static ReceiveMessage toMessage(Map<String, Object> map){
        ReceiveMessage rm = new ReceiveMessage();
        rm.setTaskId((String) map.get("task_id"));
        rm.setMsgId((String)map.get("msg_id"));
        rm.setMsgType(objectToInt(map.get("msg_type"), MsgType.TEXT));
        
        rm.setContent(new Text((String) map.get("content")));
        
        rm.setFromType(objectToInt(map.get("from_type"), ObjectType.USER));
        rm.setFromId((String)map.get("from_id"));
        rm.setFromCompany((String)map.get("from_company"));
        rm.setFromName((String)map.get("from_name"));
        
        rm.setToId((String)map.get("to_id"));
        rm.setToType(objectToInt(map.get("to_type"),ObjectType.USER));
        rm.setToName((String)map.get("to_name"));
        rm.setToCompany((String)map.get("to_company"));
        
        rm.setRecReceipt(objectToBoolean(map.get("rec_receipt"),false));
        rm.setIsCountUnread(objectToBoolean(map.get("is_count_unread"),true));
        rm.setIsDeleteAfterRead(objectToBoolean(map.get("is_delete_after_read"),false));
        rm.setIsNeedReadReceipt(objectToBoolean(map.get("is_need_read_receipt"),false));
        
        rm.setToDeviceTypes((String)map.get("to_device_types"));
        rm.setAtUserIds((String)map.get("at_user_ids"));
        
        rm.setSendTime(objectToLong(map.get("send_time"),System.currentTimeMillis()));
        rm.setIsRead(objectToInt(map.get("is_read"),0));
        return rm;
    }
    
    public static int objectToInt(Object o,int def){
        if(null == o){
            return def;
        }else if(o instanceof Integer){
            return (Integer)o;
        }else {
            return Integer.getInteger(o.toString());
        }
    }

    public static long objectToLong(Object o,long def){
        if(null == o){
            return def;
        }else if(o instanceof Integer){
            return (Long) o;
        }else {
            return Long.parseLong(o.toString());
        }
    }
    
    public static boolean objectToBoolean(Object o,boolean def){
        if(null == o){
            return def;
        }else if(o instanceof Boolean){
            return (Boolean) o;
        }else {
            return Boolean.getBoolean(o.toString());
        }
    }
    
    public static String encryptContent(String content){
         try {
             byte[] bytes = content.getBytes("UTF-8");
             for(int i=0; i < bytes.length; i++){
                 bytes[i] = (byte) ~bytes[i];
             }
             return Base64.encode(bytes);
         } catch (UnsupportedEncodingException e) {
             throw new RuntimeException(e);
         }
    }
    
    public static String decryptContent(String content){

        try {
            if(null == content || content.isEmpty()){
                return "";
            }
            byte[] bytes = Base64.decode(content);
            for(int i=0; i < bytes.length; i++){
                bytes[i] = (byte) ~bytes[i];
            }
            return new String(bytes,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
         
    }
}