package com.baocloud.netty.chap2.bio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Date;

import com.baocloud.mul.SleepUtils;


public class BIOTime {
	public static void main(String[] args) {
		Thread server=new Thread(new TimeServer(9090), "TimeServer");
		server.start();
		for(int i=0;i<800;i++){
			TimeClient tc=new TimeClient(9090);
			new Thread(tc, "Client").start();
		}
	}
}

class TimeServer implements Runnable {
	private int port;

	public TimeServer(int port) {
		this.port = port;
	}

	@Override
	public void run() {
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(port);
			Socket socket = null;
			while (true) {
				socket = serverSocket.accept();
				new Thread(new TimeHandler(socket), "TimeHandler").start();
			}
		} catch (Exception e) {

		} finally {
			if (serverSocket != null) {
				System.out.println("Server close");
				try {
					serverSocket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				serverSocket = null;
			}
		}

	}

}

class TimeHandler implements Runnable {
	private Socket socket;

	public TimeHandler(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		BufferedReader br = null;
		PrintWriter pw = null;

		try {
			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			pw = new PrintWriter(socket.getOutputStream(), true);
			String currentTime = null;
			String body = null;
			while (true) {
				body = br.readLine();
				if (body == null)
					break;
				System.out.println("The time sever receive order:" + body);
				currentTime = "QUERY TIME ORDER".equals(body) ? new Date(System.currentTimeMillis()).toString()
						: "BAD ORDER";
				pw.println(currentTime);
			}
		} catch (Exception e) {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}

			if (pw != null) {
				pw.close();
				pw = null;
			}
			if (socket != null) {
				try {
					socket.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				socket = null;
			}
		}

	}
}

class TimeClient implements Runnable {
	private int port;

	public TimeClient(int port) {
		this.port = port;
	}

	@Override
	public void run() {
		Socket socket = null;
		BufferedReader br = null;
		PrintWriter pw = null;
		try {
			socket = new Socket("127.0.0.1", port);
			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			pw = new PrintWriter(socket.getOutputStream(), true);
			pw.println("QUERY TIME ORDER");
			String resp = br.readLine();
			System.out.println("Now:" + resp);
			SleepUtils.second(2);
		} catch (Exception e) {
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}

			if (pw != null) {
				pw.close();
				pw = null;
			}
			if (socket != null) {
				try {
					socket.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				socket = null;
			}
		}
	}

}