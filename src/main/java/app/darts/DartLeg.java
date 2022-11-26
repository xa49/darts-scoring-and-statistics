package app.darts;

import app.darts.stats.PlayerStatistics;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class DartLeg {
    private final List<ScoreTracker> scoreTrackers;
    private int throwingRound;

    public static DartLeg of(GameStyle gameStyle, String startingPlayer, String opponentPlayer,
                             Map<String, PlayerStatistics> playerStatistics) {
        List<ScoreTracker> scoreTrackers = List.of(
                new ScoreTracker(gameStyle, startingPlayer, playerStatistics.get(startingPlayer).getEventDispatcher()),
                new ScoreTracker(gameStyle, opponentPlayer, playerStatistics.get(opponentPlayer).getEventDispatcher())
        );
        return new DartLeg(scoreTrackers);
    }

    private DartLeg(List<ScoreTracker> scoreTrackers) {
        this.scoreTrackers = scoreTrackers;
    }

    public String getCurrentPlayer() {
        return scoreTrackers.get(throwingRound % scoreTrackers.size()).getPlayerName();
    }

    public int getArrowsLeftForCurrentPlayer() {
        ScoreTracker currentPlayerTracker = scoreTrackers.get(throwingRound % scoreTrackers.size());
        return DartsConstants.THROWS_PER_ROUND - currentPlayerTracker.getThrowsInCurrentRound().size();
    }

    public int getCurrentPlayerRunningTotal() {
        return scoreTrackers.get(throwingRound % scoreTrackers.size()).getCurrentScore();
    }

    public boolean isOver() {
        return scoreTrackers.stream().anyMatch(ScoreTracker::isWinner);
    }

    public Optional<String> getWinningPlayer() {
        return scoreTrackers.stream()
                .filter(ScoreTracker::isWinner)
                .map(ScoreTracker::getPlayerName)
                .findAny();
    }


    public void addThrow(DartThrow dartThrow) {
        if (isOver()) {
            throw new IllegalStateException("Cannot add throw to finished leg.");
        }

        ScoreTracker currentPlayerScores = scoreTrackers.get(throwingRound % scoreTrackers.size());
        currentPlayerScores.addThrow(dartThrow);

        if (currentPlayerScores.isWinner()) {
            scoreTrackers.get((throwingRound + 1) % scoreTrackers.size()).recordLegLost();
        } else if (currentPlayerScores.isEndOfRound()) {
            throwingRound++;
        }

    }

    public int getScoreBy(String player) {
        return player.equals(getCurrentPlayer()) ? getCurrentPlayerRunningTotal()
                : scoreTrackers.get(scoreTrackers.size() - (throwingRound % scoreTrackers.size() + 1)).getCurrentScore();
    }
}
