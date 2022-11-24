package app.darts;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DartGameTest {

    @Test
    void simpleGameIsWonByStartingPlayer() {
        DartGame game = DartGame.from(10L, 1, OutshotStyle.DOUBLE_OR_INNER_BULL_OUT, 1, 40, "starting", "opponent");
        game.addThrow(DartThrow.of("d20"));
        assertTrue(game.isOver());
        assertEquals("starting", game.getWinningPlayer());
    }

    @Test
    void cannotAddThrowToFinishedGame() {
        DartGame game = DartGame.from(10L, 1, OutshotStyle.DOUBLE_OR_INNER_BULL_OUT, 1, 40, "starting", "opponent");
        game.addThrow(DartThrow.of("d20"));
        IllegalStateException ex = assertThrows(IllegalStateException.class,
                () -> game.addThrow(DartThrow.of("1")));
        assertEquals("Cannot add throw to finished game.", ex.getMessage());
    }

}