package link.message.client.content;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 消息详细内容
 * 
 * @author zhongt
 *
 */
public abstract class MessageContent {
	// 消息类型
	@JSONField(serialize=false, deserialize=false)
	protected int type;

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
}
