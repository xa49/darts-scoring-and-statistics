package app.darts;

import app.darts.stats.ConcreteGameEventDispatcher;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ScoreTrackerTest {

    @Test
    void canCompleteTwoRounds() {
        GameStyle gameStyle = GameStyle.builder()
                .initialScore(501)
                .sets(1)
                .legsPerSet(1)
                .outshotStyle(OutshotStyle.DOUBLE_OR_INNER_BULL_OUT)
                .build();
        ScoreTracker scoreTracker = new ScoreTracker(gameStyle, "player1", new ConcreteGameEventDispatcher());

        scoreTracker.addThrow(DartThrow.of("t20"));
        assertEquals(441, scoreTracker.getCurrentScore());

        scoreTracker.addThrow(DartThrow.of("50"));
        assertEquals(441, scoreTracker.getPreThrowScore());
        assertEquals(391, scoreTracker.getCurrentScore());
        assertEquals(List.of(DartThrow.of("t20"), DartThrow.of("50")),
                scoreTracker.getThrowsInCurrentRound());

        scoreTracker.addThrow(DartThrow.of("50"));
        assertEquals(341, scoreTracker.getCurrentScore());
        assertTrue(scoreTracker.isEndOfRound());
        assertEquals(0, scoreTracker.getThrowsInCurrentRound().size());
        assertEquals(3, scoreTracker.getThrowsInLatestRound().size());
        assertEquals(3, scoreTracker.getThrowsInLeg().size());

        scoreTracker.addThrow(DartThrow.of("t20"));
        scoreTracker.addThrow(DartThrow.of("t20"));
        scoreTracker.addThrow(DartThrow.of("t20"));
        assertTrue(scoreTracker.isEndOfRound());
        assertEquals(Collections.nCopies(3, DartThrow.of("t20")), scoreTracker.getThrowsInLatestRound());
    }

    @Test
    void scoreIsResetInCaseOfBust() {
        GameStyle gameStyle = GameStyle.builder()
                .initialScore(40)
                .sets(1)
                .legsPerSet(1)
                .outshotStyle(OutshotStyle.DOUBLE_OR_INNER_BULL_OUT)
                .build();
        ScoreTracker scoreTracker = new ScoreTracker(gameStyle, "player1", new ConcreteGameEventDispatcher());

        scoreTracker.addThrow(DartThrow.of("1"));
        scoreTracker.addThrow(DartThrow.of("d19"));
        assertEquals(39,scoreTracker.getPreThrowScore());
        assertEquals(40, scoreTracker.getCurrentScore());
        assertTrue(scoreTracker.isEndOfRound());
        assertEquals(List.of(DartThrow.getNoScore(), DartThrow.getNoScore(), DartThrow.getNoThrow()),
                scoreTracker.getThrowsInLatestRound());
        assertEquals(List.of(DartThrow.getNoScore(), DartThrow.getNoScore(), DartThrow.getNoThrow()),
                scoreTracker.getThrowsInLeg());
    }

    @Test
    void legIsWon() {
        GameStyle gameStyle = GameStyle.builder()
                .initialScore(41)
                .sets(1)
                .legsPerSet(1)
                .outshotStyle(OutshotStyle.DOUBLE_OR_INNER_BULL_OUT)
                .build();
        ScoreTracker scoreTracker = new ScoreTracker(gameStyle, "player1", new ConcreteGameEventDispatcher());

        scoreTracker.addThrow(DartThrow.of("1"));
        scoreTracker.addThrow(DartThrow.of("d20"));
        assertTrue(scoreTracker.isWinner());
        assertEquals(List.of(DartThrow.of("1"), DartThrow.of("d20"), DartThrow.getNoThrow()),
                scoreTracker.getThrowsInLatestRound());
        assertEquals(List.of(DartThrow.of("1"), DartThrow.of("d20"), DartThrow.getNoThrow()),
                scoreTracker.getThrowsInLeg());
    }

}