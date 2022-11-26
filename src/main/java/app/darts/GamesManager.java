package app.darts;

import app.darts.stats.PlayerStatisticsDto;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GamesManager {
    private final Map<Long, DartGame> games = new HashMap<>();

    public DartGame addGame(GameStyleDto gameStyleDto, String startingPlayer, String otherPlayer) {
        GameStyle gameStyle = GameStyle.of(gameStyleDto);

        DartGame game = DartGame.of(gameStyle, startingPlayer, otherPlayer);
        games.put(game.getId(), game);
        return game;
    }

    public GameStateDto addThrow(long gameId, String dartThrow) {
        if (games.containsKey(gameId)) {
            DartGame dartGame = games.get(gameId);

            if (!dartGame.isOver()) {
                dartGame.addThrow(DartThrow.of(dartThrow));
            }

            GameStyleDto gameDto = dartGame.getGameStyle().toDto();
            List<PlayerStatisticsDto> playerDtos = dartGame.getPlayerStatisticsDtos();
            Map<String, Map<String, Integer>> currentStanding = dartGame.getCurrentStanding();

            return new GameStateDto(dartGame.getWinningPlayer(), dartGame.getNextToThrow(),
                    dartGame.getArrowsLeft(), currentStanding, gameDto, playerDtos);
        }
        throw new IllegalArgumentException("No game with id: " + gameId);
    }
}
