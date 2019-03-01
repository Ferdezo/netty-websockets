package pl.ferdezo.netty.domain;

import io.netty.channel.group.ChannelGroup;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@RequiredArgsConstructor
@Getter
public class ReadOnlySubscriptions {
    private final Map<String, ChannelGroup> subcriptions;
}
