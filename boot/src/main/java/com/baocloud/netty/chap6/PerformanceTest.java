package com.baocloud.netty.chap6;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;

public class PerformanceTest {
	public static void main(String[] args) throws IOException{
		UserInfo user = new UserInfo();
		user.buildUserID(200).buildUserName("Welcome to Netty World");
		int loop=99999999;
		ByteArrayOutputStream bos = null;
		ObjectOutputStream os = null;
		long begin=System.currentTimeMillis();
		for(int i=0;i<loop;i++){
			 bos = new ByteArrayOutputStream();
			 os = new ObjectOutputStream(bos);
			os.writeObject(user);
			os.flush();
			os.close();
		}
		
		long end=System.currentTimeMillis();
		System.out.println("The jdk serializable cost time "+(end-begin) +" ms");
		System.out.println("--------------------------------------------------");
		
		ByteBuffer buff=ByteBuffer.allocate(1024);
		begin=System.currentTimeMillis();
		for(int i=0;i<loop;i++){
			user.codeC(buff);
		}
		
		end=System.currentTimeMillis();
		System.out.println("The byte array serialiable cost time is "+(end-begin)+" ms");
		
		
	}

}
