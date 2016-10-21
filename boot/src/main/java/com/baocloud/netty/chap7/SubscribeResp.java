package com.baocloud.netty.chap7;

import java.io.Serializable;

import lombok.Data;
import lombok.ToString;

@SuppressWarnings("serial")
@Data
@ToString
public class SubscribeResp implements Serializable {
	private int subReqId;
	private String respCode;
	private String desc;
}
