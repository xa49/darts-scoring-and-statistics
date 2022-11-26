package app.darts.stats;

import java.util.*;


public class ConcreteGameEventDispatcher implements GameEventDispatcher {
    private final Map<LegEvent, List<LegEventUser>> subscribers = new EnumMap<>(LegEvent.class);


    @Override
    public void subscribe(LegEvent event, LegEventUser user) {
        subscribers.computeIfAbsent(event, l -> new ArrayList<>()).add(user);
    }

    @Override
    public void publish(LegEvent event, LegDataProvider dataProvider) {
        subscribers.getOrDefault(event, new ArrayList<>()).forEach(
                u -> u.update(event, dataProvider));
    }
}
