package tests;

import com.alibaba.fastjson.JSON;
import net.bingosoft.oss.imclient.IMClient;
import net.bingosoft.oss.imclient.IMConfig;
import net.bingosoft.oss.imclient.IMUtil;
import net.bingosoft.oss.imclient.model.AccessToken;
import net.bingosoft.oss.imclient.model.MsgType;
import net.bingosoft.oss.imclient.model.ObjectType;
import net.bingosoft.oss.imclient.model.ReceiveMessage;
import net.bingosoft.oss.imclient.model.SendMessage;
import net.bingosoft.oss.imclient.spi.AccessTokenProvider;
import net.bingosoft.oss.imclient.utils.MessageBuilder;
import org.junit.After;
import org.junit.Before;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.mock.action.ExpectationCallback;
import org.mockserver.model.Body;
import org.mockserver.model.Header;
import org.mockserver.model.HttpClassCallback;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import org.mockserver.model.JsonBody;
import org.mockserver.model.NottableString;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

/**
 * @author kael.
 */
public abstract class IMTestBase {
    
    protected static final String senderId = "senderId";
    protected static final String senderName = "senderName";
    protected static final String receiverId = "receiverId";
    protected static final String receiverName = "receiverName";
    
    protected static final String authorization = "Authorization";
    protected static final String bearer = "Bearer";
    protected static final String at1 = "at1";
    protected static final String rt1 = "rt1";
    protected static final String at2 = "at2";
    protected static final String rt2 = "rt2";
    protected static final long   exp = System.currentTimeMillis()+1000*60*60;
    
    protected static final int imPort = 7080;
    protected static final String imUrl = "http://localhost:"+imPort;
    protected static final ClientAndServer imServer = startClientAndServer(imPort);
    // protected static final ClientAndServer uaServer = startClientAndServer(7090);
    // protected static final ClientAndProxy proxy = startClientAndProxy(1090);
    protected final Header header1;
    protected final SendMessage errorMessage;
    protected final SendMessage textMessage;
    protected final AccessToken invalidToken;
    protected final AccessToken accessToken1;
    protected final AccessToken accessToken2;
    protected final IMConfig config;
    protected final AccessTokenProvider tp;
    protected final IMClient client;
    
    public IMTestBase() {
        header1 = new Header(authorization, bearer+" "+at1);
        invalidToken = createAccessToken("at3",rt1,exp,bearer);
        accessToken1 = createAccessToken(at1,rt1,exp,bearer);
        accessToken2 = createAccessToken(at2,rt2,exp,bearer);
        
        textMessage = createSendMessage(senderId,senderName,receiverId,receiverName);
        errorMessage = createSendMessage("error","error","error","error");
        config = new IMConfig(imUrl);
        tp = new AccessTokenProvider() {
            @Override
            public AccessToken obtainAccessTokenByClientCredentials() {
                return accessToken1;
            }

            @Override
            public AccessToken refreshAccessToken(AccessToken accessToken) {
                return accessToken2;
            }
        };
        client = new IMClient(config,tp);
    }
    @Before
    public void allBefore(){
        Body<ReceiveMessage> body = JsonBody.json(textMessage);
        HttpRequest request = request().withMethod("POST")
                //.withBody(body)
                .withPath("/private/send");
        imServer.when(request).callback(HttpClassCallback.callback(UserSendCallback.class.getName()));
        
        before();
    }
    @After
    public void allAfter(){
        after();
    }
    protected void before(){}
    protected void after(){}
    protected SendMessage createSendMessage(String senderId, String senderName, String receiverId, String receiverName){
        return MessageBuilder.userMessage()
                .setFromId(senderId)
                .setFromCompany("品高")
                .setFromName(senderName)

                .setToId(receiverId)
                .setToName(receiverName)
                .setToCompany("品高")
                .setToType(ObjectType.USER)

                .setMsgType(MsgType.TEXT)
                .setMsgId(UUID.randomUUID().toString())
                .setTaskId(UUID.randomUUID().toString())

                .setIsCountUnread(true)
                .setIsDeleteAfterRead(false)
                .setRecReceipt(false)
                .setIsNeedReadReceipt(false)

                .setTextContent("hello")

                .build();
    }
    protected AccessToken createAccessToken(String at, String rt, long exp, String type){
        AccessToken accessToken = new AccessToken();
        accessToken.setAccessToken(at);
        accessToken.setRefreshToken(rt);
        accessToken.setExpires(exp);
        accessToken.setTokenType(type);
        return accessToken;
    }
    
    /**
     * 检查at是否无效
     * @return 无效返回true,有效返回false
     */
    protected static boolean invalidAt(HttpRequest request){
        List<Header> headers = request.getHeaders();
        for(Header h : headers){
            if(authorization.equalsIgnoreCase(h.getName().getValue())){
                for(NottableString ns : h.getValues()){
                    String value = ns.getValue();
                    if(value.equalsIgnoreCase(bearer+" "+at1)
                        || value.equalsIgnoreCase(bearer+" "+at2)){
                        return false;
                    }
                }
            }
        }
        return true;
    }
    
    protected static boolean invalidMessage(HttpRequest request){
        Object value = request.getBody().getValue();
        Map<String, Object> map = JSON.parseObject(value.toString(),Map.class);
        if("error".equalsIgnoreCase((String) map.get("from_id"))) {
            return true;
        }
        return false;
    }
    public static class UserSendCallback implements ExpectationCallback{
        @Override
        public HttpResponse handle(HttpRequest httpRequest) {
            if(IMTestBase.invalidAt(httpRequest)){
                return response().withStatusCode(401).withBody(JsonBody.json(fail));
            }
            if(IMTestBase.invalidMessage(httpRequest)){
                return response().withStatusCode(401).withBody(JsonBody.json(fail));
            }
            return response().withStatusCode(200).withBody(JsonBody.json(success));
        }
        
        protected final Map<String, Object> success;
        protected final Map<String, Object> fail;
        public UserSendCallback() {
            success = createReceipt(true,"");
            fail = createReceipt(false,"bad request");
        }
        protected Map<String, Object> createReceipt(boolean success, String err){
            Map<String, Object> receipt = new HashMap<String, Object>();
            receipt.put("success",success);
            receipt.put("err",err);
            return receipt;
        }
    }
}
