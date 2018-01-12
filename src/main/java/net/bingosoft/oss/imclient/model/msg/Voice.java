package net.bingosoft.oss.imclient.model.msg;

import java.util.Map;

import com.alibaba.fastjson.JSON;

/**
 * 语音消息内容
 * @author tangheng.
 */
public class Voice extends FileContent {
	
	/**
     * time_long
     * 时长
     */
	private int timeLong;

	public int getTimeLong() {
		return timeLong;
	}

	public void setTimeLong(int timeLong) {
		this.timeLong = timeLong;
	}

	@Override
	public String toContentString() {
		Map<String, Object> map = toContentMap();
		map.put("time_long", timeLong);
		return JSON.toJSONString(map);
	}
}