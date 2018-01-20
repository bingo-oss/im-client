package net.bingosoft.oss.imclient.model.msg;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;

/**
 * 消息体：已读指令
 * @author tangheng<br/>
 * Sep 15, 2015
 */
public class RecRead extends Content {
	
	private String readMsgIds;
	private String readMsgTypes;
	private String toId;
	private int toType;
	private long lastSendTime;
	// 98-代表动态服务号
	private int cmd = -1;
	
	public RecRead() {}

	public RecRead(String readMsgIds, String readMsgTypes, int cmd) {
		this.readMsgIds = readMsgIds;
		this.readMsgTypes = readMsgTypes;
		this.cmd = cmd;
	}

	@Override
    public String toContentString() {
        return JSON.toJSONString(toContentMap());
    }
    
    protected Map<String, Object> toContentMap() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("readMsgIds",readMsgIds);
        map.put("readMsgTypes",readMsgTypes);
        map.put("toId",toId);
        map.put("toType",toType);
        map.put("lastSendTime",lastSendTime);
        map.put("cmd",cmd);
        return map;
    }

	public String getReadMsgIds() {
		return readMsgIds;
	}

	public void setReadMsgIds(String readMsgIds) {
		this.readMsgIds = readMsgIds;
	}

	public String getReadMsgTypes() {
		return readMsgTypes;
	}

	public void setReadMsgTypes(String readMsgTypes) {
		this.readMsgTypes = readMsgTypes;
	}

	public String getToId() {
		return toId;
	}

	public void setToId(String toId) {
		this.toId = toId;
	}

	public int getToType() {
		return toType;
	}

	public void setToType(int toType) {
		this.toType = toType;
	}

	public long getLastSendTime() {
		return lastSendTime;
	}

	public void setLastSendTime(long lastSendTime) {
		this.lastSendTime = lastSendTime;
	}

	public int getCmd() {
		return cmd;
	}

	public void setCmd(int cmd) {
		this.cmd = cmd;
	}
}