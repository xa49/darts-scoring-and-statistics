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
            return Constants.OUTER_BULLSEYE_VALUE;
        }
    },
    INNER_BULLSEYE {
        @Override
        public int getScore(int sectorNumber) {
            return Constants.INNER_BULLSEYE_VALUE;
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
    };

    public abstract int getScore(int sectorNumber);

    private static class Constants {
        private static final int OUTER_BULLSEYE_VALUE = 25;
        private static final int INNER_BULLSEYE_VALUE = 50;
    }
}
