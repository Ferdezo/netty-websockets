package pl.ferdezo.netty.domain.match;

@FunctionalInterface
public interface MatchEventSource {
    String getMatchEvent();
}
