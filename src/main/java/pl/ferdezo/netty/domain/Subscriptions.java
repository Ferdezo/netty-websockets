package pl.ferdezo.netty.domain;

import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * Dummy in memory subscriptions management
 */
@Log4j2
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Subscriptions {
    private final static Map<String, ChannelGroup> subscriptions = new ConcurrentHashMap<>();

    public static void subscribe(String channelGroupName, Channel channel) {
        log.debug("Registering channel: {} into channel group: {}", channel, channelGroupName);

        final Consumer<ChannelGroup> subscribeToChannelGroup = channelGroup -> channelGroup.add(channel);

        final Runnable subscribeToNewChannelGroup = () -> {
            DefaultChannelGroup newChannelGroup = new DefaultChannelGroup(channelGroupName, GlobalEventExecutor.INSTANCE);
            newChannelGroup.add(channel);
            subscriptions.put(channelGroupName, newChannelGroup);
        };

        Optional
            .ofNullable(subscriptions.get(channelGroupName))
            .ifPresentOrElse(
                subscribeToChannelGroup,
                subscribeToNewChannelGroup);

    }

    public static void unsubscribe(String channelGroupName, Channel channel) {
        Optional
            .ofNullable(subscriptions.get(channelGroupName))
            .orElseThrow(IllegalArgumentException::new)
            .add(channel);
    }

    public static Set<String> getSubscribedChannels() {
        return subscriptions.keySet();
    }

    public static ChannelGroup getSubscribersForChannel(String channelName) {
        return subscriptions.get(channelName);
    }
}
