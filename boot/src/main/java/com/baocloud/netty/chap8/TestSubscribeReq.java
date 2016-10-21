package com.baocloud.netty.chap8;

import com.google.protobuf.InvalidProtocolBufferException;

public class TestSubscribeReq {
	private static byte[] encode(SubscribeReqProto.SubscribeReq req) {
		return req.toByteArray();
	}

	private static SubscribeReqProto.SubscribeReq decode(byte[] body) throws InvalidProtocolBufferException {
		return SubscribeReqProto.SubscribeReq.parseFrom(body);
	}
	
	private static SubscribeReqProto.SubscribeReq create(){
		SubscribeReqProto.SubscribeReq.Builder builder=SubscribeReqProto.SubscribeReq.newBuilder();
		builder.setSubReqId(1);
		builder.setUserName("James Lee");
		builder.setProductName("Netty In Action");
		builder.addAddress("nanjing jiangning");
		builder.addAddress("anhui Bozhou");
		builder.addAddress("jiangsu xuzhou");
		return builder.build();
	}
	
	public static void main(String[] args) throws InvalidProtocolBufferException {
		SubscribeReqProto.SubscribeReq req=create();
		System.out.println("Before encode->"+req.toString());
		SubscribeReqProto.SubscribeReq req2=decode(encode(req));
		System.out.println("After encode ->"+req2.toString());
		
		System.out.println("Assert equal ->"+(req.equals(req2)));
		
	}
	

}
