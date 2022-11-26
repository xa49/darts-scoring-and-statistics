package app.darts.stats;

public interface GameEventDispatcher {
    void subscribe(LegEvent e, LegEventUser u);
    void publish(LegEvent e, LegDataProvider dataProvider);
}
