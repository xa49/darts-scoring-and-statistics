package app.darts;

public enum BoardRing {
    SINGLE {
        @Override
        public int getScore(int sectorNumber) {
            return sectorNumber;
        }
    },
    DOUBLE {
        @Override
        public int getScore(int sectorNumber) {
            return 2 * sectorNumber;
        }
    },
    TRIPLE {
        @Override
        public int getScore(int sectorNumber) {
            return 3 * sectorNumber;
        }
    },
    OUTER_BULLSEYE {
        @Override
        public int getScore(int sectorNumber) {
            return DartsConstants.OUTER_BULLSEYE_VALUE;
        }
    },
    INNER_BULLSEYE {
        @Override
        public int getScore(int sectorNumber) {
            return DartsConstants.INNER_BULLSEYE_VALUE;
        }
    },
    MISS {
        @Override
        public int getScore(int sectorNumber) {
            return 0;
        }
    },
    NO_THROW {
        @Override
        public int getScore(int sectorNumber) {
            return 0;
        }
    },
    NO_SCORE {
        @Override
        public int getScore(int sectorNumber) {
            return 0;
        }
    };

    public abstract int getScore(int sectorNumber);
}
