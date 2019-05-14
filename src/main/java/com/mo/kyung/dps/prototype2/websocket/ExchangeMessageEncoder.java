package com.mo.kyung.dps.prototype2.websocket;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

import com.mo.kyung.dps.prototype2.data.datatypes.ExchangeMessage;

public class ExchangeMessageEncoder implements Encoder.Text<ExchangeMessage>{

	@Override
	public void init(EndpointConfig config) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String encode(ExchangeMessage object) throws EncodeException {
		// TODO Auto-generated method stub
		return null;
	}

}
