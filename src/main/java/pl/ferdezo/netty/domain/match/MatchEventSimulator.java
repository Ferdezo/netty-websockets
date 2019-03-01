package pl.ferdezo.netty.domain.match;

import io.netty.channel.group.ChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.log4j.Log4j2;
import pl.ferdezo.netty.domain.Subscriptions;

import java.util.*;
import java.util.function.Predicate;

@Log4j2
public class MatchEventSimulator {
    private final MatchEventSource matchEventSource = new DummyMatchEventSource();

    public void simulate() {
        final Predicate<String> randomFilter = s -> new Random().nextBoolean();

        Subscriptions.getSubscribedChannels()
            .stream()
            .filter(randomFilter)
            .map(Subscriptions::getSubscribersForChannel)
            .filter(Objects::nonNull)
            .forEach(this::sendMatchEvent);

    }

    private void sendMatchEvent(ChannelGroup channelGroup) {
        final String matchEvent = matchEventSource.getMatchEvent();
        log.info("Sending match event: {} to {}", matchEvent, channelGroup.name());
        channelGroup.writeAndFlush(new TextWebSocketFrame(channelGroup.name() + " : " + matchEvent));
    }
}
