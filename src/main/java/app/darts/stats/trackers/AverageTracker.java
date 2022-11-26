package app.darts.stats.trackers;

import app.darts.DartThrow;
import app.darts.DartsConstants;
import app.darts.stats.*;

import java.util.List;

public class AverageTracker implements LegEventUser {

    private double scoreSum;
    private int throwCount;
    private int previousScoresInRound;

    public AverageTracker(GameEventDispatcher gameEventDispatcher) {
        gameEventDispatcher.subscribe(LegEvent.CURRENT_PLAYER_THROW, this);
        gameEventDispatcher.subscribe(LegEvent.END_OF_CURRENT_PLAYER_ROUND, this);
    }

    public double getAverage() {
        return scoreSum / throwCount * DartsConstants.THROWS_PER_ROUND;
    }

    @Override
    public void update(LegEvent event, LegDataProvider dataProvider) {
        if (event == LegEvent.CURRENT_PLAYER_THROW) {
            int lastThrowScore = dataProvider.getPreThrowScore() - dataProvider.getCurrentScore();
            scoreSum += lastThrowScore;
            throwCount++;
            previousScoresInRound += lastThrowScore;
        } else if (event == LegEvent.END_OF_CURRENT_PLAYER_ROUND) {
            List<DartThrow> recordedThrows = dataProvider.getThrowsInLatestRound();
            int totalScoresInRound = recordedThrows.stream()
                    .mapToInt(DartThrow::getScore)
                    .sum();
            scoreSum += (totalScoresInRound - previousScoresInRound); // if busts, removing previously recorded scores
            previousScoresInRound = 0;
        }
    }
}
