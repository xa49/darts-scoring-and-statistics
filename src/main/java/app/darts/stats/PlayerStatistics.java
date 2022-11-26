package app.darts.stats;

import app.darts.stats.trackers.*;

import java.util.List;

public class PlayerStatistics {
    private final String playerName;
    private final GameEventDispatcher eventDispatcher;
    private final LegsPlayedTracker legsPlayedTracker;
    private final LegsWonTracker legsWonTracker;
    private final AverageTracker averageTracker;
    private final FirstNineDartAverageTracker firstNineDartAverageTracker;
    private final RoundScoreTracker oneEightyTracker;
    private final RoundScoreTracker tonPlusTracker;
    private final NineDarterTracker nineDarterTracker;
    private final HighestOutTracker highestOutTracker;
    private final TonPlusOutsTracker tonPlusOutsTracker;
    private final OutshotAttemptTracker outshotAttemptTracker;

    public PlayerStatistics(String playerName) {
        this(playerName, new ConcreteGameEventDispatcher());
    }

    public PlayerStatistics(String playerName, GameEventDispatcher gameEventDispatcher) {
        this.playerName = playerName;
        this.eventDispatcher = gameEventDispatcher;
        this.legsPlayedTracker = new LegsPlayedTracker(gameEventDispatcher);
        this.legsWonTracker = new LegsWonTracker(gameEventDispatcher);
        this.averageTracker = new AverageTracker(gameEventDispatcher);
        this.firstNineDartAverageTracker = new FirstNineDartAverageTracker(gameEventDispatcher);
        this.oneEightyTracker = new RoundScoreTracker(gameEventDispatcher, 180);
        this.tonPlusTracker = new RoundScoreTracker(gameEventDispatcher, 100);
        this.nineDarterTracker = new NineDarterTracker(gameEventDispatcher);
        this.highestOutTracker = new HighestOutTracker(gameEventDispatcher);
        this.tonPlusOutsTracker = new TonPlusOutsTracker(gameEventDispatcher);
        this.outshotAttemptTracker = new OutshotAttemptTracker(gameEventDispatcher);
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getLegsPlayedCount() {
        return legsPlayedTracker.getLegsPlayedCount();
    }

    public int getLegsWonCount() {
        return legsWonTracker.getLegsWonCount();
    }

    public double getAverage() {
        return averageTracker.getAverage();
    }

    public double getFirstNineDartAverage() {
        return firstNineDartAverageTracker.getFirstNineDartAverage();
    }

    public int getOneEightiesCount() {
        return oneEightyTracker.getThrowsReachingThresholdCount();
    }

    public int getTonPlusCount() {
        return tonPlusTracker.getThrowsReachingThresholdCount();
    }

    public int getNineDarterCount() {
        return nineDarterTracker.getNineDarterCount();
    }

    public int getOutshotAttemptCount() {
        return outshotAttemptTracker.getOutshotAttemptCount();
    }

    public int getHighestOut() {
        return highestOutTracker.getHighestOut();
    }

    public List<Integer> getTonPlusOuts(){
        return tonPlusOutsTracker.getTonPlusOuts();
    }

    public double getOutshotEfficiency() {
        return getLegsWonCount() * 100.0 / getOutshotAttemptCount();
    }

    public GameEventDispatcher getEventDispatcher() {
        return eventDispatcher;
    }
}
