package com.baocloud.netty.chap14;

public enum MessageType {
	SEVICE_REQ((byte) 0), SERVICE_RESP((byte) 1), ONE_WAY((byte) 2), LONGIN_REQ((byte) 3), LOGIN_RESP(
			(byte) 4), HEARTBEAT_REQ((byte) 5), HEARTBEAT_RESP((byte) 6);

	private byte value;

	private MessageType(byte value) {
		this.value = value;
	}

	public byte value() {
		return this.value;
	}

}
