package app.darts;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DartSetTest {

    @Test
    void startingPlayerIsMarkedStarter() {
        DartSet dartSet = DartSet.from(5, OutshotStyle.DOUBLE_OR_INNER_BULL_OUT, 501, "starting", "opponent");
        assertEquals("starting", dartSet.getCurrentPlayer());
    }

    @Test
    void cannotAddThrowToFinishedSet() {
        DartSet dartSet = DartSet.from(1, OutshotStyle.DOUBLE_OR_INNER_BULL_OUT, 40, "starting", "opponent");
        dartSet.addThrow(DartThrow.of("d20"));

        IllegalStateException ex = assertThrows(IllegalStateException.class,
                () -> dartSet.addThrow(DartThrow.of("10")));
        assertEquals("Cannot add throw to finished set.", ex.getMessage());
    }

    @Test
    void throwsAreAddedToTheObject() {
        DartSet dartSet = DartSet.from(1, OutshotStyle.DOUBLE_OR_INNER_BULL_OUT, 100, "starting", "opponent");
        dartSet.addThrow(DartThrow.of("20"));
        assertEquals(80, dartSet.getLegs().get(0).getCurrentPlayerRunningTotal());
    }

    @Test
    void evenLegCountNotAllowed() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> DartSet.from(2, OutshotStyle.DOUBLE_OR_INNER_BULL_OUT, 501, "s", "o"));
        assertEquals("Maximum total leg count must be odd.", ex.getMessage());

    }

    @Test
    void newLegIsAddedAfterOneIsWon() {
        DartSet dartSet = DartSet.from(3, OutshotStyle.DOUBLE_OR_INNER_BULL_OUT, 40, "starting", "opponent");
        // Starting player outshot
        dartSet.addThrow(DartThrow.of("d20"));

        assertEquals("starting", dartSet.getLegs().get(0).getWinningPlayer());
        assertEquals(2, dartSet.getLegs().size());
    }

    @Test
    void startingPlayersAreRotated() {
        DartSet dartSet = DartSet.from(3, OutshotStyle.DOUBLE_OR_INNER_BULL_OUT, 40, "starting", "opponent");
        assertEquals("starting", dartSet.getCurrentPlayer());
        dartSet.addThrow(DartThrow.of("d20"));
        assertEquals("opponent", dartSet.getCurrentPlayer());
    }

    @Test
    void opponentCanWinSet_singleLeg() {
        DartSet dartSet = DartSet.from(1, OutshotStyle.DOUBLE_OR_INNER_BULL_OUT, 40, "starting", "opponent");
        dartSet.addThrow(DartThrow.of("d10"));
        dartSet.addThrow(DartThrow.of("x"));
        dartSet.addThrow(DartThrow.of("x"));

        // Opponent outshot
        dartSet.addThrow(DartThrow.of("d20"));
        assertTrue(dartSet.isOver());
        assertEquals("opponent", dartSet.getWinningPlayer());
    }


    @Test
    void startingPlayerCanWin_multipleLegs() {
        DartSet dartSet = DartSet.from(3, OutshotStyle.DOUBLE_OR_INNER_BULL_OUT, 40, "starting", "opponent");
        dartSet.addThrow(DartThrow.of("d20"));

        dartSet.addThrow(DartThrow.of("x"));
        dartSet.addThrow(DartThrow.of("x"));
        dartSet.addThrow(DartThrow.of("x"));

        dartSet.addThrow(DartThrow.of("d20"));
        assertTrue(dartSet.isOver());
        assertEquals("starting", dartSet.getWinningPlayer());
        assertEquals(2, dartSet.getLegs().size());
    }

}