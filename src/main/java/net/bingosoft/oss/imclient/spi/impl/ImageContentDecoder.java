package net.bingosoft.oss.imclient.spi.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import net.bingosoft.oss.imclient.IMUtil;
import net.bingosoft.oss.imclient.model.MsgType;
import net.bingosoft.oss.imclient.model.msg.Image;
import net.bingosoft.oss.imclient.spi.ContentDecoder;

/**
 * @author kael.
 */
public class ImageContentDecoder implements ContentDecoder<Image> {
    @Override
    public boolean match(int msgType) {
        return MsgType.IMAGE == msgType;
    }

    @Override
    public Image decode(String content) {
        String decrypted = IMUtil.decryptContent(content);
        Image image = new Image();
        try {
            JSONObject json = JSON.parseObject(decrypted);
            image.setDownloadUrl(json.getString("download_url"));
            image.setExtension(json.getString("extension"));
            image.setFileName(json.getString("file_name"));
            image.setSize(json.getLong("size"));
            return image;
        } catch (Exception e) {
            return image;
        }
    }
}
