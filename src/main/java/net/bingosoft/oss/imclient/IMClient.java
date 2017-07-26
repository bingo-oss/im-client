package net.bingosoft.oss.imclient;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import net.bingosoft.oss.imclient.exception.HttpRequestException;
import net.bingosoft.oss.imclient.exception.InvalidCodeException;
import net.bingosoft.oss.imclient.exception.SendMessageFailException;
import net.bingosoft.oss.imclient.internal.HttpClient;
import net.bingosoft.oss.imclient.model.AccessToken;
import net.bingosoft.oss.imclient.model.ObjectType;
import net.bingosoft.oss.imclient.model.ReceiveMessage;
import net.bingosoft.oss.imclient.model.SendMessage;
import net.bingosoft.oss.imclient.spi.AccessTokenProvider;
import net.bingosoft.oss.imclient.model.Message;
import net.bingosoft.oss.imclient.model.Receipt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 消息服务客户端
 * @author kael.
 */
public class IMClient {
    
    protected IMConfig config;
    protected AccessTokenProvider tp;
    protected AccessToken at;

    /**
     * 创建一个{@link IMClient}对象，并自动获取一个代表应用身份的at
     */
    public IMClient(IMConfig config,AccessTokenProvider tp) {
        this.config = config;
        this.tp = tp;
    }

    /**
     * 创建一个{@link IMClient}对象，并使用授权码<code>code</code>获取一个at
     */
    public IMClient(IMConfig config,AccessTokenProvider tp,AccessToken accessToken) throws InvalidCodeException {
        this.config = config;
        this.tp = tp;
        this.at = accessToken;
    }

    public List<ReceiveMessage> poll(){
        Map<String, String> headers = createHeaders();
        String json = HttpClient.get(config.getPollUrl(),null,headers);
        List<ReceiveMessage> rms = new ArrayList<ReceiveMessage>();
        JSONArray jsonArray = JSON.parseArray(json);
        for(int i=0; i<jsonArray.size();i++){
            JSONObject object = jsonArray.getJSONObject(i);
            ReceiveMessage msg = IMUtil.toMessage(object);
            rms.add(msg);
        }
        return rms;
    }
    
    /**
     * 发送消息 
     * @throws SendMessageFailException 发送消息失败
     */
    public Receipt send(SendMessage message) throws SendMessageFailException{
        if(message.getFromType() == ObjectType.USER){
            return userSend(message);
        }else if(message.getFromType() == ObjectType.SNO){
            return snoSend(message);
        }else {
            throw new UnsupportedOperationException("not supported for message from type:" + message.getFromType());
        }
    }

    /**
     * 个人身份发送消息
     */
    public Receipt userSend(SendMessage message){
        return send(message,config.getUserUrl());
    }

    /**
     * 服务号发消息
     */
    public Receipt snoSend(SendMessage message){
        return send(message,config.getSnoUrl());
    }
    
    protected Receipt send(SendMessage message, String url){
        Map<String, String> headers = createHeaders();
        try {
            String json = HttpClient.postJsonBody(url,IMUtil.toMessageMap(message),headers);
            Receipt receipt = JSON.parseObject(json,Receipt.class);
            if(receipt.isSuccess()){
                return receipt;
            }else {
                throw new SendMessageFailException("fail when send message:"+receipt.getErr());
            }
        } catch (HttpRequestException e) {
            throw new SendMessageFailException("fail when send message to ["+url+"]",e);
        }
    }
    
    /**========================= 内部方法 ==============================**/
    protected Map<String,String> createParams(Message message) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("message", JSON.toJSONString(message));
        return params;
    }

    protected Map<String, String> createHeaders(){
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Authorization", "Bearer " + at().getAccessToken());
        return headers;
    }
    
    protected AccessToken at(){
        if(null == at){
            at = tp.obtainAccessTokenByClientCredentials();
        }
        if(at.isExpired()){
            at = tp.refreshAccessToken(at);
        }
        return at;
    }
}
