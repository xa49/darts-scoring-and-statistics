package app.darts;

import app.darts.stats.PlayerStatistics;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class DartLegTest {

    final DartLeg leg501;
    final Map<String, PlayerStatistics> playerStatistics = Map.of("starting", new PlayerStatistics("starting"),
            "opponent", new PlayerStatistics("opponent"));

    final GameStyle gameStyle501 = GameStyle.builder()
            .initialScore(501)
            .sets(1)
            .legsPerSet(1)
            .outshotStyle(OutshotStyle.DOUBLE_OR_INNER_BULL_OUT)
            .build();

    final GameStyle gameStyle100 = GameStyle.builder()
            .initialScore(100)
            .sets(1)
            .legsPerSet(1)
            .outshotStyle(OutshotStyle.DOUBLE_OR_INNER_BULL_OUT)
            .build();


    {
        leg501 = DartLeg.of(gameStyle501, "starting", "opponent", playerStatistics);
    }

    @Test
    void currentPlayerSetCorrectly() {

        assertEquals("starting", leg501.getCurrentPlayer());
    }

    @Test
    void playersRotateIfGameNotEnded() {
        assertEquals("starting", leg501.getCurrentPlayer());
        leg501.addThrow(DartThrow.of("20"));
        assertEquals("starting", leg501.getCurrentPlayer());
        leg501.addThrow(DartThrow.of("20"));
        assertEquals("starting", leg501.getCurrentPlayer());
        leg501.addThrow(DartThrow.of("20"));
        assertEquals("opponent", leg501.getCurrentPlayer());
    }


    @Test
    void scoreDeductedFromBothPlayers() {
        // Starting player throws
        leg501.addThrow(DartThrow.of("20"));
        leg501.addThrow(DartThrow.of("t20"));
        assertEquals(421, leg501.getCurrentPlayerRunningTotal());
        leg501.addThrow(DartThrow.of("25"));

        // Non-starting player throws
        leg501.addThrow(DartThrow.of("t19"));
        leg501.addThrow(DartThrow.of("t19"));
        assertEquals(387, leg501.getCurrentPlayerRunningTotal());

    }

    @Test
    void startingPlayerCanWin() {

        DartLeg leg = DartLeg.of(gameStyle100, "starting", "opponent", playerStatistics);
        // Starting player throws
        leg.addThrow(DartThrow.of("t20"));
        leg.addThrow(DartThrow.of("d20"));

        assertEquals("starting", leg.getWinningPlayer().orElse("not starting"));
    }

    @Test
    void nonStartingPlayerCanWin() {
        DartLeg leg = DartLeg.of(gameStyle100, "starting", "opponent", playerStatistics);
        // Starting player throws
        leg.addThrow(DartThrow.of("1"));
        leg.addThrow(DartThrow.of("1"));
        leg.addThrow(DartThrow.of("1"));

        // Non-starting player throws
        leg.addThrow(DartThrow.of("10"));
        leg.addThrow(DartThrow.of("d20"));
        leg.addThrow(DartThrow.of("50"));

        assertEquals("opponent", leg.getWinningPlayer().orElse("not opponent"));
    }

    @Test
    void cannotAddThrowToFinishedLeg() {
        DartLeg leg = DartLeg.of(gameStyle100, "starting", "opponent", playerStatistics);
        // Starting player throws
        leg.addThrow(DartThrow.of("t20"));
        leg.addThrow(DartThrow.of("d20"));

        IllegalStateException ex = assertThrows(IllegalStateException.class,
                () -> leg.addThrow(DartThrow.of("1")));
        assertEquals("Cannot add throw to finished leg.", ex.getMessage());
    }

    @Test
    void noScoreIfPlayerReachedZeroWithoutFinishingCondition() {
        DartLeg leg = DartLeg.of(gameStyle100, "starting", "opponent", playerStatistics);
        // Starting player throws
        leg.addThrow(DartThrow.of("t20"));
        leg.addThrow(DartThrow.of("20"));
        leg.addThrow(DartThrow.of("20"));

        assertTrue(leg.getWinningPlayer().isEmpty());
        assertEquals("opponent", leg.getCurrentPlayer());
        assertEquals(100, leg.getCurrentPlayerRunningTotal());
    }

    @Test
    void nextPlayerUpIfBustsWithOneLeft() {
        GameStyle gameStyle = GameStyle.builder()
                .initialScore(39)
                .sets(1)
                .legsPerSet(1)
                .outshotStyle(OutshotStyle.DOUBLE_OR_INNER_BULL_OUT)
                .build();
        DartLeg leg = DartLeg.of(gameStyle, "starting", "opponent", playerStatistics);
        leg.addThrow(DartThrow.of("d19")); // bust
        assertEquals("opponent", leg.getCurrentPlayer());
    }

    @Test
    void nineDarterIsRecordedCorrectly() {
        // Starting player down to 321
        leg501.addThrow(DartThrow.of("t20"));
        leg501.addThrow(DartThrow.of("t20"));
        leg501.addThrow(DartThrow.of("t20"));

        leg501.addThrow(DartThrow.of("t10"));
        leg501.addThrow(DartThrow.of("t10"));
        leg501.addThrow(DartThrow.of("t10"));

        // Starting player down to 141
        leg501.addThrow(DartThrow.of("t20"));
        leg501.addThrow(DartThrow.of("t20"));
        leg501.addThrow(DartThrow.of("t20"));

        leg501.addThrow(DartThrow.of("t10"));
        leg501.addThrow(DartThrow.of("t10"));
        leg501.addThrow(DartThrow.of("t10"));

        // Starting player outshot
        leg501.addThrow(DartThrow.of("t20"));
        leg501.addThrow(DartThrow.of("t19"));
        leg501.addThrow(DartThrow.of("d12"));

        assertEquals("starting", leg501.getWinningPlayer().orElse("not starting"));
    }


}