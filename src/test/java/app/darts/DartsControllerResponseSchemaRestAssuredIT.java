package app.darts;

import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.with;

@WebMvcTest(DartsController.class)
class DartsControllerResponseSchemaRestAssuredIT {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    GamesManager gamesManager;

    @BeforeEach
    void  init() {
        RestAssuredMockMvc.mockMvc(mockMvc);
        RestAssuredMockMvc.requestSpecification =
                given().contentType(ContentType.JSON)
                        .accept(ContentType.JSON);
    }

    @Test
    void newGameSchemaMatches() {
        DartGame standardGame = DartGame.of(
                GameStyle.builder()
                        .sets(3)
                        .legsPerSet(3)
                        .initialScore(501)
                        .outshotStyle(OutshotStyle.DOUBLE_OR_INNER_BULL_OUT)
                        .build(),
                "Player 1", "Player 2");
        Mockito.when(gamesManager.addGame(ArgumentMatchers.any(), ArgumentMatchers.anyString(), ArgumentMatchers.anyString()))
                        .thenReturn(new NewGameDetailsDto(standardGame));

        with()
                .param("sets", 3)
                .param("legsPerSet", 3)
                .param("initialScore", 501)
                .param("outshotStyle", "standard")
                .post("/new-game")
                .then()
                .body(matchesJsonSchemaInClasspath("new-game-details-dto.json"));
    }
}