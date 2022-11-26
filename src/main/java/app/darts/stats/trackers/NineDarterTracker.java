package app.darts.stats.trackers;

import app.darts.DartsConstants;
import app.darts.stats.LegDataProvider;
import app.darts.stats.LegEvent;
import app.darts.stats.LegEventUser;
import app.darts.stats.GameEventDispatcher;

public class NineDarterTracker implements LegEventUser {
    private int nineDarterCount;

    public NineDarterTracker(GameEventDispatcher gameEventDispatcher) {
        gameEventDispatcher.subscribe(LegEvent.CURRENT_PLAYER_WON_LEG, this);
    }

    public int getNineDarterCount() {
        return nineDarterCount;
    }

    @Override
    public void update(LegEvent event, LegDataProvider dataProvider) {
        if (dataProvider.getThrowsInLeg().size() == DartsConstants.NINE_DARTER_ARROW_COUNT) {
            nineDarterCount++;
        }
    }

}
