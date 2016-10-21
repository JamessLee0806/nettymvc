package com.baocloud.netty.chap6;

import java.io.Serializable;
import java.nio.ByteBuffer;

import lombok.Data;
import lombok.ToString;
@SuppressWarnings("serial")
@Data
@ToString
public class UserInfo implements Serializable {
	private String userName;
	private int userID;
	
	public UserInfo buildUserName(String userName){
		this.userName=userName;
		return this;
	}
	
	public UserInfo buildUserID(int userId){
		this.userID=userId;
		return this;
	}
	
	
	public byte[] codeC(){
		ByteBuffer buffer=ByteBuffer.allocate(1024);
		buffer.clear();
		byte[] value=this.userName.getBytes();
		buffer.putInt(value.length);
		buffer.put(value);
		buffer.putInt(this.userID);
		buffer.flip();
		
		value=null;
		byte[] result=new byte[buffer.remaining()];
		buffer.get(result);
		return  result;
	}
	
	public byte[] codeC(ByteBuffer buffer){
		buffer.clear();
		byte[] value=this.userName.getBytes();
		buffer.putInt(value.length);
		buffer.put(value);
		buffer.putInt(this.userID);
		buffer.flip();
		
		value=null;
		byte[] result=new byte[buffer.remaining()];
		buffer.get(result);
		return  result;
	}
	
	

}
