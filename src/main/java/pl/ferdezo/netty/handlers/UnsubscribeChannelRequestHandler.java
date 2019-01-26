package pl.ferdezo.netty.handlers;

import io.netty.channel.Channel;
import pl.ferdezo.netty.domain.Subscriptions;

public class UnsubscribeChannelRequestHandler extends ChannelRequestHandler {
    public UnsubscribeChannelRequestHandler(Channel channel) {
        super(channel);
    }

    @Override
    public String handleAndProduceResponse(String text) {
        try {
            Subscriptions.unsubscribe(text, channel);
            return "UNSUBSCRIBED";
        } catch (IllegalArgumentException e) {
            return "UNSUBSCRIBE_ERROR";
        }
    }
}
