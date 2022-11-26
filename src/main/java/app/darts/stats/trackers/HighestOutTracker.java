package app.darts.stats.trackers;

import app.darts.DartThrow;
import app.darts.stats.LegDataProvider;
import app.darts.stats.GameEventDispatcher;
import app.darts.stats.LegEvent;
import app.darts.stats.LegEventUser;

public class HighestOutTracker implements LegEventUser {
    private int highestOut;

    public HighestOutTracker(GameEventDispatcher gameEventDispatcher) {
        gameEventDispatcher.subscribe(LegEvent.CURRENT_PLAYER_WON_LEG, this);
    }

    public int getHighestOut() {
        return highestOut;
    }


    @Override
    public void update(LegEvent event, LegDataProvider dataProvider) {
        // only receives updates for CURRENT_PLAYER_WON_LEG event
        int outshotRoundScore = dataProvider.getThrowsInLatestRound().stream()
                .mapToInt(DartThrow::getScore)
                .sum();
        highestOut = Math.max(highestOut, outshotRoundScore);
    }

}
