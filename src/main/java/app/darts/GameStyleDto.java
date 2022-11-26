package app.darts;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@ValidGameStyleDto
@Getter
@Setter
@NoArgsConstructor
public class GameStyleDto {

    private int sets;
    private int legsPerSet;
    private int initialScore;
    private String outshotStyle;

    GameStyleDto(GameStyle gameStyle) {
        this.sets = gameStyle.getSets();
        this.legsPerSet = gameStyle.getLegsPerSet();
        this.initialScore = gameStyle.getInitialScore();
        this.outshotStyle = gameStyle.getOutshotStyle().name();
    }
}
