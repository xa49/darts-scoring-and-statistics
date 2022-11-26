package app.darts.stats.trackers;

import app.darts.DartThrow;
import app.darts.stats.LegDataProvider;
import app.darts.stats.GameEventDispatcher;
import app.darts.stats.LegEvent;
import app.darts.stats.LegEventUser;

import java.util.List;

public class RoundScoreTracker implements LegEventUser {

    private final int threshold;
    private int throwsReachingThresholdCount;

    public RoundScoreTracker(GameEventDispatcher gameEventDispatcher, int threshold) {
        gameEventDispatcher.subscribe(LegEvent.END_OF_CURRENT_PLAYER_ROUND, this);
        this.threshold = threshold;
    }

    public int getThrowsReachingThresholdCount() {
        return throwsReachingThresholdCount;
    }


    @Override
    public void update(LegEvent event, LegDataProvider dataProvider) {
        List<DartThrow> dartThrows = dataProvider.getThrowsInLatestRound();
        int sumOfScoresInRound = dartThrows.stream()
                .mapToInt(DartThrow::getScore)
                .sum();
        if (sumOfScoresInRound >= threshold) {
            throwsReachingThresholdCount++;
        }
    }
}
