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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DartsController.class)
class DartsControllerGameCreationWebMvcIT {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    GamesManager gamesManager;

    @Test
    void validGameRequest_returnsGameStyleDto_andGameId() throws Exception {
        DartGame standardGame = DartGame.of(
                GameStyle.builder()
                        .sets(3)
                        .legsPerSet(3)
                        .initialScore(501)
                        .outshotStyle(OutshotStyle.DOUBLE_OR_INNER_BULL_OUT)
                        .build(),
                "Player 1", "Player 2");
        when(gamesManager.addGame(ArgumentMatchers.any(), anyString(), anyString()))
                .thenReturn(standardGame);

        mockMvc.perform(post("/new-game")
                        .queryParam("sets", String.valueOf(3))
                        .queryParam("legsPerSet", String.valueOf(3))
                        .queryParam("initialScore", String.valueOf(501))
                        .queryParam("outshotStyle", "standard"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$", aMapWithSize(2)))
                .andExpect(jsonPath("$.gameId", isA(Integer.class)))
                .andExpect(jsonPath("$.gameSetup.sets", equalTo(3)))
                .andExpect(jsonPath("$.gameSetup.legsPerSet", equalTo(3)))
                .andExpect(jsonPath("$.gameSetup.initialScore", equalTo(501)))
                .andExpect(jsonPath("$.gameSetup.outshotStyle", equalTo("DOUBLE_OR_INNER_BULL_OUT")));
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

}