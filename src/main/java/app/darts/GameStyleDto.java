package app.darts;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@ValidGameStyleDto
@Getter
@Setter
@NoArgsConstructor
@Schema(name = "Game Style")
public class GameStyleDto {

    @Schema(example = "7", description = "A positive, odd number representing the maximum number of sets to be played.")
    private int sets;
    @Schema(example = "5", description = "A positive, odd number representing the maximum number of legs to be played in each set.")
    private int legsPerSet;
    @Schema(example = "501", description = "The starting score for each player in each leg.")
    private int initialScore;
    @Schema(example = "DOUBLE_OR_INNER_BULL_OUT", description = "Conditions the last throw must meet for winning the leg.",oneOf = OutshotStyle.class)
    private String outshotStyle;

    GameStyleDto(GameStyle gameStyle) {
        this.sets = gameStyle.getSets();
        this.legsPerSet = gameStyle.getLegsPerSet();
        this.initialScore = gameStyle.getInitialScore();
        this.outshotStyle = gameStyle.getOutshotStyle().name();
    }
}
