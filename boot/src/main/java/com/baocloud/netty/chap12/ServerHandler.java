package com.baocloud.netty.chap12;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.CharsetUtil;
import io.netty.util.internal.ThreadLocalRandom;

public class ServerHandler extends SimpleChannelInboundHandler<DatagramPacket> {
	private static final String[] DICTIONARY = { "只要功夫深，铁棒磨成针。", "旧时王谢堂前燕，飞入寻常百姓家。", "洛阳亲友如相问，一片冰心在玉壶。",
			"一寸光阴一寸金，寸金难买寸光阴。", "老骥伏枥，志在千里。烈士暮年，壮心不已!" };

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}

	@Override
	protected void messageReceived(ChannelHandlerContext ctx, DatagramPacket msg) throws Exception {
		String req = msg.content().toString(CharsetUtil.UTF_8);
		if ("谚语字典查询?".equals(req)) {
			ByteBuf buffer=Unpooled.copiedBuffer("谚语查询结果:" + nextQuote(), CharsetUtil.UTF_8);
			ctx.writeAndFlush(new DatagramPacket(buffer, msg.sender()));
		}

	}

	private String nextQuote() {
		int id = ThreadLocalRandom.current().nextInt(DICTIONARY.length);
		return DICTIONARY[id];
	}

}
