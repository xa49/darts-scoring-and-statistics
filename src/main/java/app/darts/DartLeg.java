package app.darts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DartLeg {
    private static final int THROWS_PER_ROUND = 3;
    private final DartFinishStyle dartFinishStyle;
    private final Map<String, Integer> scoresPreThrow;
    private final List<DartThrow> currentPlayerThrowsInRound = new ArrayList<>(THROWS_PER_ROUND);
    private String currentPlayer;
    private String otherPlayer;
    private final Map<String, List<DartThrow>> allThrows = new HashMap<>();
    private String winningPlayer;
    private int currentPlayerRunningTotal;

    public static DartLeg from(DartFinishStyle dartFinishStyle, String startingPlayer, String opponentPlayer, int startingScore) {
        Map<String, Integer> currentScores = new HashMap<>(Map.of(startingPlayer, startingScore, opponentPlayer, startingScore));
        return new DartLeg(dartFinishStyle, currentScores, startingPlayer, opponentPlayer, startingScore);
    }

    private DartLeg(DartFinishStyle dartFinishStyle, Map<String, Integer> scoresPreThrow, String currentPlayer,
                    String otherPlayer, int currentPlayerRunningTotal) {
        this.dartFinishStyle = dartFinishStyle;
        this.scoresPreThrow = scoresPreThrow;
        this.currentPlayer = currentPlayer;
        this.otherPlayer = otherPlayer;
        this.currentPlayerRunningTotal = currentPlayerRunningTotal;
    }

    public String getWinningPlayer() {
        return winningPlayer;
    }

    public String getCurrentPlayer() {
        return currentPlayer;
    }

    public boolean isOver() {
        return winningPlayer !=  null;
    }

    public Map<String, Integer> getScoresPreThrow() {
        return scoresPreThrow;
    }

    public List<DartThrow> getCurrentPlayerThrowsInRound() {
        return currentPlayerThrowsInRound;
    }

    public Map<String, List<DartThrow>> getAllThrows() {
        return allThrows;
    }

    public int getCurrentPlayerRunningTotal() {
        return currentPlayerRunningTotal;
    }

    public void addThrow(DartThrow dartThrow) {
        if (isOver()) {
            throw new IllegalStateException("CANNOT ADD THROW TO FINISHED LEG");
        }

        currentPlayerRunningTotal -= dartThrow.getScore();
        allThrows.computeIfAbsent(currentPlayer, l -> new ArrayList<>()).add(dartThrow);
        currentPlayerThrowsInRound.add(dartThrow);
        if ( currentPlayerRunningTotal == 0 && dartFinishStyle.qualifiesForWinningThrow(dartThrow)) {
            winningPlayer = currentPlayer;
        } else if (currentPlayerThrowsInRound.size() == THROWS_PER_ROUND) {
            recordEndOfTurn();
        }
    }

    private void recordEndOfTurn() { // cannot edit last throw of round if immediate recording of end
        if (currentPlayerRunningTotal > 0) {
            scoresPreThrow.put(currentPlayer, currentPlayerRunningTotal);
        }
        String temp = currentPlayer;
        currentPlayer = otherPlayer;
        otherPlayer = temp;
        currentPlayerRunningTotal = scoresPreThrow.get(currentPlayer);
        currentPlayerThrowsInRound.clear();
    }


}
