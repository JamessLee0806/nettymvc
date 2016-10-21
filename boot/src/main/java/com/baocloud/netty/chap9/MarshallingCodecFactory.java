package com.baocloud.netty.chap9;

import org.jboss.marshalling.MarshallerFactory;
import org.jboss.marshalling.Marshalling;
import org.jboss.marshalling.MarshallingConfiguration;

import io.netty.handler.codec.marshalling.DefaultMarshallerProvider;
import io.netty.handler.codec.marshalling.DefaultUnmarshallerProvider;
import io.netty.handler.codec.marshalling.MarshallerProvider;
import io.netty.handler.codec.marshalling.MarshallingDecoder;
import io.netty.handler.codec.marshalling.MarshallingEncoder;
import io.netty.handler.codec.marshalling.UnmarshallerProvider;

public class MarshallingCodecFactory {
	public static MarshallingDecoder buildMarshallingDecoder() {
		final MarshallerFactory fac = Marshalling.getMarshallerFactory("serial");
		final MarshallingConfiguration conf = new MarshallingConfiguration();
		conf.setVersion(5);
		UnmarshallerProvider provider = new DefaultUnmarshallerProvider(fac, conf);
		MarshallingDecoder decoder = new MarshallingDecoder(provider, 1024);

		return decoder;
	}

	public static MarshallingEncoder buildMarshalingEncoder() {
		final MarshallerFactory fac = Marshalling.getMarshallerFactory("serial");
		final MarshallingConfiguration conf = new MarshallingConfiguration();
		conf.setVersion(5);
		MarshallerProvider provider = new DefaultMarshallerProvider(fac, conf);
		MarshallingEncoder encoder = new MarshallingEncoder(provider);

		return encoder;
	}

}
