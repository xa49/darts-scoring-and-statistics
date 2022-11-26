package app.darts;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DartGameTest {

    final GameStyle gameStyle = GameStyle.builder()
            .initialScore(40)
            .legsPerSet(1)
            .sets(1)
            .outshotStyle(OutshotStyle.DOUBLE_OR_INNER_BULL_OUT)
            .build();

    @Test
    void simpleGameIsWonByStartingPlayer() {
        DartGame game = DartGame.of(gameStyle, "starting", "opponent");
        game.addThrow(DartThrow.of("d20"));
        assertTrue(game.isOver());
        assertEquals("starting", game.getWinningPlayer().orElse("not starting"));
    }

    @Test
    void cannotAddThrowToFinishedGame() {
        DartGame game = DartGame.of(gameStyle, "starting", "opponent");
        game.addThrow(DartThrow.of("d20"));
        IllegalStateException ex = assertThrows(IllegalStateException.class,
                () -> game.addThrow(DartThrow.of("1")));
        assertEquals("Cannot add throw to finished game.", ex.getMessage());
    }

}