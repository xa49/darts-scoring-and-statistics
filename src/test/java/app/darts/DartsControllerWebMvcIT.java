package app.darts;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DartsController.class)
class DartsControllerWebMvcIT {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    GamesManager gamesManager;

    @Test
    void newGame_returnsGameStyleDto_andGameId() throws Exception {
        DartGame standardGame = DartGame.of(
                GameStyle.builder()
                        .sets(3)
                        .legsPerSet(3)
                        .initialScore(501)
                        .outshotStyle(OutshotStyle.DOUBLE_OR_INNER_BULL_OUT)
                        .build(),
                "Player 1", "Player 2");
        when(gamesManager.addGame(ArgumentMatchers.any(), anyString(), anyString()))
                .thenReturn(new NewGameDetailsDto(standardGame));

        mockMvc.perform(post("/new-game")
                        .queryParam("sets", String.valueOf(3))
                        .queryParam("legsPerSet", String.valueOf(3))
                        .queryParam("initialScore", String.valueOf(501))
                        .queryParam("outshotStyle", "standard"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$", aMapWithSize(2)))
                .andExpect(jsonPath("$.gameId", isA(Integer.class)))
                .andExpect(jsonPath("$.gameStyle.sets", equalTo(3)))
                .andExpect(jsonPath("$.gameStyle.legsPerSet", equalTo(3)))
                .andExpect(jsonPath("$.gameStyle.initialScore", equalTo(501)))
                .andExpect(jsonPath("$.gameStyle.outshotStyle", equalTo("DOUBLE_OR_INNER_BULL_OUT")));
    }

    @Test
    void newGame_requestingEvenNumberOfSets_badRequest() throws Exception {
        mockMvc.perform(post("/new-game")
                        .queryParam("sets", String.valueOf(2))
                        .queryParam("legsPerSet", String.valueOf(3))
                        .queryParam("initialScore", String.valueOf(501))
                        .queryParam("outshotStyle", "standard"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail", equalTo("Validation error")))
                .andExpect(jsonPath("$.properties.violations[0].field", equalTo("sets")))
                .andExpect(jsonPath("$.properties.violations[0].message",
                        equalTo("Total number of sets must be a positive, odd number.")));
    }

    @Test
    void newGame_requestingEvenNumberOfLegs_badRequest() throws Exception {
        mockMvc.perform(post("/new-game")
                        .queryParam("sets", String.valueOf(3))
                        .queryParam("legsPerSet", String.valueOf(2))
                        .queryParam("initialScore", String.valueOf(501))
                        .queryParam("outshotStyle", "standard"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail", equalTo("Validation error")))
                .andExpect(jsonPath("$.properties.violations[0].field", equalTo("legsPerSet")))
                .andExpect(jsonPath("$.properties.violations[0].message",
                        equalTo("Legs per set must be a positive, odd number.")));
    }

    @Test
    void newGame_requestingUnknownGameStyle_badRequest() throws Exception {
        mockMvc.perform(post("/new-game")
                        .queryParam("sets", String.valueOf(3))
                        .queryParam("legsPerSet", String.valueOf(3))
                        .queryParam("initialScore", String.valueOf(501))
                        .queryParam("outshotStyle", "random style"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail", equalTo("Validation error")))
                .andExpect(jsonPath("$.properties.violations[0].field", equalTo("outshotStyle")))
                .andExpect(jsonPath("$.properties.violations[0].message",
                        startsWith("You must choose an implemented outshot style. Options: ")))
                .andExpect(jsonPath("$.properties.violations[0].message",
                        containsString("STANDARD")));
    }

    @Test
    void newGame_requestingLessThanMinimumPossibleInitialScore_badRequest() throws Exception {
        mockMvc.perform(post("/new-game")
                        .queryParam("sets", String.valueOf(3))
                        .queryParam("legsPerSet", String.valueOf(3))
                        .queryParam("initialScore", String.valueOf(1))
                        .queryParam("outshotStyle", "standard"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail", equalTo("Validation error")))
                .andExpect(jsonPath("$.properties.violations[0].field", equalTo("initialScore")))
                .andExpect(jsonPath("$.properties.violations[0].message",
                        equalTo("Initial score must not be lower than 2.")));
    }

    @Test
    void newGame_requestingNonPositiveSetNumber_badRequest() throws Exception {
        mockMvc.perform(post("/new-game")
                        .queryParam("sets", String.valueOf(0))
                        .queryParam("legsPerSet", String.valueOf(1))
                        .queryParam("initialScore", String.valueOf(501))
                        .queryParam("outshotStyle", "standard"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail", equalTo("Validation error")))
                .andExpect(jsonPath("$.properties.violations[0].field", equalTo("sets")))
                .andExpect(jsonPath("$.properties.violations[0].message",
                        equalTo("Total number of sets must be a positive, odd number.")));

    }

    @Test
    void newGame_requestingNonPositiveLegNumber_badRequest() throws Exception {
        mockMvc.perform(post("/new-game")
                        .queryParam("sets", String.valueOf(1))
                        .queryParam("legsPerSet", String.valueOf(-1))
                        .queryParam("initialScore", String.valueOf(501))
                        .queryParam("outshotStyle", "standard"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail", equalTo("Validation error")))
                .andExpect(jsonPath("$.properties.violations[0].field", equalTo("legsPerSet")))
                .andExpect(jsonPath("$.properties.violations[0].message",
                        equalTo("Legs per set must be a positive, odd number.")));
    }

    @Test
    void newGame_allViolationsAreInMessage() throws Exception {
        mockMvc.perform(post("/new-game")
                        .queryParam("sets", String.valueOf(2))
                        .queryParam("legsPerSet", String.valueOf(-1))
                        .queryParam("initialScore", String.valueOf(1))
                        .queryParam("outshotStyle", "null"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail", equalTo("Validation error")))
                .andExpect(jsonPath("$.properties.violations", iterableWithSize(4)));
    }

    @Test
    void gameRequest_returnsGameState() throws Exception {
        DartGame game = DartGame.of(
                GameStyle.builder()
                        .sets(3)
                        .legsPerSet(3)
                        .initialScore(501)
                        .outshotStyle(OutshotStyle.DOUBLE_OR_INNER_BULL_OUT).build(),
                "Player 1", "Player 2");
        when(gamesManager.getState(1L))
                .thenReturn(new GameStateDto(game));

        mockMvc.perform(get("/game/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.gameId", equalTo(game.getId().intValue())))
                .andExpect(jsonPath("$.gameStyle.sets", equalTo(3)))
                .andExpect(jsonPath("$.gameStyle.outshotStyle", equalTo("DOUBLE_OR_INNER_BULL_OUT")))
                .andExpect(jsonPath("$.playerStatistics", hasSize(2)))
                .andExpect(jsonPath("$.playerStatistics[0].legsPlayed", equalTo(0)))
                .andExpect(jsonPath("$.nextToThrow", equalTo("Player 1")))
                .andExpect(jsonPath("$.arrowsLeftForNextPlayer", equalTo(3)))
                .andExpect(jsonPath("$.currentStanding.winner", equalTo(null)))
                .andExpect(jsonPath("$.currentStanding.scoreInCurrentLeg.['Player 1']", equalTo(501)));
    }

    @Test
    void gameRequest_notFoundReturnsMessage() throws Exception {
        when(gamesManager.getState(12L))
                .thenThrow(new MissingEntityException("No game with id: 12"));

        mockMvc.perform(get("/game/12"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.title", equalTo("Not found")))
                .andExpect(jsonPath("$.detail", equalTo("No game with id: 12")));
    }

    @Test
    void getFormatHelp_returnsMessage() throws Exception {
        mockMvc.perform(get("/format-help"))
                .andExpect(status().isOk())
                .andExpect(content().string(DartsConstants.Formatting.HELP_MESSAGE));
    }

    @Test
    void addThrow_validRequest_gameStateReturned() throws Exception {
        DartGame game = DartGame.of(
                GameStyle.builder()
                        .sets(3)
                        .legsPerSet(3)
                        .initialScore(501)
                        .outshotStyle(OutshotStyle.DOUBLE_OR_INNER_BULL_OUT).build(),
                "Player 1", "Player 2");
        game.addThrow(DartThrow.of("t20"));
        when(gamesManager.addThrow(1L, "t20"))
                .thenReturn(new GameStateDto(game));

        mockMvc.perform(post("/throw")
                        .queryParam("gameId", "1")
                        .queryParam("value", "t20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.gameId", equalTo(game.getId().intValue())))
                .andExpect(jsonPath("$.playerStatistics", hasSize(2)))
                .andExpect(jsonPath("$.playerStatistics[0].legsPlayed", equalTo(0)))
                .andExpect(jsonPath("$.nextToThrow", equalTo("Player 1")))
                .andExpect(jsonPath("$.arrowsLeftForNextPlayer", equalTo(2)))
                .andExpect(jsonPath("$.currentStanding.winner", equalTo(null)))
                .andExpect(jsonPath("$.currentStanding.scoreInCurrentLeg.['Player 1']", equalTo(441)));
    }

    @Test
    void addThrow_notValidRepresentation_errorMessageReturned() throws Exception {
        when(gamesManager.addThrow(12L, "t120"))
                .thenThrow(new InvalidThrowRepresentationException("not valid", "t120"));

        mockMvc.perform(post("/throw")
                        .queryParam("gameId", "12")
                        .queryParam("value", "t120"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title", equalTo("Bad input representation")))
                .andExpect(jsonPath("$.detail", equalTo("not valid. Input: t120. Read more about formatting rules at /format-help")));
    }

}