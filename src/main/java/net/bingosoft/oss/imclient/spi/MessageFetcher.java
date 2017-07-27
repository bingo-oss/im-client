package net.bingosoft.oss.imclient.spi;

import java.util.List;

/**
 * 消息获取器
 * @author kael.
 */
public interface MessageFetcher {
    /**
     * 获取消息接口，获取到消息之后会调用{@link PollCallback#apply(List)}接口
     * 获取消息超时调用{@link PollCallback#onTimeout()}接口
     * 获取处理消息过程出现所有非预期异常会调用{@link PollCallback#onError(Throwable)}接口
     * 
     * @throws IllegalStateException 如果已经有其他线程在获取消息了，或者当前线程正在获取消息。
     */
    void fetchMessage(PollCallback callback) throws IllegalStateException;
}
