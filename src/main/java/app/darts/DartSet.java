package app.darts;

import app.darts.stats.PlayerStatistics;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DartSet {
    private final  Map<String, PlayerStatistics> playerStatistics;
    private final GameStyle gameStyle;
    private String currentPlayer;
    private String otherPlayer;
    private final List<DartLeg> legs = new ArrayList<>();
    private String winningPlayer;

    public static DartSet of(GameStyle gameStyle, String startingPlayer, String opponent,
                             Map<String, PlayerStatistics> playerStatistics) {
        return new DartSet(gameStyle, startingPlayer, opponent, playerStatistics);
    }

    private DartSet(GameStyle gameStyle, String currentPlayer, String otherPlayer,
                    Map<String, PlayerStatistics> playerStatistics) {
        this.gameStyle = gameStyle;
        this.currentPlayer = currentPlayer;
        this.otherPlayer = otherPlayer;
        this.playerStatistics = playerStatistics;

        legs.add(DartLeg.of(gameStyle, currentPlayer, otherPlayer, playerStatistics));
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
        return legs.get(legs.size()-1).getCurrentPlayer();
    }
    public int getArrowLeftForCurrentPlayer() {
        return legs.get(legs.size()-1).getArrowsLeftForCurrentPlayer();
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
        String  legWinner = legs.get(legs.size()-1).getWinningPlayer().orElseThrow(() -> new IllegalStateException("No winner found despite end of leg."));
        int winningsOfLegWinner = (int) legs.stream()
                .filter(l -> l.getWinningPlayer().isPresent() && l.getWinningPlayer().get().equals(legWinner))
                .count();

       if (winningsOfLegWinner == (gameStyle.getLegsPerSet() / 2 + 1)) {
           winningPlayer = legWinner;
       } else {
           String temp = currentPlayer;
           currentPlayer = otherPlayer;
           otherPlayer = temp;
           legs.add(DartLeg.of(gameStyle, currentPlayer, otherPlayer, playerStatistics));
       }

    }

    public int getLegsBy(String player) {
        return  (int) legs.stream()
                .filter(l -> l.getWinningPlayer().isPresent() && player.equals(l.getWinningPlayer().get()))
                .count();
    }
}
