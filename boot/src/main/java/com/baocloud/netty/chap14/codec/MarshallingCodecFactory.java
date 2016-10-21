package com.baocloud.netty.chap14.codec;

import org.jboss.marshalling.Marshaller;
import org.jboss.marshalling.MarshallerFactory;
import org.jboss.marshalling.Marshalling;
import org.jboss.marshalling.MarshallingConfiguration;
import org.jboss.marshalling.Unmarshaller;

public final class MarshallingCodecFactory {
	protected static Marshaller buildMashalling() throws Exception {
		final MarshallerFactory fac = Marshalling.getMarshallerFactory("serial");
		final MarshallingConfiguration conf = new MarshallingConfiguration();
		conf.setVersion(5);
		Marshaller marshaller = fac.createMarshaller(conf);

		return marshaller;
	}

	protected static Unmarshaller buildUnMarshalling() throws Exception {
		final MarshallerFactory fac = Marshalling.getMarshallerFactory("serial");
		final MarshallingConfiguration conf = new MarshallingConfiguration();
		conf.setVersion(5);

		Unmarshaller unm = fac.createUnmarshaller(conf);

		return unm;
	}

}
