package validation.states;

public interface State {

    State getRejectState();

    default boolean isAccept() {
        return false;
    }
}
