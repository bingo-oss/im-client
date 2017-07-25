package net.bingosoft.oss.imclient;

import net.bingosoft.oss.imclient.internal.Base64;
import net.bingosoft.oss.imclient.model.SendMessage;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * 消息服务工具类
 * @author kael.
 */
public class IMUtil {
    
    public static Map<String, Object> toMessageMap(SendMessage message){
        Map<String, Object> map = new HashMap<String, Object>();
        
        map.put("task_id",message.getTaskId());
        map.put("msg_id",message.getMsgId());
        map.put("msg_type",message.getMsgType());
        
        map.put("content",encryptContent(message.getContent()));
        
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
        throw new UnsupportedOperationException();
    }
}
