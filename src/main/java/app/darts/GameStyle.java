package app.darts;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder // Tests make use of the builder
@ToString
public class GameStyle {

    private final int initialScore;
    private final int sets;
    private final int legsPerSet;
    private final OutshotStyle outshotStyle;

    public static GameStyle of(GameStyleDto gameStyleDto) {
        return new GameStyle(gameStyleDto.getInitialScore(), gameStyleDto.getSets(),
                gameStyleDto.getLegsPerSet(),
                DartsConstants.GAME_STYLES.get(gameStyleDto.getOutshotStyle().toUpperCase()));
    }

    private GameStyle(int initialScore, int sets, int legsPerSet, OutshotStyle outshotStyle) {
        this.initialScore = initialScore;
        this.sets = sets;
        this.legsPerSet = legsPerSet;
        this.outshotStyle = outshotStyle;
        validateState();
    }

    public GameStyleDto toDto() {
        return new GameStyleDto(this);
    }

    private void validateState() {
        List<String> violations = new ArrayList<>();
        if (sets % 2 == 0) {
            violations.add("Maximum possible number of sets must be odd.");
        }
        if (legsPerSet % 2 == 0) {
            violations.add("Maximum possible number of legs must be odd.");
        }
        if (outshotStyle == null) {
            violations.add("No implemented outshot manager found for request.");
        }
        if (!violations.isEmpty()) {
            throw new IllegalStateException(String.join(" AND ALSO: "));
        }
    }
}
