package net.bingosoft.oss.imclient.spi.impl;

import net.bingosoft.oss.imclient.spi.PollCallback;

/**
 * 
 * {@link PollCallback}抽象实现，提供{@link PollCallback#onTimeout()}和{@link PollCallback#onError(Throwable)}的默认处理。
 * @author kael.
 */
public abstract class AbstractPollCallback implements PollCallback {
    /**
     * 默认不做处理，继续等待下一个消息
     * @return <code>true</code>
     */
    @Override
    public boolean onTimeout() {
        return true;
    }

    /**
     * 默认把异常信息打印到控制台，继续等待下一个消息
     * @return <code>true</code>
     */
    @Override
    public boolean onError(Throwable e) {
        e.printStackTrace();
        return true;
    }
}
