package app.darts;

public enum DartFinishStyle {
    DOUBLE_OR_INNER_BULL_OUT {
        @Override
        public boolean qualifiesForWinningThrow(DartThrow dartThrow) {
            return dartThrow.isDouble() || dartThrow.isInnerBullsEye();
        }
    };

    public abstract boolean qualifiesForWinningThrow(DartThrow dartThrow);
}
