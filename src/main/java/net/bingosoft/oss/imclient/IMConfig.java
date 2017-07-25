package net.bingosoft.oss.imclient;

/**
 * 消息服务客户端配置对象
 * @author kael.
 */
public class IMConfig {
    
    protected String embUrl;
    protected String sendUrl;
    
    public IMConfig(String embUrl) {
        this.embUrl = embUrl;
        this.autoConfigure(embUrl);
    }

    public String getEmbUrl() {
        return embUrl;
    }

    public void setEmbUrl(String embUrl) {
        this.embUrl = embUrl;
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
