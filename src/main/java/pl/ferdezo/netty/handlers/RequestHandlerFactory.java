package pl.ferdezo.netty.handlers;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

@UtilityClass
public class RequestHandlerFactory {

    public static RequestHandler create(String message) {
        if (StringUtils.startsWith(message, "SUBSCRIBE")) {
            return new SubscribeRequestHandler();
        }

        if (StringUtils.startsWith(message, "UNSUBSCRIBE")) {
            return new UnsubscribeRequestHandler();
        }

        throw new IllegalArgumentException("Unrecognized request: " + message);
    }
}
