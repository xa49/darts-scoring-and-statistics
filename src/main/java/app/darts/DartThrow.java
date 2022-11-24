package app.darts;

import java.util.Arrays;
import java.util.Map;

public class DartThrow {
    private static final Map<String, BoardRing> MODIFIERS =
            Map.of("d", BoardRing.DOUBLE,
                    "t", BoardRing.TRIPLE,
                    "x", BoardRing.MISS);
    private static final int OUTER_BULLSEYE_SPECIFIER = 25;
    private static final int INNER_BULLSEYE_SPECIFIER = 50;
    private static final int MAXIMUM_SECTOR_VALUE = 20;
    private static final char[] DIGITS = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};

    private final int sector;
    private final BoardRing ring;

    public static DartThrow of(String throwDetail) {
        if (throwDetail.length() == 0 || throwDetail.length() > 3) {
            throw new InvalidThrowRepresentationException(
                    "String representing throw should be 1 to 3 characters long.", throwDetail);
        }

        BoardRing ring = getRing(throwDetail);
        int sector = getSector(throwDetail, ring);

        return new DartThrow(sector, ring);
    }

    private static BoardRing getRing(String throwDetail) {
        BoardRing ring;
        String firstChar = throwDetail.substring(0, 1).toLowerCase();
        if (MODIFIERS.containsKey(firstChar)) {
            ring = MODIFIERS.get(firstChar);
            if (ring == BoardRing.MISS && throwDetail.length() != 1) {
                throw new InvalidThrowRepresentationException(
                        "String representing board miss can be only a single 'x' character.", throwDetail);
            } else if (ring != BoardRing.MISS && Arrays.binarySearch(DIGITS, throwDetail.charAt(1)) < 0) {
                throw new InvalidThrowRepresentationException(
                        "Only the first character can be a non-digit modifier.", throwDetail);
            }
        } else if (Character.isAlphabetic(throwDetail.charAt(0))) {
            throw new InvalidThrowRepresentationException(
                    "Letter '" + throwDetail.charAt(0) + "' is not a valid modifier.", throwDetail);
        } else if (throwDetail.equals("" + OUTER_BULLSEYE_SPECIFIER)) {
            ring = BoardRing.OUTER_BULLSEYE;
        } else if (throwDetail.equals("" + INNER_BULLSEYE_SPECIFIER)) {
            ring = BoardRing.INNER_BULLSEYE;
        } else {
            ring = BoardRing.SINGLE;
        }
        return ring;
    }

    private static int getSector(String throwDetail, BoardRing ring) {
        int score;
        if (ring == BoardRing.MISS
                || ring == BoardRing.OUTER_BULLSEYE
                || ring == BoardRing.INNER_BULLSEYE) {
            score = 0;
        } else {
            try {
                if (Character.isAlphabetic(throwDetail.charAt(0))) {
                    score = Integer.parseInt(throwDetail.substring(1));
                } else {
                    score = Integer.parseInt(throwDetail);
                }
            } catch (NumberFormatException e) {
                throw new InvalidThrowRepresentationException("Not parsable as sector value.", throwDetail);
            }
            if (score > MAXIMUM_SECTOR_VALUE) {
                throw new InvalidThrowRepresentationException(
                        "Maximum sector value is " + MAXIMUM_SECTOR_VALUE + ".", throwDetail);
            }
        }
        return score;
    }

    private DartThrow(int sector, BoardRing ring) {
        this.sector = sector;
        this.ring = ring;
    }

    public int getScore() {
        return ring.getScore(sector);
    }

    public boolean isDouble() {
        return ring == BoardRing.DOUBLE;
    }

    public boolean isInnerBullsEye() {
        return ring == BoardRing.INNER_BULLSEYE;
    }
}
