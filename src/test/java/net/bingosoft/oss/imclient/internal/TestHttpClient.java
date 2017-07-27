package net.bingosoft.oss.imclient.internal;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.Body;
import org.mockserver.model.Parameter;
import org.mockserver.model.ParameterBody;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

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
    
    @Test
    public void testPost() throws UnsupportedEncodingException {
        Map<String, String> params = new HashMap<String, String>();
        params.put("username","12700000021");
        params.put("password","123123a");
        params.put("grant_type","password");
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Authorization","Basic "+BASIC_HEADER);
        String ok = HttpClient.post(http+"/post",params,headers);
        Assert.assertEquals("ok",ok);
        System.out.println(ok);
    }
    @BeforeClass
    public static void beforeClass(){
        mockServer = startClientAndServer(port);
        mockServer.when(request().withPath("/post").withMethod("POST")
                .withBody(ParameterBody
                        .params(
                                new Parameter("username","12700000021"),
                                new Parameter("password","123123a"),
                                new Parameter("grant_type", "password")
                        )
                ).withHeader("Authorization",BASIC_HEADER)
        ).respond(response().withStatusCode(200).withBody("ok"));
    }
    @AfterClass
    public static void afterClass(){
        mockServer.stop();
    }    
}
