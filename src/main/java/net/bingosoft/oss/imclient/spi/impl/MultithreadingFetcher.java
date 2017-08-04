package net.bingosoft.oss.imclient.spi.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import net.bingosoft.oss.imclient.IMClient;
import net.bingosoft.oss.imclient.IMUtil;
import net.bingosoft.oss.imclient.internal.HttpClient;
import net.bingosoft.oss.imclient.model.ReceiveMessage;
import net.bingosoft.oss.imclient.spi.ContentDecoder;
import net.bingosoft.oss.imclient.spi.MessageFetcher;
import net.bingosoft.oss.imclient.spi.PollCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 多线程消息获取器，使用这个获取器会启动一个新的守护线程，并通过这个线程不断获取消息，每次获取消息之后会回调接口
 * @author kael.
 */
public class MultithreadingFetcher implements MessageFetcher {
    
    protected Thread pollMsgThread;
    protected final IMClient client;

    public MultithreadingFetcher(IMClient client) {
        this.client = client;
    }

    /**
     * 这个实现会限制并发，同一个对象只允许开启一个线程
     */
    @Override
    public synchronized void fetchMessage(final PollCallback callback) {
        if(null != pollMsgThread && pollMsgThread.isAlive()){
            throw new IllegalStateException("A poll thread has been started!");
        }
        final Map<String, String> headers = client.createHeaders();
        pollMsgThread = new Thread(new Runnable() {
            @SuppressWarnings("rawtypes")
			@Override
            public void run() {
            do {
                try {
                    String json = HttpClient.get(client.getConfig().getPollUrl(),null,headers);
                    List<ReceiveMessage> rms = new ArrayList<ReceiveMessage>();
                    JSONArray jsonArray = JSON.parseArray(json);
                    for(int i=0; i<jsonArray.size();i++){
                        JSONObject object = jsonArray.getJSONObject(i);
                        ReceiveMessage msg = IMUtil.toMessage(object);
                        for(ContentDecoder decoder:client.getDecoders()){
                            if(decoder.match(msg.getMsgType())){
                                msg.setContent(decoder.decode(msg.getContent().toContentString()));
                                break;
                            }
                        }
                        rms.add(msg);
                    }
                    if(!callback.apply(rms)){
                        break;
                    }
                } catch (HttpClient.HttpRequestException e){
                    if(e.getStatus() == 417){
                        if(!callback.onTimeout()){
                            break;
                        }
                    }
                } catch (Throwable e){
                    if(!callback.onError(e)){
                        break;
                    }
                }
            }while (true);
            }
        });
        pollMsgThread.setDaemon(true);
        pollMsgThread.start();
    }
}
