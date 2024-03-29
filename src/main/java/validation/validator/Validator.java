package validation.validator;

import validation.machines.TuringMachine;
import validation.states.State;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.function.Function;

/**
 * class for validating file structure
 */
public class Validator {
    private final Map<State, Function<Character, State>> stateMachine;
    private final State startState;

    public Validator(TuringMachine turingMachine, State startState) {
        this.stateMachine = turingMachine.getStateMachine();
        this.startState = startState;
    }

    public boolean validate(File contents) throws FileNotFoundException {
        if (contents == null) throw new IllegalArgumentException("Contents may not be null");

        try (Scanner sc = new Scanner(contents)) {
            return runStateMachine(sc);
        }
    }

    public boolean validate(String contents) {
        if (contents == null) throw new IllegalArgumentException("Contents may not be null");

        try (Scanner sc = new Scanner(contents)) {
            return runStateMachine(sc);
        }
    }

    private boolean runStateMachine(Scanner sc) {
        String contents = sc.useDelimiter("\\A").next().replaceAll("\\s", "") + " ";

        State state = startState;

        for (char c : contents.toCharArray()) {
            state = stateMachine.get(state).apply(c);

            if (state == state.getRejectState()) break;
        }

        return state.isAccept();
    }
}
