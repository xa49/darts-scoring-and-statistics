package app.darts;

public enum OutshotStyle {
    DOUBLE_OR_INNER_BULL_OUT {
        @Override
        public boolean qualifies(DartThrow dartThrow) {
            return dartThrow.isDouble() || dartThrow.isInnerBullsEye();
        }
    };

    public abstract boolean qualifies(DartThrow dartThrow);
}
