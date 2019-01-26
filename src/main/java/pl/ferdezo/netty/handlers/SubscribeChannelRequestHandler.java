package pl.ferdezo.netty.handlers;

import io.netty.channel.Channel;
import pl.ferdezo.netty.domain.Subscriptions;

public class SubscribeChannelRequestHandler extends ChannelRequestHandler {
    public SubscribeChannelRequestHandler(Channel channel) {
        super(channel);
    }

    @Override
    public String handleAndProduceResponse(String text) {
        Subscriptions.subscribe(text, channel);
        return "SUBSCRIBED";
    }
}
