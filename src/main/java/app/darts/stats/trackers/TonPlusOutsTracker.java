package app.darts.stats.trackers;

import app.darts.DartThrow;
import app.darts.DartsConstants;
import app.darts.stats.LegDataProvider;
import app.darts.stats.GameEventDispatcher;
import app.darts.stats.LegEvent;
import app.darts.stats.LegEventUser;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class TonPlusOutsTracker implements LegEventUser {
    private final List<Integer> tonPlusOuts = new ArrayList<>();

    public TonPlusOutsTracker(GameEventDispatcher gameEventDispatcher) {
        gameEventDispatcher.subscribe(LegEvent.CURRENT_PLAYER_WON_LEG, this);
    }

    public List<Integer> getTonPlusOuts() {
        return tonPlusOuts;
    }

    @Override
    public void update(LegEvent event, LegDataProvider dataProvider) {
        int scoreOut = dataProvider.getThrowsInLatestRound().stream()
                .mapToInt(DartThrow::getScore)
                .sum();
        if (scoreOut >= DartsConstants.TON_PLUS_THRESHOLD) {
            tonPlusOuts.add(scoreOut);
            tonPlusOuts.sort(Comparator.naturalOrder());
        }
    }
}
