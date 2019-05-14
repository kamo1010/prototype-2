package com.mo.kyung.dps.prototype2.websocket;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

import com.mo.kyung.dps.prototype2.data.datatypes.ExchangeMessage;

public class ExchangeMessageDecoder implements Decoder.Text<ExchangeMessage>{

	@Override
	public void init(EndpointConfig config) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ExchangeMessage decode(String s) throws DecodeException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean willDecode(String s) {
		// TODO Auto-generated method stub
		return false;
	}

}
