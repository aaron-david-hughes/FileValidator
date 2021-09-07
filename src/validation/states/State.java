package validation.states;

public interface State {

    State getStartState();

    State getRejectState();

    default boolean isAccept() {
        return false;
    }
}
