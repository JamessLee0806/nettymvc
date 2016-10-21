package com.baocloud.netty.chap13;

import java.io.File;
import java.io.RandomAccessFile;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.DefaultFileRegion;
import io.netty.channel.FileRegion;
import io.netty.channel.SimpleChannelInboundHandler;

public class FileServerHandler extends SimpleChannelInboundHandler<String> {
	private final String CR=System.getProperty("line.separator");
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}
	@Override
	protected void messageReceived(ChannelHandlerContext ctx, String msg) throws Exception {
		if("exit".equals(msg)||"quit".equals(msg)){
			ctx.channel().close();
		}
		File file=new File(msg);
		if(file.exists()){
			if(!file.isFile()){
				ctx.writeAndFlush("Not a file:"+msg+CR);
				return;
			}
			ctx.write(file +"  "+file.length()+CR);
			RandomAccessFile ra=new RandomAccessFile(file, "r");
			FileRegion fr=new DefaultFileRegion(ra.getChannel(), 0, ra.length());
			ctx.write(fr);
			ctx.writeAndFlush(CR);
			ra.close();
		}else{
			ctx.writeAndFlush("File Not Found:"+msg+CR);
		}
	}

}
