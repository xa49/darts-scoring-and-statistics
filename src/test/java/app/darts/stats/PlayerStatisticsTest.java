package app.darts.stats;

import app.darts.DartThrow;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlayerStatisticsTest {

    final GameEventDispatcher gameEventDispatcher = new ConcreteGameEventDispatcher();
    final PlayerStatistics playerStatistics = new PlayerStatistics("player", gameEventDispatcher);

    @Test
    void startingValuesAsExpected() {
        assertEquals(0, playerStatistics.getLegsPlayedCount());
        assertEquals(0, playerStatistics.getLegsWonCount());
        assertEquals(Double.NaN, playerStatistics.getAverage());
        assertEquals(Double.NaN, playerStatistics.getFirstNineDartAverage());
        assertEquals(0, playerStatistics.getOneEightiesCount());
        assertEquals(0, playerStatistics.getTonPlusCount());
        assertEquals(0, playerStatistics.getNineDarterCount());
        assertEquals(0, playerStatistics.getHighestOut());
        assertEquals(0, playerStatistics.getOutshotAttemptCount());
        assertEquals(Double.NaN, playerStatistics.getOutshotEfficiency());
        assertTrue(playerStatistics.getTonPlusOuts().isEmpty());

    }

    @Test
    void shortFormatTests_ignoringNineDartTypeTrackers() {
        // Testing: Average tracker, 180 tracker, ton-plus round tracker, outshot attempt tracker, legs played, legs won tracker, highest out tracker (partial)
        // First throw: T20
        gameEventDispatcher.publish(LegEvent.CURRENT_PLAYER_THROW,
                LegDataProvider.getConstantProvider(282, 222,
                        List.of(DartThrow.of("t20")), List.of(DartThrow.of("t20"))));
        assertEquals(180, playerStatistics.getAverage());

        // Second throw: T20
        gameEventDispatcher.publish(LegEvent.CURRENT_PLAYER_THROW,
                LegDataProvider.getConstantProvider(222, 162,
                        Collections.nCopies(2, DartThrow.of("t20")), Collections.nCopies(2, DartThrow.of("t20"))));

        // Third throw: T20. End of valid turn
        gameEventDispatcher.publish(LegEvent.CURRENT_PLAYER_THROW,
                LegDataProvider.getConstantProvider(162, 102,
                        Collections.nCopies(3, DartThrow.of("t20")), Collections.nCopies(3, DartThrow.of("t20"))));

        // End round
        gameEventDispatcher.publish(LegEvent.END_OF_CURRENT_PLAYER_ROUND,
                LegDataProvider.getConstantProvider(162, 102,
                        Collections.nCopies(3, DartThrow.of("t20")), Collections.nCopies(3, DartThrow.of("t20"))));
        assertEquals(180, playerStatistics.getAverage());
        assertEquals(1, playerStatistics.getOneEightiesCount());
        assertEquals(1, playerStatistics.getTonPlusCount());

        // Second round first throw: 20
        gameEventDispatcher.publish(LegEvent.CURRENT_PLAYER_THROW,
                LegDataProvider.getConstantProvider(102, 82,
                        List.of(DartThrow.of("t20"), DartThrow.of("t20"), DartThrow.of("t20"), DartThrow.of("20")),
                        List.of(DartThrow.of("20"))));
        assertEquals(150, playerStatistics.getAverage());

        // Second throw: 20
        gameEventDispatcher.publish(LegEvent.CURRENT_PLAYER_THROW,
                LegDataProvider.getConstantProvider(82, 62,
                        List.of(DartThrow.of("t20"), DartThrow.of("t20"), DartThrow.of("t20"), DartThrow.of("20"), DartThrow.of("20")),
                        List.of(DartThrow.of("20"), DartThrow.of("20"))));
        assertEquals(132, playerStatistics.getAverage());

        // Third throw: T20
        gameEventDispatcher.publish(LegEvent.CURRENT_PLAYER_THROW,
                LegDataProvider.getConstantProvider(62, 2,
                        List.of(DartThrow.of("t20"), DartThrow.of("t20"), DartThrow.of("t20"), DartThrow.of("20"), DartThrow.of("20"), DartThrow.of("t20")),
                        List.of(DartThrow.of("20"), DartThrow.of("20"), DartThrow.of("t20"))));
        assertEquals(140, playerStatistics.getAverage());

        // End of second round
        gameEventDispatcher.publish(LegEvent.END_OF_CURRENT_PLAYER_ROUND,
                LegDataProvider.getConstantProvider(62, 2,
                        List.of(DartThrow.of("t20"), DartThrow.of("t20"), DartThrow.of("t20"), DartThrow.of("20"), DartThrow.of("20"), DartThrow.of("t20")),
                        List.of(DartThrow.of("20"), DartThrow.of("20"), DartThrow.of("t20"))));
        assertEquals(1, playerStatistics.getOneEightiesCount());
        assertEquals(2, playerStatistics.getTonPlusCount());

        // Other trackers are still zero
        assertEquals(0, playerStatistics.getLegsWonCount());
        assertEquals(0, playerStatistics.getLegsPlayedCount());
        assertEquals(0, playerStatistics.getHighestOut());
        assertEquals(0, playerStatistics.getOutshotAttemptCount());
        assertTrue(playerStatistics.getTonPlusOuts().isEmpty());

        // Third round first throw: miss
        gameEventDispatcher.publish(LegEvent.CURRENT_PLAYER_THROW,
                LegDataProvider.getConstantProvider(2, 2,
                        List.of(DartThrow.of("t20"), DartThrow.of("t20"), DartThrow.of("t20"), DartThrow.of("20"), DartThrow.of("20"), DartThrow.of("t20"), DartThrow.of("x")),
                        List.of(DartThrow.of("x"))));
        assertEquals(120, playerStatistics.getAverage());
        assertEquals(1, playerStatistics.getOutshotAttemptCount());

        // Second throw outshot: d1
        gameEventDispatcher.publish(LegEvent.CURRENT_PLAYER_THROW,
                LegDataProvider.getConstantProvider(2, 0,
                        List.of(DartThrow.of("t20"), DartThrow.of("t20"), DartThrow.of("t20"), DartThrow.of("20"), DartThrow.of("20"), DartThrow.of("t20"), DartThrow.of("x"), DartThrow.of("d1")),
                        List.of(DartThrow.of("x"), DartThrow.of("d1"))));
        gameEventDispatcher.publish(LegEvent.END_OF_CURRENT_PLAYER_ROUND,
                LegDataProvider.getConstantProvider(2, 0,
                        List.of(DartThrow.of("t20"), DartThrow.of("t20"), DartThrow.of("t20"), DartThrow.of("20"), DartThrow.of("20"), DartThrow.of("t20"), DartThrow.of("x"), DartThrow.of("d1")),
                        List.of(DartThrow.of("x"), DartThrow.of("d1"))));
        gameEventDispatcher.publish(LegEvent.CURRENT_PLAYER_WON_LEG,
                LegDataProvider.getConstantProvider(2, 0,
                        List.of(DartThrow.of("t20"), DartThrow.of("t20"), DartThrow.of("t20"), DartThrow.of("20"), DartThrow.of("20"), DartThrow.of("t20"), DartThrow.of("x"), DartThrow.of("d1")),
                        List.of(DartThrow.of("x"), DartThrow.of("d1"))));
        gameEventDispatcher.publish(LegEvent.CURRENT_PLAYER_FINISHED_LEG,
                LegDataProvider.getConstantProvider(2, 0,
                        List.of(DartThrow.of("t20"), DartThrow.of("t20"), DartThrow.of("t20"), DartThrow.of("20"), DartThrow.of("20"), DartThrow.of("t20"), DartThrow.of("x"), DartThrow.of("d1")),
                        List.of(DartThrow.of("x"), DartThrow.of("d1"))));


        assertEquals(105.75, playerStatistics.getAverage(), 0.00000001);
        assertEquals(1, playerStatistics.getLegsPlayedCount());
        assertEquals(1, playerStatistics.getLegsWonCount());
        assertEquals(1, playerStatistics.getOneEightiesCount());
        assertEquals(2, playerStatistics.getTonPlusCount());
        assertEquals(2, playerStatistics.getHighestOut());
        assertTrue(playerStatistics.getTonPlusOuts().isEmpty());
        assertEquals(2, playerStatistics.getOutshotAttemptCount());
        assertEquals(50, playerStatistics.getOutshotEfficiency());

    }

    @Test
    void legPlayedButNotWon() {
        gameEventDispatcher.publish(LegEvent.CURRENT_PLAYER_FINISHED_LEG,
                LegDataProvider.getConstantProvider(0, 0, null, null));
        assertEquals(1, playerStatistics.getLegsPlayedCount());
        assertEquals(0, playerStatistics.getLegsWonCount());
    }

    @Test
    void tonPlusFinish_overriddenWithSecondOne() {
        // First throw: T20
        gameEventDispatcher.publish(LegEvent.CURRENT_PLAYER_THROW,
                LegDataProvider.getConstantProvider(160, 100,
                        List.of(DartThrow.of("t20")), List.of(DartThrow.of("t20"))));

        // Second throw: T20
        gameEventDispatcher.publish(LegEvent.CURRENT_PLAYER_THROW,
                LegDataProvider.getConstantProvider(100, 40,
                        Collections.nCopies(2, DartThrow.of("t20")), Collections.nCopies(2, DartThrow.of("t20"))));

        // Third throw: D20. Outshot
        gameEventDispatcher.publish(LegEvent.CURRENT_PLAYER_THROW,
                LegDataProvider.getConstantProvider(40, 0,
                        List.of(DartThrow.of("t20"), DartThrow.of("t20"), DartThrow.of("d20")),
                        List.of(DartThrow.of("t20"), DartThrow.of("t20"), DartThrow.of("d20"))));
        gameEventDispatcher.publish(LegEvent.END_OF_CURRENT_PLAYER_ROUND,
                LegDataProvider.getConstantProvider(40, 0,
                        List.of(DartThrow.of("t20"), DartThrow.of("t20"), DartThrow.of("d20")),
                        List.of(DartThrow.of("t20"), DartThrow.of("t20"), DartThrow.of("d20"))));
        gameEventDispatcher.publish(LegEvent.CURRENT_PLAYER_WON_LEG,
                LegDataProvider.getConstantProvider(40, 0,
                        List.of(DartThrow.of("t20"), DartThrow.of("t20"), DartThrow.of("d20")),
                        List.of(DartThrow.of("t20"), DartThrow.of("t20"), DartThrow.of("d20"))));

        assertEquals(List.of(160), playerStatistics.getTonPlusOuts());
        assertEquals(160, playerStatistics.getHighestOut());


        // First throw: T20
        gameEventDispatcher.publish(LegEvent.CURRENT_PLAYER_THROW,
                LegDataProvider.getConstantProvider(170, 110,
                        List.of(DartThrow.of("t20")), List.of(DartThrow.of("t20"))));

        // Second throw: T20
        gameEventDispatcher.publish(LegEvent.CURRENT_PLAYER_THROW,
                LegDataProvider.getConstantProvider(110, 50,
                        Collections.nCopies(2, DartThrow.of("t20")), Collections.nCopies(2, DartThrow.of("t20"))));

        // Third throw: D20. Outshot
        gameEventDispatcher.publish(LegEvent.CURRENT_PLAYER_THROW,
                LegDataProvider.getConstantProvider(50, 0,
                        List.of(DartThrow.of("t20"), DartThrow.of("t20"), DartThrow.of("50")),
                        List.of(DartThrow.of("t20"), DartThrow.of("t20"), DartThrow.of("50"))));
        gameEventDispatcher.publish(LegEvent.END_OF_CURRENT_PLAYER_ROUND,
                LegDataProvider.getConstantProvider(40, 0,
                        List.of(DartThrow.of("t20"), DartThrow.of("t20"), DartThrow.of("50")),
                        List.of(DartThrow.of("t20"), DartThrow.of("t20"), DartThrow.of("50"))));
        gameEventDispatcher.publish(LegEvent.CURRENT_PLAYER_WON_LEG,
                LegDataProvider.getConstantProvider(40, 0,
                        List.of(DartThrow.of("t20"), DartThrow.of("t20"), DartThrow.of("50")),
                        List.of(DartThrow.of("t20"), DartThrow.of("t20"), DartThrow.of("50"))));

        assertEquals(List.of(160, 170), playerStatistics.getTonPlusOuts());
        assertEquals(170, playerStatistics.getHighestOut());

    }



}