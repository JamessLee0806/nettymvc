package com.baocloud.netty.chap6;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class TestUserInfo {
	public static void main(String[] args) throws IOException {
		UserInfo user = new UserInfo();
		user.buildUserID(200).buildUserName("Welcome to Netty World");
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream os = new ObjectOutputStream(bos);
		os.writeObject(user);
		os.flush();
		os.close();
		byte[] b = bos.toByteArray();
		System.out.println("The jdk serializable length is " + b.length);
		for (int i = 0; i < b.length; i++) {
			System.out.print(b[i]);
		}
		System.out.println("\n-----------------------------------------");

		System.out.println("The byte array serializable length is " + user.codeC().length);

		for (int i = 0; i < user.codeC().length; i++) {
			System.out.print(user.codeC()[i]);
		}

	}
}
