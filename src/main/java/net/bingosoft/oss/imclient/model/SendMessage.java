package net.bingosoft.oss.imclient.model;

/**
 * @author kael.
 */
public class SendMessage extends Message {
 
	/**
     * to_ids
     * 指定消息具体接收者，发送互联群组消息时用到
     */
    protected String toIds;

	public String getToIds() {
		return toIds;
	}

	public void setToIds(String toIds) {
		this.toIds = toIds;
	}
}