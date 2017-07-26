package net.bingosoft.oss.imclient.model.msg;

import net.bingosoft.oss.imclient.IMUtil;

/**
 * 
 * 消息内容抽象类，提供内容加密接口
 * @author kael.
 */
public abstract class Content {
    
    public abstract String toContentString();
    
    public String encryptContent(){
        return IMUtil.encryptContent(toContentString());
    }
    
    public static final Content EMPTY = new Content() {
        @Override
        public String toContentString() {
            return "";
        }
    };
    
}
