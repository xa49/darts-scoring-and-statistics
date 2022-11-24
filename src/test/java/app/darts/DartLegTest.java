package app.darts;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DartLegTest {

    @Test
    void currentPlayerSetCorrectly() {
        DartLeg leg = DartLeg.from(DartFinishStyle.DOUBLE_OR_INNER_BULL_OUT, "starting", "opponent", 501);
        assertEquals("starting", leg.getCurrentPlayer());
    }

    @Test
    void playersRotateIfGameNotEnded() {
        DartLeg leg = DartLeg.from(DartFinishStyle.DOUBLE_OR_INNER_BULL_OUT, "starting", "opponent", 501);
        assertEquals("starting", leg.getCurrentPlayer());
        leg.addThrow(DartThrow.of("20"));
        assertEquals("starting", leg.getCurrentPlayer());
        leg.addThrow(DartThrow.of("20"));
        assertEquals("starting", leg.getCurrentPlayer());
        leg.addThrow(DartThrow.of("20"));
        assertEquals("opponent", leg.getCurrentPlayer());
    }


    @Test
    void scoreDeductedFromBothPlayers() {
        DartLeg leg = DartLeg.from(DartFinishStyle.DOUBLE_OR_INNER_BULL_OUT, "starting", "opponent", 501);
        // Starting player throws
        leg.addThrow(DartThrow.of("20"));
        leg.addThrow(DartThrow.of("t20"));
        leg.addThrow(DartThrow.of("25"));

        // Non-starting player throws
        leg.addThrow(DartThrow.of("t19"));
        leg.addThrow(DartThrow.of("t19"));
        leg.addThrow(DartThrow.of("t19"));

        assertEquals(396, leg.getScoresPreThrow().get("starting"));
        assertEquals(330, leg.getScoresPreThrow().get("opponent"));
    }

    @Test
    void startingPlayerCanWin() {
        DartLeg leg = DartLeg.from(DartFinishStyle.DOUBLE_OR_INNER_BULL_OUT, "starting", "opponent", 100);
        // Starting player throws
        leg.addThrow(DartThrow.of("20"));
        leg.addThrow(DartThrow.of("d20"));
        leg.addThrow(DartThrow.of("d20"));

        assertEquals("starting", leg.getWinningPlayer());
    }

    @Test
    void nonStartingPlayerCanWin() {
        DartLeg leg = DartLeg.from(DartFinishStyle.DOUBLE_OR_INNER_BULL_OUT, "starting", "opponent", 100);
        // Starting player throws
        leg.addThrow(DartThrow.of("1"));
        leg.addThrow(DartThrow.of("1"));
        leg.addThrow(DartThrow.of("1"));

        // Non-starting player throws
        leg.addThrow(DartThrow.of("10"));
        leg.addThrow(DartThrow.of("d20"));
        leg.addThrow(DartThrow.of("50"));

        assertEquals("opponent", leg.getWinningPlayer());
    }

    @Test
    void cannotAddThrowToFinishedLeg() {
        DartLeg leg = DartLeg.from(DartFinishStyle.DOUBLE_OR_INNER_BULL_OUT, "starting", "opponent", 100);
        // Starting player throws
        leg.addThrow(DartThrow.of("10"));
        leg.addThrow(DartThrow.of("d20"));
        leg.addThrow(DartThrow.of("50"));

        IllegalStateException ex = assertThrows(IllegalStateException.class,
                () -> leg.addThrow(DartThrow.of("1")));
    }

    @Test
    void noScoreIfPlayerReachedZeroWithoutFinishingCondition() {
        DartLeg leg = DartLeg.from(DartFinishStyle.DOUBLE_OR_INNER_BULL_OUT, "starting", "opponent", 100);
        // Starting player throws
        leg.addThrow(DartThrow.of("t20"));
        leg.addThrow(DartThrow.of("20"));
        leg.addThrow(DartThrow.of("20"));

        assertNull(leg.getWinningPlayer());
        assertEquals("opponent", leg.getCurrentPlayer());
        assertEquals(100, leg.getScoresPreThrow().get("starting"));
    }

}