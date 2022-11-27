package app.darts;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(name = "New Game Details")
public class NewGameDetailsDto {
    private final long gameId;
    private final GameStyleDto gameStyle;

    public NewGameDetailsDto(DartGame game) {
        this.gameId = game.getId();
        this.gameStyle = game.getGameStyle().toDto();
    }
}
