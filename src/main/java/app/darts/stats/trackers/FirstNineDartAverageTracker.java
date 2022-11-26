package app.darts.stats.trackers;

import app.darts.DartThrow;
import app.darts.DartsConstants;
import app.darts.stats.*;

import java.util.List;

public class FirstNineDartAverageTracker implements LegEventUser {
    private double scoreSum;
    private int throwCount;
    private int currentRoundScoreSum;

    public FirstNineDartAverageTracker(GameEventDispatcher gameEventDispatcher) {
        gameEventDispatcher.subscribe(LegEvent.CURRENT_PLAYER_THROW, this);
        gameEventDispatcher.subscribe(LegEvent.END_OF_CURRENT_PLAYER_ROUND, this);
    }

    public double getFirstNineDartAverage() {
        return scoreSum / throwCount * DartsConstants.THROWS_PER_ROUND;
    }


    @Override
    public void update(LegEvent event, LegDataProvider dataProvider) {
        if (event == LegEvent.CURRENT_PLAYER_THROW && dataProvider.getThrowsInLeg().size() <= 9) {
                int lastScore = dataProvider.getPreThrowScore() - dataProvider.getCurrentScore();
                scoreSum += lastScore;
                throwCount++;
                currentRoundScoreSum += lastScore;
        } else if (event == LegEvent.END_OF_CURRENT_PLAYER_ROUND && dataProvider.getThrowsInLeg().size() <=9) {
                List<DartThrow> lastRoundThrows = dataProvider.getThrowsInLatestRound();
                int scoreInRound = lastRoundThrows.stream()
                        .mapToInt(DartThrow::getScore)
                        .sum();
                scoreSum += scoreInRound;
                scoreSum -= currentRoundScoreSum;
                currentRoundScoreSum = 0;
        }

    }
}
