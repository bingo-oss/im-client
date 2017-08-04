package net.bingosoft.oss.imclient;

import com.alibaba.fastjson.JSON;
import net.bingosoft.oss.imclient.exception.InvalidCodeException;
import net.bingosoft.oss.imclient.exception.SendMessageFailException;
import net.bingosoft.oss.imclient.internal.HttpClient;
import net.bingosoft.oss.imclient.model.AccessToken;
import net.bingosoft.oss.imclient.model.IdTypes;
import net.bingosoft.oss.imclient.model.ObjectType;
import net.bingosoft.oss.imclient.model.Receipt;
import net.bingosoft.oss.imclient.model.SendMessage;
import net.bingosoft.oss.imclient.model.SendTo;
import net.bingosoft.oss.imclient.spi.AccessTokenProvider;
import net.bingosoft.oss.imclient.spi.ContentDecoder;
import net.bingosoft.oss.imclient.spi.MessageFetcher;
import net.bingosoft.oss.imclient.spi.PollCallback;
import net.bingosoft.oss.imclient.spi.impl.ImageContentDecoder;
import net.bingosoft.oss.imclient.spi.impl.MultithreadingFetcher;
import net.bingosoft.oss.imclient.spi.impl.TextContentDecoder;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 消息服务客户端
 * @author kael.
 */
public class IMClient {
    
    protected IMConfig config;
    protected AccessTokenProvider tp;
    protected AccessToken at;
    protected MessageFetcher fetcher;
    @SuppressWarnings("rawtypes")
	protected final Set<ContentDecoder> decoders = new HashSet<ContentDecoder>();
    
    /**
     * 创建一个{@link IMClient}对象，并自动获取一个代表应用身份的at
     */
    public IMClient(IMConfig config,AccessTokenProvider tp) {
        this(config,tp,null);
    }

    /**
     * 创建一个{@link IMClient}对象，并使用授权码<code>code</code>获取一个at
     */
    public IMClient(IMConfig config,AccessTokenProvider tp,AccessToken accessToken) throws InvalidCodeException {
        this.config = config;
        this.tp = tp;
        this.at = accessToken;
        registerDefaultContentDecoder();
    }

    /**
     * 注册一个新的消息解码器，可以用来覆盖默认的消息解码器，也可以扩展新的消息解码器
     */
    @SuppressWarnings("rawtypes")
	public void registerContentDecoder(ContentDecoder decoder){
        decoders.add(decoder);
    }
    
    /**
     * 获取消息接口，非阻塞接口
     * @see MessageFetcher
     * @throws IllegalStateException 如果已经在获取消息过程中
     */
    public synchronized void poll(final PollCallback callback) throws IllegalStateException{
        fetcher().fetchMessage(callback);
    }
    
    /**
     * 个人发送消息 
     * @throws SendMessageFailException 发送消息失败
     */
    public Receipt send(SendMessage message) throws SendMessageFailException{
        if(message.getFromType() == ObjectType.USER){
            return userSend(message);
        }else {
            throw new UnsupportedOperationException("not supported for message from type:" + message.getFromType());
        }
    }
    
    /**
     * 服务号发消息
     */
    public Receipt snoSend(SendMessage message, SendTo sendTo) throws SendMessageFailException{
        Map<String, String> headers = createHeaders();
        Map<String, String> params = new HashMap<String, String>();
        
        StringBuilder ids = new StringBuilder();
        StringBuilder names = new StringBuilder();
        StringBuilder msgIds = new StringBuilder();

        for(SendTo.Receiver receiver : sendTo.getReceivers()){
            ids.append(receiver.getId()+",");
            names.append(receiver.getName()+",");
            msgIds.append(receiver.getMsgId()+",");
        }
        int length = ids.length();
        if(length > 0){
            ids.deleteCharAt(ids.length()-1);
            names.deleteCharAt(names.length()-1);
            msgIds.deleteCharAt(msgIds.length()-1);
        }
        if(sendTo.getIdType() == IdTypes.USER_ID){
            params.put("userIds",ids.toString());
        }else if(sendTo.getIdType() == IdTypes.LOGIN_ID){
            params.put("loginIds",ids.toString());
        }else {
            throw new SendMessageFailException("Not support id_type:"+sendTo.getIdType());
        }
        params.put("id_type",sendTo.getIdType()+"");
        params.put("userNames",names.toString());
        params.put("msgIds",msgIds.toString());
        params.put("message",JSON.toJSONString(IMUtil.toMessageMap(message,false)));
        String json;
        try {
            json = HttpClient.post(config.getSnoUrl(),params,headers);
        } catch (HttpClient.HttpRequestException e) {
            throw new SendMessageFailException("fail when send message to ["+config.getSnoUrl()+"]",e);
        }
        Receipt receipt = JSON.parseObject(json,Receipt.class);
        if(receipt.isSuccess()){
            return receipt;
        }else {
            throw new SendMessageFailException("fail when send message:"+receipt.getErr());
        }
    }
    
    /**
     * 服务号发消息给粉丝
     */
	public Receipt snoSend2fans(SendMessage message) throws SendMessageFailException {
		Map<String, String> headers = createHeaders();
		Map<String, String> params = new HashMap<String, String>();

		params.put("message", JSON.toJSONString(IMUtil.toMessageMap(message, false)));
		String json;
		try {
			json = HttpClient.post(config.getSno2fansUrl(), params, headers);
		} catch (HttpClient.HttpRequestException e) {
			throw new SendMessageFailException("fail when send message to [" + config.getSnoUrl() + "]", e);
		}
		Receipt receipt = JSON.parseObject(json, Receipt.class);
		if (receipt.isSuccess()) {
			return receipt;
		} else {
			throw new SendMessageFailException("fail when send message:" + receipt.getErr());
		}
	}
    
    /**========================= 内部方法 ==============================**/
    /**
     * 个人身份发送消息
     */
    protected Receipt userSend(SendMessage message){
        return send(message,config.getUserUrl());
    }

    protected Receipt send(SendMessage message, String url){
        Map<String, String> headers = createHeaders();
        try {
            String json = HttpClient.postJsonBody(url,JSON.toJSONString(IMUtil.toMessageMap(message)),headers);
            Receipt receipt = JSON.parseObject(json,Receipt.class);
            if(receipt.isSuccess()){
                return receipt;
            }else {
                throw new SendMessageFailException("fail when send message:"+receipt.getErr());
            }
        } catch (HttpClient.HttpRequestException e) {
            throw new SendMessageFailException("fail when send message to ["+url+"]",e);
        }
    }
    
    public Map<String, String> createHeaders(){
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

    protected MessageFetcher fetcher(){
        if(null == fetcher){
            fetcher = new MultithreadingFetcher(this);
        }
        return fetcher;
    }

    /**
     * 注册默认的解码器
     */
    protected void registerDefaultContentDecoder(){
        registerContentDecoder(new TextContentDecoder());
        registerContentDecoder(new ImageContentDecoder());
    }
    
    public IMConfig getConfig() {
        return config;
    }

    public void setConfig(IMConfig config) {
        this.config = config;
    }

    public MessageFetcher getFetcher() {
        return fetcher;
    }

    public void setFetcher(MessageFetcher fetcher) {
        this.fetcher = fetcher;
    }

    @SuppressWarnings("rawtypes")
	public Set<ContentDecoder> getDecoders() {
        return decoders;
    }
}