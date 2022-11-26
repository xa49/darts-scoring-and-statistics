package app.darts.stats;

import app.darts.DartThrow;

import java.util.List;

public interface LegDataProvider {

    List<DartThrow> getThrowsInLeg();

    List<DartThrow> getThrowsInLatestRound();

    List<DartThrow> getThrowsInCurrentRound();

    int getPreThrowScore();

    int getCurrentScore();

    static LegDataProvider getConstantProvider(int scorePreThrow, int currentScore, List<DartThrow> throwsInLeg, List<DartThrow> throwsInLatestRound) {
        return new LegDataProvider() {
            @Override
            public List<DartThrow> getThrowsInLeg() {
                return throwsInLeg;
            }

            @Override
            public List<DartThrow> getThrowsInLatestRound() {
                return throwsInLatestRound;
            }

            @Override
            public List<DartThrow> getThrowsInCurrentRound() {
                return null;
            }

            @Override
            public int getPreThrowScore() {
                return scorePreThrow;
            }

            @Override
            public int getCurrentScore() {
                return currentScore;
            }
        };
    }
}
