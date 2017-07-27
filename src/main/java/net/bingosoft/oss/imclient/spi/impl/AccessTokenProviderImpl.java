package net.bingosoft.oss.imclient.spi.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import net.bingosoft.oss.imclient.IMConfig;
import net.bingosoft.oss.imclient.exception.InvalidCodeException;
import net.bingosoft.oss.imclient.internal.Base64;
import net.bingosoft.oss.imclient.internal.HttpClient;
import net.bingosoft.oss.imclient.model.AccessToken;
import net.bingosoft.oss.imclient.spi.AccessTokenProvider;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author kael.
 */
public class AccessTokenProviderImpl implements AccessTokenProvider {
    
    protected final String tokenUrl;
    protected final String clientId;
    protected final String clientSecret;

    public AccessTokenProviderImpl(String tokenUrl, String clientId, String clientSecret) {
        this.tokenUrl = tokenUrl;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    @Override
    public AccessToken refreshAccessToken(AccessToken accessToken){
        Map<String, String> headers = createHeaders();
        Map<String, String> params = new HashMap<String, String>();
        params.put("grant_type", "refresh_token");
        params.put("refresh_token", accessToken.getRefreshToken());
        try {
            String json = HttpClient.post(tokenUrl,params,headers);
            return parseToAccessToken(json);
        } catch (Exception e) {
            throw new InvalidCodeException("refresh token error:",e);
        }
    }
    @Override
    public AccessToken obtainAccessTokenByClientCredentials(){
        Map<String, String> headers = createHeaders();
        Map<String, String> params = new HashMap<String, String>();
        params.put("grant_type", "client_credentials");
        try {
            String json = HttpClient.post(tokenUrl,params,headers);
            return parseToAccessToken(json);
        } catch (Exception e) {
            throw new InvalidCodeException("obtain access token by client credentials error:",e);
        }
    }
    
    /**========= 内部方法 =========**/
    
    protected AccessToken parseToAccessToken(String text){
        JSONObject json = JSON.parseObject(text);
        String at = json.getString("access_token");
        String rt = json.getString("refresh_token");
        String type = json.getString("token_type");
        int expiresIn = json.getInteger("expires_in");
        long expires = System.currentTimeMillis()/1000+expiresIn;
        AccessToken accessToken = new AccessToken();
        accessToken.setAccessToken(at);
        accessToken.setRefreshToken(rt);
        accessToken.setTokenType(type);
        accessToken.setExpires(expires);
        return accessToken;
    }
    
    protected Map<String, String> createHeaders(){
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Authorization", buildBasicHeader());
        return headers;
    }
    
    public String buildBasicHeader(){
        try {
            String str = clientId+":"+clientSecret;
            return "Basic " + new String(Base64.encode(str));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
