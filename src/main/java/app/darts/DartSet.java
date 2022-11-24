package app.darts;

import java.util.ArrayList;
import java.util.List;

public class DartSet {
    private final OutshotStyle outshotStyle;
    private final int maxLegCount;
    private final int countFrom;
    private String currentPlayer;
    private String otherPlayer;
    private final List<DartLeg> legs = new ArrayList<>();
    private String winningPlayer;

    public static DartSet from(int maxLegCount, OutshotStyle outshotStyle, int countFrom, String startingPlayer, String opponent) {
        if (maxLegCount % 2 == 0) {
            throw new IllegalArgumentException("Maximum total leg count must be odd.");
        }
        return new DartSet(outshotStyle, maxLegCount, countFrom, startingPlayer, opponent);
    }

    private DartSet(OutshotStyle outshotStyle, int maxLegCount, int countFrom, String currentPlayer, String otherPlayer) {
        this.outshotStyle = outshotStyle;
        this.maxLegCount = maxLegCount;
        this.countFrom = countFrom;
        this.currentPlayer = currentPlayer;
        this.otherPlayer = otherPlayer;

        legs.add(DartLeg.from(outshotStyle, currentPlayer, otherPlayer, countFrom));
    }

    public boolean isOver() {
        return winningPlayer != null;
    }

    public String getWinningPlayer() {
        return winningPlayer;
    }

    public List<DartLeg> getLegs() {
        return legs;
    }

    public String getCurrentPlayer() {
        return currentPlayer;
    }

    public void addThrow(DartThrow dartThrow) {
        if (isOver()) {
            throw new IllegalStateException("Cannot add throw to finished set.");
        }
        DartLeg currentLeg = legs.get(legs.size()-1);
        currentLeg.addThrow(dartThrow);
        if (currentLeg.isOver()) {
            recordLegOver();
        }
    }

    private void recordLegOver() {
        String  legWinner = legs.get(legs.size()-1).getWinningPlayer();
        int winningsOfLegWinner = (int) legs.stream()
                .filter(l -> l.getWinningPlayer().equals(legWinner))
                .count();

       if (winningsOfLegWinner == (maxLegCount / 2 + 1)) {
           winningPlayer = legWinner;
       } else {
           String temp = currentPlayer;
           currentPlayer = otherPlayer;
           otherPlayer = temp;
           legs.add(DartLeg.from(outshotStyle, currentPlayer, otherPlayer, countFrom));
       }

    }
}
