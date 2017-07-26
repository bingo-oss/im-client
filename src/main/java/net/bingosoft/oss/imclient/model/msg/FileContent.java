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

    @Override
    public String toContentString() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("file_name",fileName);
        map.put("size",size);
        map.put("download_url",downloadUrl);
        map.put("extension",extension);
        return JSON.toJSONString(map);
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
}
