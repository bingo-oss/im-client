package net.bingosoft.oss.imclient;

/**
 * 消息服务客户端配置对象
 * @author kael.
 */
public class IMConfig {
    
    protected String imUrl;
    protected String userUrl;
    protected String pollUrl;
    protected String snoUrl;
    
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
        
        this.userUrl = url + "/private/send";
        this.pollUrl = url + "/private/poll";
        this.snoUrl = url + "/svrnum/send";
    }

    public String getUserUrl() {
        return userUrl;
    }

    public void setUserUrl(String userUrl) {
        this.userUrl = userUrl;
    }

    public String getSnoUrl() {
        return snoUrl;
    }

    public void setSnoUrl(String snoUrl) {
        this.snoUrl = snoUrl;
    }

    public String getPollUrl() {
        return pollUrl;
    }

    public void setPollUrl(String pollUrl) {
        this.pollUrl = pollUrl;
    }
}
