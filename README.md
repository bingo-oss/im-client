# 品高即时消息客户端(Java)

品高即时消息客户端是[品高即时消息服务](http://dev.bingocc.com/guide/services/im.html)的客户端，调用了即使消息服务的webapi接口。

## 安装

**Maven**

```xml
<dependency>
	<groupId>net.bingosoft.oss</groupId>
	<artifactId>sso-client</artifactId>
	<version>[1.0.0,]</version>
</dependency>
```

## 运行环境

|SDK版本|Java版本|
| ------ | -------  |
|1.x.x   |6+        |

## 外部依赖

|名称      | 版本    | 依赖说明|      
| ------- | ------- | ------- |  
|fastjson | 1.2.31+ | JSON解析 |

## 前提条件

品高即时消息服务已经接入[品高统一认证](http://dev.bingocc.com/guide/services/auth.html)。

使用即时消息服务需要先[注册应用](http://dev.bingocc.com/guide/services/auth_client.html)，并获取[获取访问令牌](http://dev.bingocc.com/guide/services/auth_at.html)。

> 如何注册应用和获取访问令牌不在此文档说明。

## 使用

在开始发送和获取消息之前，需要先构造即使消息客户端对象`IMClient`。

`IMClient`依赖三个对象：

* `IMConfig`：跟即时消息服务相关的配置。
* `AccessTokenProvider`：管理访问令牌的对象。
* `AccessToken`：访问令牌，关于访问令牌的详细说明请看[获取访问令牌](http://dev.bingocc.com/guide/services/auth_at.html)。

```java
String at = "..."; // 访问令牌
String rt = "..."; // 刷新令牌
String type = "..."; // 令牌类型
long expires = "..."; // 过期时间
AccessToken accessToken = new AccessToken();
accessToken.setAccessToken(at);
accessToken.setRefreshToken(rt);
accessToken.setTokenType(type);
accessToken.setExpires(expires);

String tokenUrl = "..."; // 统一认证服务获取token的接口
String clientId = "..."; // 统一认证服务中注册的应用标识
String clientSecret = "..."; // 统一认证服务中注册的应用密钥
String imUrl = "..."; // 即时消息服务地址

IMConfig config = new IMConfig(imUrl);
AccessTokenProvider tp = new AccessTokenProviderImpl(tokenUrl, clientId, clientSecret);
IMClient client = new IMClient(config, tp, accessToken);
```

> **注：** 如果只是用于服务号发送消息，可以不需要预先构造访问令牌，按照如下方式创建`IMClient`:
> 
> ```java
> IMClient client = new IMClient(config, tp);
> ```

### 1. 发送消息

发送消息，发送消息需要构造消息对象，不同的消息类型有不同的属性，详细的消息类型属性请参考[消息类型](http://dev.bingocc.com/im)。

这里以构造一个文本消息为例：

```java
String fromId = "...";
String fromName = "...";
String fromCompany = "...";
String toId = "...";
String toName = "...";
String toCompany = "...";

IMClient client = new IMClient(config, tp, accessToken);

Message msg = MessageBuilder.userMessage()
    .setFromId(fromId)
    .setFromCompany(fromCompany)
    .setFromName(fromName)

    .setToId(toId)
    .setToName(toName)
    .setToCompany(toCompany)
    .setToType(ObjectType.USER)

    .setMsgType(msgType)
    .setMsgId(UUID.randomUUID().toString())
    .setTaskId(UUID.randomUUID().toString())

    .setIsCountUnread(true)
    .setIsDeleteAfterRead(false)
    .setRecReceipt(false)
    .setIsNeedReadReceipt(false)

    .setContent(msg)

    .build();

client.send(msg);
```

### 2. 获取消息

由于即时消息服务的获取消息接口会阻塞HTTP请求，客户端默认采用异步回调的方式处理获取消息：

```java
IMClient client = new IMClient(config, tp, accessToken);

receiver.poll(new AbstractPollCallback() {
    @Override
    public boolean apply(List<ReceiveMessage> messages) {
        // 处理收到的消息。
        for (ReceiveMessage msg : messages) {
            System.out.println(msg.getMsgType() + ":" + msg.getContent().toContentString());
        }
        return true;
    }
});
```

> 使用获取消息接口需要传入回调对象`PollCallback`，这个接口的具体使用方式请看源码注释。

## 扩展定制

在使用即时消息客户端的过程，有些时候默认的实现可能不满足使用需求，客户端允许对某些功能进行定制。

### 1. 自定义消息获取器

默认的消息获取器是异步回调的，如果不希望使用异步回调的机制，可以自定义消息获取器，修改为同步的方式获取。

如：

```java
public class BlockMessageFetcher implements MessageFetcher {
    protected final IMClient client;
    public BlockMessageFetcher(IMClient client) {
        this.client = client;
    }
    @Override
    public void fetchMessage(PollCallback callback) throws IllegalStateException {
        Map<String, String> headers = client.createHeaders();
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
            callback.apply(rms);
    }
}
```

使用上面实现的阻塞式消息获取器：

```java
IMClient client = new IMClient(config, tp, accessToken);
client.setFetcher(new BlockMessageFetcher(client));
```

### 2. 自定义消息解析器

即时消息的类型非常多，即时消息客户端目前没有实现全部的消息类型解析，但是允许使用者自定义消息类型解析器：

```java
public class FileContentDecoder implements ContentDecoder {
    @Override
    public boolean match(int msgType) {
        return msgType== MsgType.FILE;
    }

    @Override
    public Content decode(String content) {
        String json = IMUtil.decryptContent(content);
        JSONObject object = JSON.parseObject(json);
        FileContent file = new FileContent(){};
        file.setDownloadUrl(object.getString("download_url"));
        file.setExtension(object.getString("extension"));
        file.setFileName(object.getString("file_name"));
        file.setSize(object.getLong("size"));
        return file;
    }
}
```

然后注册这个消息类型解析器即可：

```java
IMClient client = new IMClient(config, tp, accessToken);
client.registerContentDecoder(new FileContentDecoder());
```

> **注意：** 如果没有实现某种类型的消息解析器，那么当客户端接收到这种类型的消息时，将不会对消息内容进行解析，得到的消息会是加密后的密文。

## 常见问题

整理中...