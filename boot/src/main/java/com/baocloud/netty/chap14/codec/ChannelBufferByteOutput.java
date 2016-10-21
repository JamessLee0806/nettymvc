package com.baocloud.netty.chap14.codec;

import java.io.IOException;

import org.jboss.marshalling.ByteOutput;

import io.netty.buffer.ByteBuf;
import lombok.Getter;

/**
 * 
 * @author lijinghua {@link ByteOutput} implementation which write the data to
 *         {@link ByteBuf}
 *
 */
@Getter
public class ChannelBufferByteOutput implements ByteOutput {

	private final ByteBuf buffer;

	public ChannelBufferByteOutput(ByteBuf buff) {
		this.buffer = buff;
	}

	@Override
	public void close() throws IOException {

	}

	@Override
	public void flush() throws IOException {

	}

	@Override
	public void write(int b) throws IOException {
		buffer.writeByte(b);
	}

	@Override
	public void write(byte[] b) throws IOException {
		write(b, 0, b.length);
	}

	@Override
	public void write(byte[] b, int off, int len) throws IOException {
		buffer.writeBytes(b, off, len);

	}
	
	

}
