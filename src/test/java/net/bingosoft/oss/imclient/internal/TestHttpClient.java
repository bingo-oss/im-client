package net.bingosoft.oss.imclient.internal;

import org.junit.Assert;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author kael.
 */
public class TestHttpClient {
    @Test
    public void testPost() throws UnsupportedEncodingException {
        Map<String, String> params = new HashMap<String, String>();
        params.put("username","12700000021");
        params.put("password","123123a");
        params.put("grant_type","password");
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Authorization","Basic "+new String(Base64.encode("clientId:clientSecret"),"UTF-8"));
        String json = HttpClient.post("https://10.201.76.141:443/sso/oauth2/token",params,headers);
        Assert.assertNotNull(json);
        System.out.println(json);
    }
    
}
