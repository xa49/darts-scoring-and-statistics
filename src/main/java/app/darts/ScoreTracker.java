package app.darts;

import app.darts.stats.GameEventDispatcher;
import app.darts.stats.LegDataProvider;
import app.darts.stats.LegEvent;
import lombok.Getter;

import java.util.*;
import java.util.stream.IntStream;

@Getter
public class ScoreTracker implements LegDataProvider {
    private final GameStyle gameStyle;
    private final String playerName;
    private final GameEventDispatcher eventDispatcher;
    private final Deque<DartThrow> throwsInLeg = new LinkedList<>();
    private final List<DartThrow> throwsInLatestRound = new ArrayList<>(DartsConstants.THROWS_PER_ROUND);
    private final List<DartThrow> throwsInCurrentRound = new ArrayList<>(DartsConstants.THROWS_PER_ROUND);
    private int preThrowScore;
    private int currentScore;
    private boolean endOfRound;

    public ScoreTracker(GameStyle gameStyle, String playerName, GameEventDispatcher eventDispatcher) {
        this.gameStyle = gameStyle;
        this.playerName = playerName;
        this.eventDispatcher = eventDispatcher;
        this.preThrowScore = gameStyle.getInitialScore();
        this.currentScore = gameStyle.getInitialScore();
    }

    public List<DartThrow> getThrowsInLeg() {
        return new ArrayList<>(throwsInLeg);
    }

    public boolean isWinner() {
        return currentScore == 0;
    }

    public void addThrow(DartThrow dartThrow) {
        endOfRound = false;
        preThrowScore = currentScore;
        currentScore -= dartThrow.getScore();
        throwsInCurrentRound.add(dartThrow);
        throwsInLeg.add(dartThrow);

        eventDispatcher.publish(LegEvent.CURRENT_PLAYER_THROW, this);

        if ((throwsInCurrentRound.size() == DartsConstants.THROWS_PER_ROUND
                || currentScore < DartsConstants.MINIMUM_CHECKOUT_VALUE)) {
            recordEndOfRound(dartThrow);
        }
    }

    public void recordLegLost() {
        eventDispatcher.publish(LegEvent.CURRENT_PLAYER_FINISHED_LEG, this);
    }

    private void recordEndOfRound(DartThrow lastThrow) {
        List<DartThrow> recordedThrows;
        if (currentScore == 0 &&  gameStyle.getOutshotStyle().qualifies(lastThrow)) {
            recordedThrows = getRecordableWinningThrows();
            closeRound(recordedThrows);
            eventDispatcher.publish(LegEvent.END_OF_CURRENT_PLAYER_ROUND, this);
            eventDispatcher.publish(LegEvent.CURRENT_PLAYER_WON_LEG, this);
            eventDispatcher.publish(LegEvent.CURRENT_PLAYER_FINISHED_LEG, this);

        } else if (currentScore >= DartsConstants.MINIMUM_CHECKOUT_VALUE) {
            recordedThrows = throwsInCurrentRound;
            closeRound(recordedThrows);
            eventDispatcher.publish(LegEvent.END_OF_CURRENT_PLAYER_ROUND, this);

        } else {
            recordedThrows = getRecordableBustedThrows();
            currentScore += getScore(throwsInCurrentRound); // reset score in case of bust
            closeRound(recordedThrows);
            eventDispatcher.publish(LegEvent.END_OF_CURRENT_PLAYER_ROUND, this);
        }

    }

    private void closeRound(List<DartThrow> recordedThrows) {
        throwsInLatestRound.clear();
        throwsInLatestRound.addAll(recordedThrows);
        IntStream.range(0, throwsInCurrentRound.size())
                .forEach(i -> throwsInLeg.pop());
        throwsInLeg.addAll(recordedThrows);
        throwsInCurrentRound.clear();
        endOfRound = true;
    }

    private List<DartThrow> getRecordableBustedThrows() {
        List<DartThrow> recordableThrows = new ArrayList<>();
        recordableThrows.addAll(
                Collections.nCopies(throwsInCurrentRound.size(), DartThrow.getNoScore()));
        recordableThrows.addAll(
                Collections.nCopies(DartsConstants.THROWS_PER_ROUND - throwsInCurrentRound.size(), DartThrow.getNoThrow()));

        return recordableThrows;
    }

    private List<DartThrow> getRecordableWinningThrows() {
        List<DartThrow> winningThrows = new ArrayList<>(throwsInCurrentRound);
        winningThrows.addAll(
                Collections.nCopies(DartsConstants.THROWS_PER_ROUND - throwsInCurrentRound.size(), DartThrow.getNoThrow()));

        return winningThrows;
    }

    private int getScore(List<DartThrow> dartThrows) {
        return dartThrows.stream()
                .mapToInt(DartThrow::getScore)
                .sum();
    }
}
