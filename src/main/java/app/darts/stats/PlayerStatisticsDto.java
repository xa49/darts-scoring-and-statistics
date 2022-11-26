package app.darts.stats;

import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public class PlayerStatisticsDto {
    private final String playerName;
    private final int legsPlayed;
    private final int legsWon;
    private final double average;
    private final double firstNineDartAverage;
    private final int oneEighties;
    private final int tonPluses;
    private final int nineDarters;
    private final int highestOut;
    private final List<Integer> tonPlusOuts;
    private final double outshotEfficiency;

    public PlayerStatisticsDto(PlayerStatistics statistics) {
        this.playerName = statistics.getPlayerName();
        this.legsPlayed = statistics.getLegsPlayedCount();
        this.legsWon = statistics.getLegsWonCount();
        this.average = statistics.getAverage();
        this.firstNineDartAverage = statistics.getFirstNineDartAverage();
        this.oneEighties = statistics.getOneEightiesCount();
        this.tonPluses = statistics.getTonPlusCount();
        this.nineDarters = statistics.getNineDarterCount();
        this.highestOut = statistics.getHighestOut();
        this.tonPlusOuts = statistics.getTonPlusOuts();
        this.outshotEfficiency = statistics.getOutshotEfficiency();
    }
}
