package com.baocloud.netty.chap7;

import java.io.Serializable;

import lombok.Data;
import lombok.ToString;

@SuppressWarnings("serial")
@Data
@ToString
public class SubscribeReq implements Serializable {
	private int subReqID;
	private String userName;
	private String pdtName;
	private String phoneNumber;
	private String address;
	
	
}
