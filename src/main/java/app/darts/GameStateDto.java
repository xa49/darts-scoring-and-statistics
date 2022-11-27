package app.darts;

import app.darts.stats.PlayerStatisticsDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

@Schema(name = "Game State")
public record GameStateDto(Long gameId,
                           GameEvent lastEvent,
                           String nextToThrow,
                           int arrowsLeftForNextPlayer,
                           Standings currentStanding,
                           GameStyleDto gameStyle,
                           List<PlayerStatisticsDto> playerStatistics) {
    public GameStateDto(DartGame dartGame) {
        this(dartGame.getId(), dartGame.getLastEvent(), dartGame.getNextToThrow(), dartGame.getArrowsLeft(), new Standings(dartGame),
                dartGame.getGameStyle().toDto(), dartGame.getPlayerStatisticsDtos());
    }

    @Getter
    static class Standings {

        private final Optional<String> winner;

        @Schema(example = "{\"Player 1\":2,\"Player 2\":3}")
        private final Map<String, Integer> setsWon;
        @Schema(example = "{\"Player 1\":1,\"Player 2\":1}")
        private final Map<String, Integer> legsWonInCurrentSet;
        @Schema(example = "{\"Player 1\":441,\"Player 2\":401}")
        private final Map<String, Integer> scoreInCurrentLeg;

        public Standings(DartGame dartGame) {
            this.winner = dartGame.getWinningPlayer();
            setsWon = new TreeMap<>(dartGame.getSetsWonByAll());
            legsWonInCurrentSet = new TreeMap<>(dartGame.getLegsWonInCurrentSetByAll());
            scoreInCurrentLeg = new TreeMap<>(dartGame.getScoresInCurrentLegByAll());
        }

    }

}
