package app.darts;

import app.darts.stats.PlayerStatistics;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class DartSetTest {
    final GameStyle gameStyle = GameStyle.builder()
            .outshotStyle(OutshotStyle.DOUBLE_OR_INNER_BULL_OUT)
            .sets(1)
            .legsPerSet(1)
            .initialScore(501)
            .build();

    final GameStyle shortGameFrom40 = GameStyle.builder()
            .outshotStyle(OutshotStyle.DOUBLE_OR_INNER_BULL_OUT)
            .sets(1)
            .legsPerSet(1)
            .initialScore(40)
            .build();
    final Map<String, PlayerStatistics> playerStatistics = Map.of("starting", new PlayerStatistics("starting"),
            "opponent", new PlayerStatistics("opponent"));

    @Test
    void startingPlayerIsMarkedStarter() {
        DartSet dartSet = DartSet.of(gameStyle, "starting", "opponent", playerStatistics);
        assertEquals("starting", dartSet.getCurrentPlayer());
    }

    @Test
    void cannotAddThrowToFinishedSet() {

        DartSet dartSet = DartSet.of(shortGameFrom40, "starting", "opponent", playerStatistics);
        dartSet.addThrow(DartThrow.of("d20")); // outshot for 40

        IllegalStateException ex = assertThrows(IllegalStateException.class,
                () -> dartSet.addThrow(DartThrow.of("10")));
        assertEquals("Cannot add throw to finished set.", ex.getMessage());
    }

    @Test
    void throwsAreAddedToTheObject() {
        DartSet dartSet = DartSet.of(gameStyle, "starting", "opponent", playerStatistics);
        dartSet.addThrow(DartThrow.of("20"));
        assertEquals(481, dartSet.getLegs().get(0).getCurrentPlayerRunningTotal());
    }

    @Test
    void newLegIsAddedAfterOneIsWon() {
        GameStyle threeLegGame = GameStyle.builder()
                .outshotStyle(OutshotStyle.DOUBLE_OR_INNER_BULL_OUT)
                .sets(1)
                .legsPerSet(3)
                .initialScore(40)
                .build();
        DartSet dartSet = DartSet.of(threeLegGame, "starting", "opponent", playerStatistics);
        dartSet.addThrow(DartThrow.of("d20"));

        assertEquals("starting", dartSet.getLegs().get(0).getWinningPlayer().orElse("not starting"));
        assertEquals(2, dartSet.getLegs().size());
    }

    @Test
    void startingPlayersAreRotated() {
        GameStyle multiRoundShortGame = GameStyle.builder()
                .outshotStyle(OutshotStyle.DOUBLE_OR_INNER_BULL_OUT)
                .sets(1)
                .legsPerSet(3)
                .initialScore(40)
                .build();
        DartSet dartSet = DartSet.of(multiRoundShortGame, "starting", "opponent", playerStatistics);
        assertEquals("starting", dartSet.getCurrentPlayer());
        dartSet.addThrow(DartThrow.of("d20"));
        assertEquals("opponent", dartSet.getCurrentPlayer());
    }

    @Test
    void opponentCanWinSet_singleLeg() {
        DartSet dartSet = DartSet.of(shortGameFrom40, "starting", "opponent", playerStatistics);
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
        GameStyle multiRoundShortGame = GameStyle.builder()
                .outshotStyle(OutshotStyle.DOUBLE_OR_INNER_BULL_OUT)
                .sets(1)
                .legsPerSet(3)
                .initialScore(40)
                .build();
        DartSet dartSet = DartSet.of(multiRoundShortGame, "starting", "opponent", playerStatistics);
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