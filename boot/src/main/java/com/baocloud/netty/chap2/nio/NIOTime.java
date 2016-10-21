package com.baocloud.netty.chap2.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NIOTime {
	private static class NioTimeServer implements Runnable {
		private Selector selector;
		private ServerSocketChannel srvChannel;
		private volatile boolean stop;

		public NioTimeServer(int port) {
			try {
				selector = Selector.open();
				srvChannel = ServerSocketChannel.open();
				srvChannel.configureBlocking(false);
				srvChannel.socket().bind(new InetSocketAddress(port), 1024);
				srvChannel.register(selector, SelectionKey.OP_ACCEPT);
				System.out.println("The time server is start in port:" + port);
			} catch (Exception e) {
				e.printStackTrace();
				System.exit(1);
			}
		}

		public void stop() {
			this.stop = true;
		}

		@Override
		public void run() {
			while (!stop) {
				try {
					selector.select(1000);
					Set<SelectionKey> it = selector.selectedKeys();
					System.out.println(it.size());
					Iterator<SelectionKey> keys = it.iterator();
					SelectionKey key = null;
					while (keys.hasNext()) {
						key = keys.next();
						keys.remove();
						try {
							handleInput(key);
							key.channel().toString();
						} catch (Exception e) {
							e.printStackTrace();
							if (key != null) {
								key.cancel();
								if (key.channel() != null)
									key.channel().close();
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			//
			if (selector != null) {
				try {
					selector.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		private void handleInput(SelectionKey key) throws Exception {
			if (key.isValid()) {
				if (key.isAcceptable()) {// 链接
					ServerSocketChannel ssChannel = (ServerSocketChannel) key.channel();
					SocketChannel channel = ssChannel.accept();
					channel.configureBlocking(false);
					channel.register(selector, SelectionKey.OP_READ);
				}
				if (key.isReadable()) {// 可读
					SocketChannel channel = (SocketChannel) key.channel();
					ByteBuffer readBuffer = ByteBuffer.allocate(1024);
					int readBytes = channel.read(readBuffer);
					if (readBytes > 0) {
						readBuffer.flip();
						byte[] bytes = new byte[readBuffer.remaining()];
						readBuffer.get(bytes);
						String body = new String(bytes, "UTF-8");
						System.out.println("The time server receive order:" + body);
						String currentTime = "QUERY TIME ORDER".equals(body)
								? new Date(System.currentTimeMillis()).toString() : "BAD ORDER";
						doWrite(channel, currentTime);
					} else if (readBytes < 0) {
						key.cancel();
						channel.close();
					} else {
						//
					}

				}

			}
		}

		private void doWrite(SocketChannel channel, String response) throws IOException {
			if (response != null && response.length() > 0) {
				byte[] bytes = response.getBytes();
				ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
				writeBuffer.put(bytes);
				writeBuffer.flip();
				channel.write(writeBuffer);
				System.out.println("send...."+response);
			}
		}

	}

	private static class TimeClientHandler implements Runnable {
		private int port;
		private String host;
		private Selector selector;
		private SocketChannel scChannel;
		private volatile boolean stop;
		private  String threadName=null;

		public TimeClientHandler(int port, String host) {
			this.host = host == null ? "127.0.0.1" : host;
			this.port = port;
			try {
				selector = Selector.open();
				scChannel = SocketChannel.open();
				scChannel.configureBlocking(false);
			} catch (Exception e) {
				e.printStackTrace();
				System.exit(1);
			}
		}

		@Override
		public void run() {
			threadName=Thread.currentThread().getName();
			try {
				doConnect();
			} catch (Exception e) {
				e.printStackTrace();
				System.exit(1);
			}
			while (!stop) {
				try {
					selector.select(1000);
					Set<SelectionKey> keys = selector.selectedKeys();
					Iterator<SelectionKey> ites = keys.iterator();
					SelectionKey key = null;
					while (ites.hasNext()) {
						key = ites.next();
						ites.remove();
						try {
							handleInput(key);
						} catch (Exception e) {
							e.printStackTrace();
							key.cancel();
							if (key.channel() != null)
								key.channel().close();
						}

					}
				} catch (Exception e) {
					e.printStackTrace();
					System.exit(1);
				}
			}

			if (selector != null) {
				try {
					selector.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}

		private void handleInput(SelectionKey key) throws IOException {
			if (key.isValid()) {
				SocketChannel socketChannel = (SocketChannel) key.channel();
				if (key.isConnectable()) {
					if (socketChannel.finishConnect()) {
						socketChannel.register(selector, SelectionKey.OP_READ);
						doWrite(socketChannel);
					}
				} else {
					//System.exit(1);
					System.out.println(threadName+ " not connet....");
				}
				if (key.isReadable()) {
					ByteBuffer readerBuffer = ByteBuffer.allocate(1024);
					int readCnt = socketChannel.read(readerBuffer);
					if (readCnt > 0) {
						readerBuffer.flip();
						byte[] bytes = new byte[readerBuffer.remaining()];
						readerBuffer.get(bytes);
						String body = new String(bytes, "UTF-8");
						System.out.println(threadName+ " NOW is:" + body);
						this.stop = true;
					} else if (readCnt < 0) {
						key.cancel();
						socketChannel.close();
					} else {

					}

				}
			}
		}

		private void doWrite(SocketChannel channel) throws IOException {
			byte[] req = "QUERY TIME ORDER".getBytes();
			ByteBuffer writeBuffer = ByteBuffer.allocate(req.length);
			writeBuffer.put(req);
			writeBuffer.flip();
			channel.write(writeBuffer);
			if (!writeBuffer.hasRemaining()) {
				System.out.println(threadName+ " send order 2 sever seccuss");
			}
		}

		private void doConnect() throws IOException {
			if (scChannel.connect(new InetSocketAddress(host, port))) {
				scChannel.register(selector, SelectionKey.OP_READ);
				doWrite(scChannel);
			} else {
				scChannel.register(selector, SelectionKey.OP_CONNECT);
			}
		}

	}

	public static void main(String[] args) {
		NioTimeServer server = new NioTimeServer(8080);
		new Thread(server).start();
		ExecutorService pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
		for (int i = 0; i < 100; i++) {
			pool.execute(new TimeClientHandler(8080, null));
		}
		pool.shutdown();
		//server.stop();
	}

}
