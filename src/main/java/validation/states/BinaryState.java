package validation.states;

public enum BinaryState implements State {
    ACCEPT() {
        @Override
        public boolean isAccept() {
            return true;
        }
    },
    ONE(),
    REJECT(),
    START(),
    ZERO();

    @Override
    public BinaryState getRejectState() {
        return REJECT;
    }
}
