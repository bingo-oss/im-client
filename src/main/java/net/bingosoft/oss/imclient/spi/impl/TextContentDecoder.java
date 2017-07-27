package net.bingosoft.oss.imclient.spi.impl;

import net.bingosoft.oss.imclient.IMUtil;
import net.bingosoft.oss.imclient.model.MsgType;
import net.bingosoft.oss.imclient.model.msg.Text;
import net.bingosoft.oss.imclient.spi.ContentDecoder;

/**
 * @author kael.
 */
public class TextContentDecoder implements ContentDecoder<Text> {
    @Override
    public boolean match(int msgType) {
        return msgType == MsgType.TEXT;
    }

    @Override
    public Text decode(String content) {
        String decrypted = IMUtil.decryptContent(content);
        return new Text(decrypted);
    }
}
