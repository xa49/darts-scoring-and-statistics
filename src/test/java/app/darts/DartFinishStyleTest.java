package app.darts;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DartFinishStyleTest {

    @Test
    void doubleOrInnerBullOut_doubleQualifiesForOut() {
        assertTrue(DartFinishStyle.DOUBLE_OR_INNER_BULL_OUT.qualifiesForWinningThrow(DartThrow.of("d5")));
    }

    @Test
    void doubleOrInnerBullOut_innerBullQualifiesForOut() {
        assertTrue(DartFinishStyle.DOUBLE_OR_INNER_BULL_OUT.qualifiesForWinningThrow(DartThrow.of("50")));
    }

    @Test
    void doubleOrInnerBullOut_othersNotQualifyForOut() {
        assertFalse(DartFinishStyle.DOUBLE_OR_INNER_BULL_OUT.qualifiesForWinningThrow(DartThrow.of("12")));
        assertFalse(DartFinishStyle.DOUBLE_OR_INNER_BULL_OUT.qualifiesForWinningThrow(DartThrow.of("x")));
        assertFalse(DartFinishStyle.DOUBLE_OR_INNER_BULL_OUT.qualifiesForWinningThrow(DartThrow.of("t20")));
        assertFalse(DartFinishStyle.DOUBLE_OR_INNER_BULL_OUT.qualifiesForWinningThrow(DartThrow.of("25")));
    }

}