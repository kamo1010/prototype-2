package com.mo.kyung.dps.prototype2.websocket;

import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(value = "/notify",
					encoders = ExchangeMessageEncoder.class,
					decoders = ExchangeMessageDecoder.class)
public class NotificationEndpoint {

}
