package app.darts;

import app.Violation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@AllArgsConstructor
@Tag(name = "darts-game", description = "the API to create and manage games")
public class DartsController {

    private final GamesManager gamesManager;

    @Operation(summary = "Create a new darts game")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successful operation",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = NewGameDetailsDto.class))),
            @ApiResponse(responseCode = "400", description = "Validation error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class)))})
    @PostMapping(value = "/new-game")
    public ResponseEntity<NewGameDetailsDto> addNewGame(
            @Valid @ParameterObject GameStyleDto gameStyle,
            @RequestParam(required = false, defaultValue = "Player 1") String startingPlayer,
            @RequestParam(required = false, defaultValue = "Player 2") String opponent) {
        return ResponseEntity.created(URI.create("new-game"))
                .body(gamesManager.addGame(gameStyle, startingPlayer, opponent));
    }


    @Operation(summary = "Get the current state of a game")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GameStateDto.class))),
            @ApiResponse(responseCode = "404", description = "Game not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class)))
    })
    @GetMapping("/game/{gameId}")
    public ResponseEntity<GameStateDto> getGameState(@PathVariable Long gameId) {
        return ResponseEntity.ok(gamesManager.getState(gameId));
    }

    @Operation(summary = "Record the throw of the next player in a game")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GameStateDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class)))
    })
    @PostMapping("/throw")
    public ResponseEntity<GameStateDto> addThrow(
            @RequestParam(name = "gameId") long gameId,
            @Parameter(description = "The string representation of the last throw", example = "d20") @RequestParam(name = "value") String addedThrow) {
        return ResponseEntity.ok(gamesManager.addThrow(gameId, addedThrow));
    }

    @Operation(summary = "Get information about the formatting rules")
    @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/text",
            examples = @ExampleObject(name = "formatting", value = DartsConstants.Formatting.HELP_MESSAGE)))
    @GetMapping("/format-help")
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

    @ExceptionHandler(MissingEntityException.class)
    public ErrorResponse sendMissingEntityException(MissingEntityException ex) {
        return ErrorResponse.builder(ex, HttpStatus.NOT_FOUND, ex.getMessage())
                .title("Not found")
                .build();
    }
}
