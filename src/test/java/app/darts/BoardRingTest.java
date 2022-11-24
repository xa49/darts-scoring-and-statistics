package app.darts;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoardRingTest {

    @Test
    void singlesValueCalculation() {
        assertEquals(17, BoardRing.SINGLE.getScore(17));
    }

    @Test
    void doublesValueCalculation() {
        assertEquals(34, BoardRing.DOUBLE.getScore(17));
    }

    @Test
    void tripleValueCalculation() {
        assertEquals(51, BoardRing.TRIPLE.getScore(17));
    }

    @Test
    void outerBullValueCalculation_alwaysReturnsSame() {
        assertEquals(25, BoardRing.OUTER_BULLSEYE.getScore(0));
        assertEquals(25, BoardRing.OUTER_BULLSEYE.getScore(25));
    }

    @Test
    void  innerBullValueCalculation_alwaysReturnsSame() {
        assertEquals(50, BoardRing.INNER_BULLSEYE.getScore(0));
        assertEquals(50, BoardRing.INNER_BULLSEYE.getScore(50));
    }

    @Test
    void miss_alwaysReturnsZero() {
        assertEquals(0, BoardRing.MISS.getScore(0));
        assertEquals(0, BoardRing.MISS.getScore(1));
    }

    @Test
    void noThrow_alwaysReturnsZero() {
        assertEquals(0, BoardRing.NO_THROW.getScore(0));
        assertEquals(0, BoardRing.NO_THROW.getScore(1));
    }

}