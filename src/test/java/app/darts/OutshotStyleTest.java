package app.darts;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OutshotStyleTest {

    @Test
    void doubleOrInnerBullOut_doubleQualifiesForOut() {
        assertTrue(OutshotStyle.DOUBLE_OR_INNER_BULL_OUT.qualifies(DartThrow.of("d5")));
    }

    @Test
    void doubleOrInnerBullOut_innerBullQualifiesForOut() {
        assertTrue(OutshotStyle.DOUBLE_OR_INNER_BULL_OUT.qualifies(DartThrow.of("50")));
    }

    @Test
    void doubleOrInnerBullOut_othersNotQualifyForOut() {
        assertFalse(OutshotStyle.DOUBLE_OR_INNER_BULL_OUT.qualifies(DartThrow.of("12")));
        assertFalse(OutshotStyle.DOUBLE_OR_INNER_BULL_OUT.qualifies(DartThrow.of("x")));
        assertFalse(OutshotStyle.DOUBLE_OR_INNER_BULL_OUT.qualifies(DartThrow.of("t20")));
        assertFalse(OutshotStyle.DOUBLE_OR_INNER_BULL_OUT.qualifies(DartThrow.of("25")));
    }

    @Test
    void anythingQualifies_allThrowsAreCheckout() {
        assertTrue(OutshotStyle.ANY_CHECKOUT.qualifies(DartThrow.of("12")));
        assertTrue(OutshotStyle.ANY_CHECKOUT.qualifies(DartThrow.of("x")));
        assertTrue(OutshotStyle.ANY_CHECKOUT.qualifies(DartThrow.of("t20")));
        assertTrue(OutshotStyle.ANY_CHECKOUT.qualifies(DartThrow.of("25")));
    }

    @Test
    void onlyInnerBullCheckout_allOthersAreRejected() {
        assertTrue(OutshotStyle.ONLY_INNER_BULL_OUT.qualifies(DartThrow.of("50")));
        assertFalse(OutshotStyle.ONLY_INNER_BULL_OUT.qualifies(DartThrow.of("12")));
        assertFalse(OutshotStyle.ONLY_INNER_BULL_OUT.qualifies(DartThrow.of("x")));
        assertFalse(OutshotStyle.ONLY_INNER_BULL_OUT.qualifies(DartThrow.of("t20")));
        assertFalse(OutshotStyle.ONLY_INNER_BULL_OUT.qualifies(DartThrow.of("25")));
    }

}