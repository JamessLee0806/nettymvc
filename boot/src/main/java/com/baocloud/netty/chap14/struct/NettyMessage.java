package com.baocloud.netty.chap14.struct;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public final class NettyMessage {
	private Header header;

    private Object body;
}
