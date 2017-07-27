package net.bingosoft.oss.imclient.internal;

import com.alibaba.fastjson.JSON;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.JsonBody;
import org.mockserver.model.Parameter;
import org.mockserver.model.ParameterBody;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

/**
 * @author kael.
 */
public class TestHttpClient {
    
    protected static final int port = 7000;
    protected static final String http = "http://localhost:"+port;
    protected static final String https = "https://localhost:"+port;
    protected static ClientAndServer mockServer;
    // Base64.encode("clientId:ClientSecret")
    protected static final String BASIC_HEADER = "Basic Y2xpZW50SWQ6Y2xpZW50U2VjcmV0";
    protected static final Map<String, String> paramsMap = new HashMap<String, String>();
    protected static final Map<String, String> headers = new HashMap<String, String>();
    protected static final List<Parameter> paramList = new ArrayList<Parameter>();
    static {
        paramsMap.put("username","12700000021");
        paramsMap.put("password","123123a");
        paramsMap.put("grant_type","password");
        headers.put("Authorization",BASIC_HEADER);

        for(Entry<String, String> entry: paramsMap.entrySet()){
            paramList.add(new Parameter(entry.getKey(),entry.getValue()));
        }
    }
    
    @Test
    public void testPost() throws IOException {
        String ok = HttpClient.post(http+"/post", paramsMap,headers);
        Assert.assertEquals("ok",ok);
        ok = HttpClient.post(https+"/post", paramsMap,headers);
        Assert.assertEquals("ok",ok);
        
    }
    @Test
    public void testGet(){
        String ok = HttpClient.get(http+"/get", paramsMap,headers);
        Assert.assertEquals("ok",ok);
        ok = HttpClient.get(https+"/get", paramsMap,headers);
        Assert.assertEquals("ok",ok);
    }
    @Test
    public void testPostBody(){
        String ok = HttpClient.postJsonBody(http+"/json", JSON.toJSONString(paramsMap),headers);
        Assert.assertEquals("ok",ok);
        ok = HttpClient.postJsonBody(https+"/json", JSON.toJSONString(paramsMap),headers);
        Assert.assertEquals("ok",ok);
    }
    
    @BeforeClass
    public static void beforeClass(){
        mockServer = startClientAndServer(port);
        mockServer.when(request().withPath("/post").withMethod("POST")
            .withBody(ParameterBody.params(paramList)).withHeader("Authorization",BASIC_HEADER)
        ).respond(response().withStatusCode(200).withBody("ok"));
        mockServer.when(request().withPath("/get").withMethod("GET")
            .withQueryStringParameters(paramList).withHeader("Authorization",BASIC_HEADER)
        ).respond(response().withStatusCode(200).withBody("ok"));
        mockServer.when(request().withPath("/json").withMethod("POST")
            .withBody(JsonBody.json(paramsMap)).withHeader("Authorization",BASIC_HEADER)
        ).respond(response().withStatusCode(200).withBody("ok"));
    }
    @AfterClass
    public static void afterClass(){
        mockServer.stop();
    }    
}
