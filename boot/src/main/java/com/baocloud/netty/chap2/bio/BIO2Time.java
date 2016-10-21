package com.baocloud.netty.chap2.bio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Date;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.baocloud.mul.SleepUtils;

public class BIO2Time {
	private static class TimeServer implements Runnable {
		private int port;

		public TimeServer(int port) {
			this.port = port;
		}

		@Override
		public void run() {
			ServerSocket serverSocket = null;
			TimeHandlerThreadPool threadPool = null;
			try {
				serverSocket = new ServerSocket(port);
				Socket socket = null;
				threadPool = new TimeHandlerThreadPool(1000, 2000);
				while (true) {
					socket = serverSocket.accept();
					// new Thread(new TimeHandler(socket),
					// "TimeHandler").start();
					threadPool.execute(new TimeHandler(socket));
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
					if (threadPool != null) {
						threadPool.shutdown();
					}
					threadPool = null;
				}
			}

		}

	}

	private static class TimeHandler implements Runnable {
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

	private static class TimeClient implements Runnable {
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

	private static class TimeHandlerThreadPool {
		private ExecutorService threadPool;

		public TimeHandlerThreadPool(int maxPoolSize, int queueSize) {
			threadPool = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(), maxPoolSize, 120,
					TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(queueSize));
		}

		public void execute(Runnable task) {
			this.threadPool.execute(task);
		}

		public void shutdown() {
			this.threadPool.shutdown();
		}
	}

	public static void main(String[] args) {
		Thread server = new Thread(new TimeServer(9090), "TimeServer");
		server.start();
		for (int i = 0; i < 800; i++) {
			TimeClient tc = new TimeClient(9090);
			new Thread(tc, "Client").start();
		}
	}

}
