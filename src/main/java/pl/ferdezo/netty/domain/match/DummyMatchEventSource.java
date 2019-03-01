package pl.ferdezo.netty.domain.match;

import java.util.List;
import java.util.Random;

public class DummyMatchEventSource implements MatchEventSource {
    private final static List<String> matchEvents = List.of(
        "goal", "yellow card", "red card", "penalty", "foul", "corner", "free kick");

    @Override
    public String getMatchEvent() {
        return randomMatchEvent();
    }

    private String randomMatchEvent() {
        int randomIndex = new Random().nextInt(matchEvents.size());
        return matchEvents.get(randomIndex);
    }
}
