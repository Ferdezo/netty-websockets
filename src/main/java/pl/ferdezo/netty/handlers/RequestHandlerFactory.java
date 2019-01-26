package pl.ferdezo.netty.handlers;

import io.netty.channel.Channel;
import org.apache.commons.lang3.StringUtils;

public final class RequestHandlerFactory {

    public static RequestHandler create(String message, Channel channel) {
        if (StringUtils.startsWith(message, "SUBSCRIBE")) {
            return new SubscribeChannelRequestHandler(channel);
        }

        if (StringUtils.startsWith(message, "UNSUBSCRIBE")) {
            return new UnsubscribeChannelRequestHandler(channel);
        }

        throw new IllegalArgumentException("Unrecognized request: " + message);
    }

    private RequestHandlerFactory() {}
}
