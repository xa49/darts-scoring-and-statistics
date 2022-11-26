package app.darts.stats.trackers;

import app.darts.DartsConstants;
import app.darts.stats.LegDataProvider;
import app.darts.stats.GameEventDispatcher;
import app.darts.stats.LegEvent;
import app.darts.stats.LegEventUser;

public class OutshotAttemptTracker implements LegEventUser {
    private int outshotAttemptCount;

    public OutshotAttemptTracker(GameEventDispatcher gameEventDispatcher) {
        gameEventDispatcher.subscribe(LegEvent.CURRENT_PLAYER_THROW, this);
    }

    public int getOutshotAttemptCount() {
        return outshotAttemptCount;
    }

    @Override
    public void update(LegEvent event, LegDataProvider dataProvider) {
        int scoreBeforeThrow = dataProvider.getPreThrowScore();
        if (scoreBeforeThrow == DartsConstants.INNER_BULLSEYE_VALUE
                ||( scoreBeforeThrow <= DartsConstants.MAX_DOUBLE_CHECKOUT_VALUE && scoreBeforeThrow % 2 == 0)) {
            outshotAttemptCount++;
        }
    }
}
