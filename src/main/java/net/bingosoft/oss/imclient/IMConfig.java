package net.bingosoft.oss.imclient;

/**
 * 消息服务客户端配置对象
 * @author kael.
 */
public class IMConfig {
    
    protected String imUrl;
    protected String sendUrl;
    
    public IMConfig(String imUrl) {
        this.imUrl = imUrl;
        this.autoConfigure(imUrl);
    }

    public String getImUrl() {
        return imUrl;
    }

    public void setImUrl(String imUrl) {
        this.imUrl = imUrl;
    }

    public void autoConfigure(String embUrl){
        if(null == embUrl || embUrl.trim().isEmpty()){
            return;
        }
        String url = embUrl.endsWith("/")?embUrl.substring(0,embUrl.length()-1):embUrl;
        
        this.sendUrl = url + "/private/send";
    }

    public String getSendUrl() {
        return sendUrl;
    }

    public void setSendUrl(String sendUrl) {
        this.sendUrl = sendUrl;
    }

}
