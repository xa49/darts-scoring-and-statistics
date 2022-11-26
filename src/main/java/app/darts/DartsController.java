package app.darts;

import app.Violation;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.*;

@RestController
@AllArgsConstructor
public class DartsController {

    private final GamesManager gamesManager;

    @PostMapping("/new-game")
    public ResponseEntity<Map<String, Object>> addNewGame(@Valid GameStyleDto gameStyle,
                                                          @RequestParam(required = false, defaultValue = "Player 1") String startingPlayer,
                                                          @RequestParam(required = false, defaultValue = "Player 2") String opponent) {
        DartGame game = gamesManager.addGame(gameStyle, startingPlayer, opponent);
        return ResponseEntity.created(URI.create("new-game"))
                .body(new TreeMap<>(Map.of("gameId", game.getId(),
                        "gameSetup", game.getGameStyle().toDto())));
    }

    @PostMapping("/throw")
    public ResponseEntity<GameStateDto> addThrow(@RequestParam(name = "gameId") long gameId,
                                                 @RequestParam(name = "value") String addedThrow) {
        return ResponseEntity.ok(gamesManager.addThrow(gameId, addedThrow));
    }

    @GetMapping("/help")
    public ResponseEntity<String> sendFormatHelp() {
        return ResponseEntity.ok(DartsConstants.Formatting.HELP_MESSAGE);
    }

    @ExceptionHandler(InvalidThrowRepresentationException.class)
    public ErrorResponse sendWrongThrowInputMessage(InvalidThrowRepresentationException ex) {
        return ErrorResponse.builder(ex, HttpStatus.BAD_REQUEST, ex.getMessage())
                .title("Bad input representation")
                .build();
    }

    @ExceptionHandler(BindException.class)
    public ErrorResponse sendValidationException(BindException ex) {
        List<Violation> violations = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> new Violation(fe.getField(), fe.getDefaultMessage()))
                .toList();
        return ErrorResponse.builder(ex, HttpStatus.BAD_REQUEST, "Validation error")
                .property("violations", violations).build();
    }
}
