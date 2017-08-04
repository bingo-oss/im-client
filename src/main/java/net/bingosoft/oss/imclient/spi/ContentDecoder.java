package net.bingosoft.oss.imclient.spi;

import net.bingosoft.oss.imclient.model.msg.Content;

/**
 * 消息解码器接口，如果收到某些SDK未实现的消息类型，可以自行实现这个接口并通过
 * @author kael.
 */
public interface ContentDecoder<T extends Content> {
    /**
     * 返回这个解码器是否支持这种消息类型
     * @return <code>true</code>表示支持解析这种消息类型，<code>false</code>表示不支持
     */
    boolean match(int msgType);

    /**
     * 解码消息内容，查看{@link Content}，传入的消息是未解密的消息内容。
     * 正常情况下需要先解密，后解码成{@link Content}对象
     * @return 解码后的{@link Content}对象
     */
    T decode(String content);
}