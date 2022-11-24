package app.darts;

import java.util.ArrayList;
import java.util.List;

public class DartGame {
    private Long id;
    private final int maxSetCount;
    private final OutshotStyle outshotStyle;
    private final int maxLegCount;
    private final int countFrom;
    private String currentPlayer;
    private String otherPlayer;
    private final List<DartSet> sets = new ArrayList<>();
    private String winningPlayer;

    public static DartGame from(Long id, int maxSetCount, OutshotStyle outshotStyle, int maxLegCount, int countFrom, String startingPlayer,
                                String otherPlayer) {
        if (maxSetCount % 2 == 0) {
            throw new IllegalArgumentException("Maximum total count of sets should be odd.");

        }
        return new DartGame(id, maxSetCount, outshotStyle, maxLegCount, countFrom, startingPlayer, otherPlayer);
    }

    private DartGame(Long id, int maxSetCount, OutshotStyle outshotStyle, int maxLegCount, int countFrom, String currentPlayer, String otherPlayer) {
        this.id = id;
        this.maxSetCount = maxSetCount;
        this.outshotStyle = outshotStyle;
        this.maxLegCount = maxLegCount;
        this.countFrom = countFrom;
        this.currentPlayer = currentPlayer;
        this.otherPlayer = otherPlayer;

        sets.add(DartSet.from(maxLegCount, outshotStyle, countFrom, currentPlayer, otherPlayer));
    }

    public boolean isOver() {
        return winningPlayer != null;
    }

    public String getWinningPlayer() {
        return winningPlayer;
    }

    public void  addThrow(DartThrow dartThrow) {
        if (isOver()) {
            throw new IllegalStateException("Cannot add throw to finished game.");
        }

        DartSet currentSet = sets.get(sets.size()-1);
        currentSet.addThrow(dartThrow);
        if (currentSet.isOver()) {
            recordEndOfSet();
        }
    }

    private void recordEndOfSet() {
        String lastSetWinner = sets.get(sets.size()-1).getWinningPlayer();
        int winningCount =(int) sets.stream().filter(s ->s.getWinningPlayer().equals(lastSetWinner))
                .count();
        if (winningCount == maxSetCount / 2 + 1) {
            winningPlayer = lastSetWinner;
        } else {
            String temp = currentPlayer;
            currentPlayer = otherPlayer;
            otherPlayer = temp;
            sets.add(DartSet.from(maxLegCount, outshotStyle, countFrom, currentPlayer, otherPlayer));
        }
    }
}
