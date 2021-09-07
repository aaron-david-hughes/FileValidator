package validation.machines;

import validation.states.State;

import java.util.Map;
import java.util.function.Function;

public interface TuringMachine {

    Map<State, Function<Character, State>> getStateMachine();

}
