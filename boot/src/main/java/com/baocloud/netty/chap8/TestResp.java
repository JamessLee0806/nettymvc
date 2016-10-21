package com.baocloud.netty.chap8;

import com.google.protobuf.InvalidProtocolBufferException;

public class TestResp {
	private static byte[] encode(SubscribeRespProto.SubscribeResp resp){
		return resp.toByteArray();
	}
	
	private static SubscribeRespProto.SubscribeResp decode(byte[] body) throws InvalidProtocolBufferException{
		return SubscribeRespProto.SubscribeResp.parseFrom(body);
	}
	
	private static SubscribeRespProto.SubscribeResp create(){
		SubscribeRespProto.SubscribeResp.Builder builder=SubscribeRespProto.SubscribeResp.newBuilder();
		builder.setRespCode(1);
		builder.setDesc("dddd");
		builder.setSubReqID(123);
		return builder.build();
	}
	
	public static void main(String[] args) throws InvalidProtocolBufferException {
		SubscribeRespProto.SubscribeResp resp=create();
		System.out.println(resp);
		SubscribeRespProto.SubscribeResp resp2=decode(encode(resp));
		System.out.println(resp2);
	}

}
