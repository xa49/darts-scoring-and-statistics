package app.darts.stats.trackers;

import app.darts.stats.LegDataProvider;
import app.darts.stats.LegEvent;
import app.darts.stats.LegEventUser;
import app.darts.stats.GameEventDispatcher;

public class LegsWonTracker implements LegEventUser {
    private int legsWonCount;

    public LegsWonTracker(GameEventDispatcher gameEventDispatcher) {
        gameEventDispatcher.subscribe(LegEvent.CURRENT_PLAYER_WON_LEG, this);
    }

    public int getLegsWonCount() {
        return legsWonCount;
    }

    @Override
    public void update(LegEvent event, LegDataProvider dataProvider) {
        legsWonCount++;
    }
}
