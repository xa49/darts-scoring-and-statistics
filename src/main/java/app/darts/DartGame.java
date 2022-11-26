package app.darts;

import app.darts.stats.PlayerStatistics;
import app.darts.stats.PlayerStatisticsDto;

import java.util.*;
import java.util.stream.Collectors;

public class DartGame {

    private static long nextId = 1L;
    private final Map<String, PlayerStatistics> playerStatistics = new HashMap<>();
    private final Long id;
    private final GameStyle gameStyle;
    private String currentPlayer;
    private String otherPlayer;
    private final List<DartSet> sets = new ArrayList<>();
    private String winningPlayer;

    public static DartGame of(GameStyle gameStyle, String startingPlayer,
                              String otherPlayer) {
        return new DartGame(startingPlayer, otherPlayer, gameStyle);
    }

    private DartGame(String currentPlayer, String otherPlayer, GameStyle gameStyle) {
        this.id = nextId++;
        this.gameStyle = gameStyle;
        this.currentPlayer = currentPlayer;
        this.otherPlayer = otherPlayer;

        playerStatistics.put(currentPlayer, new PlayerStatistics(currentPlayer));
        playerStatistics.put(otherPlayer, new PlayerStatistics(otherPlayer));
        sets.add(DartSet.of(gameStyle, currentPlayer, otherPlayer, playerStatistics));
    }

    public boolean isOver() {
        return winningPlayer != null;
    }

    public Optional<String> getWinningPlayer() {
        return Optional.ofNullable(winningPlayer);
    }

    public Long getId() {
        return id;
    }

    public PlayerStatistics getPlayerStatistics(String name) {
        PlayerStatistics personalStatistics = playerStatistics.get(name);
        if (personalStatistics == null) {
            throw new IllegalArgumentException("No such player in game: " + name);
        }
        return personalStatistics;
    }

    public GameStyle getGameStyle() {
        return gameStyle;
    }

    public void addThrow(DartThrow dartThrow) {
        if (isOver()) {
            throw new IllegalStateException("Cannot add throw to finished game.");
        }

        DartSet currentSet = sets.get(sets.size() - 1);
        currentSet.addThrow(dartThrow);
        if (currentSet.isOver()) {
            recordEndOfSet();
        }
    }

    public int getSetsWonBy(String player) {
        return (int) sets.stream()
                .filter(s -> player.equals(s.getWinningPlayer()))
                .count();
    }

    public String getNextToThrow() {
        if (isOver()) {
            return "GAME OVER";
        }
        DartSet currentSet = sets.get(sets.size() - 1);
        return currentSet.getCurrentPlayer();
    }

    private void recordEndOfSet() {
        String lastSetWinner = sets.get(sets.size() - 1).getWinningPlayer();
        int winningCount = (int) sets.stream().filter(s -> s.getWinningPlayer().equals(lastSetWinner))
                .count();
        if (winningCount == gameStyle.getSets() / 2 + 1) {
            winningPlayer = lastSetWinner;
        } else {
            String temp = currentPlayer;
            currentPlayer = otherPlayer;
            otherPlayer = temp;
            sets.add(DartSet.of(gameStyle, currentPlayer, otherPlayer, playerStatistics));
        }
    }

    public List<PlayerStatisticsDto> getPlayerStatisticsDtos() {
        return playerStatistics.values().stream()
                .map(PlayerStatisticsDto::new)
                .toList();
    }

    public Map<String, Map<String, Integer>> getCurrentStanding() {
        Map<String, Integer> gameSets = playerStatistics.keySet().stream()
                .collect(Collectors.toMap(
                        p -> p,
                        this::getSetsWonBy
                ));
        Map<String, Integer> currentSetLegs = playerStatistics.keySet().stream()
                .collect(Collectors.toMap(
                        p -> p,
                        this::getLegsWonByInCurrentSet
                ));
        Map<String, Integer> currentScore = playerStatistics.keySet().stream()
                .collect(Collectors.toMap(
                        p -> p,
                        this::getScoreByInCurrentLeg
                ));
        return Map.of("scores current leg", currentScore,"legs won set", currentSetLegs,"sets won", gameSets);
     }
     public int getArrowsLeft() {
        if (isOver()) {
            return 0;
        }
        return sets.get(sets.size()-1).getArrowLeftForCurrentPlayer();
     }

    private int getLegsWonByInCurrentSet(String player) {
       return sets.get(sets.size()-1).getLegsBy(player);
    }

    private int getScoreByInCurrentLeg(String player) {
        List<DartLeg> currentLegs = sets.get(sets.size() - 1).getLegs();
        return currentLegs.get(currentLegs.size()-1).getScoreBy(player);
    }
}
