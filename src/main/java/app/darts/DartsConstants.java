package app.darts;

import java.util.HashMap;
import java.util.Map;

public class DartsConstants {
    public static final int MINIMUM_CHECKOUT_VALUE = 2;
    public static final int MAX_DOUBLE_CHECKOUT_VALUE = 40;
    public static final int THROWS_PER_ROUND = 3;
    public static final int OUTER_BULLSEYE_VALUE = 25;
    public static final int INNER_BULLSEYE_VALUE = 50;
    public static final int NINE_DARTER_ARROW_COUNT = 9;
    public static final int TON_PLUS_THRESHOLD = 100;

    public static final Map<String, OutshotStyle> GAME_STYLES = new HashMap<>(
            Map.of("STANDARD", OutshotStyle.DOUBLE_OR_INNER_BULL_OUT));

    static {
        for (OutshotStyle outshotStyle : OutshotStyle.values()) {
            GAME_STYLES.put(outshotStyle.name(), outshotStyle);
        }
    }

    private DartsConstants() {
        // utility class
    }

    public static class Formatting {
        public static final String HELP_MESSAGE = """
                Single scores are represented with their decimal values from 1 to 20.
                Double scores need to get a case-insensitive 'd' prefix. The same prefix for triples is 't'.
                Bull's eye shots are represented with their score, i.e. 25 or 50. A miss is represented as 'x'.
                """;

        private Formatting() {
            // utility class
        }
    }
}
