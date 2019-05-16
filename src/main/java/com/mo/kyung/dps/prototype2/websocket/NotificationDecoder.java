package com.mo.kyung.dps.prototype2.websocket;

import java.io.IOException;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

import com.mo.kyung.dps.prototype2.data.representations.ExchangeMessageRepresentation;

public class NotificationDecoder implements Decoder.Text<ExchangeMessageRepresentation>{

	@Override
	public void init(EndpointConfig config) {
	}

	@Override
	public void destroy() {
	}

	@Override
	public ExchangeMessageRepresentation decode(String message) throws DecodeException {
		try {
            return Constants.getMapper().readValue(message, ExchangeMessageRepresentation.class);
        } catch (IOException e) {
            throw new DecodeException(message, "Unable to decode text to Message", e);
        }
	}

	@Override
	public boolean willDecode(String message) {
		return message.contains(Constants.getUserNameKey())
				&& message.contains(Constants.getMessageKey())
				&& message.contains(Constants.getEditionDateKey())
				&& message.contains(Constants.getTopicKey());
    }

}
