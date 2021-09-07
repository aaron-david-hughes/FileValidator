package validation.machines.binary;

import validation.machines.TuringMachine;
import validation.states.BinaryState;
import validation.states.JsonState;
import validation.states.State;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class BinaryTuringMachine implements TuringMachine {
    private final Map<State, Function<Character, State>> stateMachine = new HashMap<>();

    public BinaryTuringMachine() {
        setStateMachine();
    }

    public Map<State, Function<Character, State>> getStateMachine() {
        return new HashMap<>(stateMachine);
    }

    private static final Function<Character, State> TRANSITIONS = c -> {
        switch (c) {
            case '0': return BinaryState.ZERO;
            case '1': return BinaryState.ONE;
            case ' ': return BinaryState.ACCEPT;
            default: return BinaryState.REJECT;
        }
    };

    private void setStateMachine() {
        stateMachine.put(BinaryState.START, TRANSITIONS);

        stateMachine.put(BinaryState.ZERO, TRANSITIONS);

        stateMachine.put(BinaryState.ONE, TRANSITIONS);

        stateMachine.put(JsonState.ACCEPT, c -> BinaryState.REJECT);

        stateMachine.put(BinaryState.REJECT, c -> BinaryState.REJECT);
    }
}
