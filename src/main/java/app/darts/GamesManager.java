package app.darts;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class GamesManager {
    private final Map<Long, DartGame> games = new HashMap<>();

    public NewGameDetailsDto addGame(GameStyleDto gameStyleDto, String startingPlayer, String otherPlayer) {
        GameStyle gameStyle = GameStyle.of(gameStyleDto);

        DartGame game = DartGame.of(gameStyle, startingPlayer, otherPlayer);
        games.put(game.getId(), game);
        return new NewGameDetailsDto(game);
    }

    public GameStateDto addThrow(long gameId, String dartThrow) {
        DartGame game = getGame(gameId);

        if (!game.isOver()) {
            game.addThrow(DartThrow.of(dartThrow));
        }

        return new GameStateDto(game);
    }

    public GameStateDto getState(Long gameId) {
        DartGame game = getGame(gameId);
        return new GameStateDto(game);
    }

    private DartGame getGame(Long gameId) {
        if (!games.containsKey(gameId)) {
            throw new MissingEntityException("No game with id: " + gameId);
        }
        return games.get(gameId);
    }
}
