package app.darts.stats.trackers;

import app.darts.stats.LegDataProvider;
import app.darts.stats.LegEvent;
import app.darts.stats.LegEventUser;
import app.darts.stats.GameEventDispatcher;

public class LegsPlayedTracker implements LegEventUser {
    private int legsPlayedCount;

    public LegsPlayedTracker(GameEventDispatcher gameEventDispatcher) {
        gameEventDispatcher.subscribe(LegEvent.CURRENT_PLAYER_FINISHED_LEG, this);
    }

    public int getLegsPlayedCount() {
        return legsPlayedCount;
    }

    @Override
    public void update(LegEvent event, LegDataProvider dataProvider) {
        legsPlayedCount++;
    }
}
