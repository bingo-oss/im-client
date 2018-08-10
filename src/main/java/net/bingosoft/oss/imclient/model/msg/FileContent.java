package net.bingosoft.oss.imclient.model.msg;

import com.alibaba.fastjson.JSON;

import java.util.HashMap;
import java.util.Map;

/**
 * 文件类型的消息
 * @author kael.
 */
public abstract class FileContent extends Content {
    /**
     * file_name
     * 文件名
     */
    protected String fileName;

    /**
     * size
     * 文件大小
     */
    protected long size;

    /**
     * download_url
     * 文件下载地址
     */
    protected String downloadUrl;

    /**
     * extension
     * 文件扩展名
     */
    protected String extension;
    
    /**
     * cipher_key
     * 加密密钥key。默认为空。目前在公安行业可能会用到，接收方需要使用cipher_key对多媒体进行解密。
     */
    protected String cipherKey;

    @Override
    public String toContentString() {
        return JSON.toJSONString(toContentMap());
    }
    
    protected Map<String, Object> toContentMap() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("file_name",fileName);
        map.put("size",size);
        map.put("download_url",downloadUrl);
        map.put("extension",extension);
        map.put("cipher_key",cipherKey);
        return map;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

	public String getCipherKey() {
		return cipherKey;
	}

	public void setCipherKey(String cipherKey) {
		this.cipherKey = cipherKey;
	}
}