package app.darts;

import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DartGameSimulationTest {

    @Test
    void fullShortGame() throws IOException {
        GameStyle gameStyle = GameStyle.builder()
                .sets(3)
                .legsPerSet(3)
                .initialScore(100)
                .outshotStyle(OutshotStyle.DOUBLE_OR_INNER_BULL_OUT)
                .build();
        DartGame dartGame = DartGame.of( gameStyle, "starting", "opponent");

        try(BufferedReader reader = Files.newBufferedReader(Path.of("src/test/resources/short-game-simulation.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                dartGame.addThrow(DartThrow.of(line));
            }
        }

        assertEquals("starting", dartGame.getWinningPlayer().orElse("not starting"));

        // First 9-dart averages not relevant because of short format
        var winningStats = dartGame.getPlayerStatistics("starting");
        assertEquals(80, winningStats.getAverage(), 0.0001);
        assertEquals(50, winningStats.getOutshotEfficiency());
        assertEquals(4, winningStats.getLegsWonCount());
        assertEquals(6, winningStats.getLegsPlayedCount());

        var opponentStats = dartGame.getPlayerStatistics("opponent");
        assertEquals(80.769230769, opponentStats.getAverage(), 0.0001);
        assertEquals(100, opponentStats.getOutshotEfficiency());
        assertEquals(2, opponentStats.getLegsWonCount());
        assertEquals(6, opponentStats.getLegsPlayedCount());
    }

    @Test
    void short501Game_nineDartAveragesCalculatedProperly_nineDartedCalculatedProperly() throws IOException {
        GameStyle gameStyle = GameStyle.builder()
                .sets(1)
                .legsPerSet(3)
                .initialScore(501)
                .outshotStyle(OutshotStyle.DOUBLE_OR_INNER_BULL_OUT)
                .build();
        DartGame dartGame = DartGame.of(gameStyle, "starting", "opponent");

        try(BufferedReader reader = Files.newBufferedReader(Path.of("src/test/resources/short-501-game.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                dartGame.addThrow(DartThrow.of(line));
            }
        }

        assertEquals("starting", dartGame.getWinningPlayer().orElse("not starting"));

        var winningStats = dartGame.getPlayerStatistics("starting");
        assertEquals(92.5, winningStats.getFirstNineDartAverage(), 0.0001);
        var opponentStats = dartGame.getPlayerStatistics("opponent");
        assertEquals(98.8888, opponentStats.getFirstNineDartAverage(), 0.0001);
        assertEquals(1, opponentStats.getNineDarterCount());
    }
}
