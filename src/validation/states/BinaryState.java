package validation.states;

public enum BinaryState implements State {
    ACCEPT() {
        @Override
        public boolean isAccept() {
            return true;
        }
    },
    ONE(),
    START(),
    REJECT(),
    ZERO();

    @Override
    public State getStartState() {
        return START;
    }

    @Override
    public State getRejectState() {
        return REJECT;
    }
}
