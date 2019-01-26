package pl.ferdezo.netty.handlers;

import io.netty.channel.Channel;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

/**
 * Request handler when channel reference will be used
 */
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class ChannelRequestHandler implements RequestHandler {
    protected final Channel channel;
}
