package net.bingosoft.oss.imclient.internal;

import com.alibaba.fastjson.JSON;
import net.bingosoft.oss.imclient.exception.HttpRequestException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.ssl.AllowAllHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;

import javax.net.ssl.SSLContext;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author kael.
 */
public class HttpClient {

    protected static final org.apache.http.client.HttpClient https = https();
    protected static final org.apache.http.client.HttpClient http = http();
    
    public static String postJsonBody(String url, Object body, Map<String, String> headers) throws HttpRequestException{
        URI uri = URI.create(url);
        HttpHost host = parseHost(uri);
        HttpPost req = post(uri);
        if(null != headers && headers.size() > 0){
            for (Map.Entry<String,String> entry : headers.entrySet()){
                req.addHeader(entry.getKey(),entry.getValue());
            }
        }
        HttpEntity entity = EntityBuilder.create().setContentType(ContentType.APPLICATION_JSON)
                .setContentEncoding("UTF-8").setText(JSON.toJSONString(body)).build();
        req.setEntity(entity);
        return sendAndGetContent(req,host);
    }
    
    public static String post(String url, Map<String, String> params, Map<String, String> headers) throws HttpRequestException{
        URI uri = URI.create(url);
        HttpHost host = parseHost(uri);
        HttpPost req = post(uri);
        if(null != params && params.size() > 0){
            List<NameValuePair> nps = new ArrayList<NameValuePair>(params.size());
            for (Map.Entry<String,String> entry : params.entrySet()){
                nps.add(new BasicNameValuePair(entry.getKey(),entry.getValue()));
            }
            req.setEntity(EntityBuilder.create().setParameters(nps).build());
        }
        if(null != headers && headers.size() > 0){
            for (Map.Entry<String,String> entry : headers.entrySet()){
                req.addHeader(entry.getKey(),entry.getValue());
            }
        }
        return sendAndGetContent(req,host);
    }
    
    public static String get(String url, Map<String, String> params, Map<String, String> headers) throws HttpRequestException{
        URI uri = URI.create(url);
        HttpHost host = parseHost(uri);
        RequestBuilder builder = RequestBuilder.get().setUri(uri);
        if(null != params && params.size() > 0){
            for (Map.Entry<String,String> entry : params.entrySet()){
                builder.addParameter(new BasicNameValuePair(entry.getKey(),entry.getValue()));
            }
        }
        if(null != headers && headers.size() > 0){
            for (Map.Entry<String,String> entry : headers.entrySet()){
                builder.addHeader(entry.getKey(),entry.getValue());
            }
        }
        HttpUriRequest request = builder.build();
        return sendAndGetContent(request,host);
    }
    
    protected static HttpResponse send(HttpRequest request, HttpHost host){
        try {
            if("https".equalsIgnoreCase(host.getSchemeName())){
                return https.execute(host,request);
            }else {
                return http.execute(host,request);
            }
        } catch (IOException e) {
            throw new HttpRequestException(e);
        }
    }

    protected static HttpHost parseHost(URI uri){
        String h = uri.getHost();
        String scheme = uri.getScheme();
        int port = uri.getPort();
        return new HttpHost(h,port,scheme);
    }
    
    protected static String sendAndGetContent(HttpRequest request, HttpHost host){
        HttpResponse response = send(request,host);
        int code = response.getStatusLine().getStatusCode();
        InputStream is = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        StringBuilder content = new StringBuilder();
        try {
            is = response.getEntity().getContent();
            isr = new InputStreamReader(is,"UTF-8");
            br = new BufferedReader(isr);
            do{
                String line = br.readLine();
                if(null == line){
                    break;
                }
                content.append(line);
                content.append("\n");
            }while (true);
        } catch (IOException e) {
            throw new HttpRequestException(e);
        } finally {
            try {
                if(null != br){
                    br.close();
                }
                if(null != isr){
                    isr.close();
                }
                if(null != is){
                    is.close();
                }
            }catch (IOException e1){
                throw new HttpRequestException("close input stream error",e1);
            }
        }
        if(code>=400){
            throw new HttpRequestException("error status: " + code + "\n" + content.toString());
        }
        return content.toString();
    }

    protected static HttpGet get(URI uri){
        return new HttpGet(uri);
    }

    protected static HttpPost post(URI uri){
        return new HttpPost(uri);
    }
    
    private HttpClient() {}

    public static org.apache.http.client.HttpClient http() {
        return HttpClients.createDefault();
    }
    
    public static org.apache.http.client.HttpClient https() {
        try {
            SSLContext sslContext = SSLContexts.custom().useTLS().loadTrustMaterial(null, new TrustStrategy() {
                @Override
                public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    return true;
                }
            }).build();
            SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslContext,
                    new AllowAllHostnameVerifier());

            Registry<ConnectionSocketFactory> r = RegistryBuilder.<ConnectionSocketFactory>create()
                    .register("https", sslConnectionSocketFactory)
                    .build();
            PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(r);
            cm.setMaxTotal(500);
            cm.setDefaultMaxPerRoute(350);

            SocketConfig socketConfig = SocketConfig.custom()
                    .setSoKeepAlive(true)
                    .setTcpNoDelay(true)
                    .setSoTimeout(20000)
                    .build();
            cm.setDefaultSocketConfig(socketConfig);

            RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(20000)
                    .setConnectTimeout(20000).setSocketTimeout(20000).build();

            CloseableHttpClient client = HttpClients.custom()
                    .setConnectionManager(cm)
                    .setDefaultRequestConfig(requestConfig)
                    .build();
            return client;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
        return null;
    }
    
}
