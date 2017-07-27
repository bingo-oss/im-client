package net.bingosoft.oss.imclient.spi;

import net.bingosoft.oss.imclient.IMClient;
import net.bingosoft.oss.imclient.model.ReceiveMessage;

import java.util.List;

/**
 * 拉取消息的回调接口
 * @author kael.
 */
public interface PollCallback {
    /**
     * 调用{@link IMClient#poll(PollCallback)}的时候，当获取到新消息时会将消息解析成{@link ReceiveMessage}对象的列表并执行这个接口。
     * @return 返回<code>true</code>表示继续获取消息，返回<code>false</code>表示不继续获取消息
     */
    boolean apply(List<ReceiveMessage> messages);

    /**
     * 调用{@link IMClient#poll(PollCallback)}的时候，每次http请求超时后会调这个接口。
     * @return 返回<code>true</code>表示继续获取消息，返回<code>false</code>表示不继续获取消息。
     */
    boolean onTimeout();

    /**
     * 调用{@link IMClient#poll(PollCallback)}的时候，如果出现未知异常，会调用这个接口并把异常传入提供处理。
     * @return 返回<code>true</code>表示继续获取消息，返回<code>false</code>表示不继续获取消息。
     */
    boolean onError(Throwable e);
}
