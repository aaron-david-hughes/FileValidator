package validation.machines;

import org.junit.Test;
import validation.states.BinaryState;
import validation.states.State;

import java.util.Map;
import java.util.function.Function;

import static org.junit.Assert.assertEquals;

public class BinaryTurningMachineTest {

    private final Map<State, Function<Character, State>> binaryTuringMachine = new BinaryTuringMachine().getStateMachine();

    @Test
    public void acceptStateTransitions() {
        char[] chars = {'0', '1', '2', ' '};

        Function<Character, State> f = binaryTuringMachine.get(BinaryState.ACCEPT);

        for (char c : chars) {
            assertEquals(BinaryState.REJECT, f.apply(c));
        }
    }

    @Test
    public void oneStateTransitions() {
        Function<Character, State> f = binaryTuringMachine.get(BinaryState.ONE);

        assertExpectedTransitions(f);
    }

    @Test
    public void rejectStateTransitions() {
        char[] chars = {'0', '1', '2', ' '};

        Function<Character, State> f = binaryTuringMachine.get(BinaryState.REJECT);

        for (char c : chars) {
            assertEquals(BinaryState.REJECT, f.apply(c));
        }
    }

    @Test
    public void startStateTransitions() {
        Function<Character, State> f = binaryTuringMachine.get(BinaryState.START);

        assertExpectedTransitions(f);
    }

    @Test
    public void zeroStateTransitions() {
        Function<Character, State> f = binaryTuringMachine.get(BinaryState.ZERO);

        assertExpectedTransitions(f);
    }

    private void assertExpectedTransitions(Function<Character, State> f) {
        assertEquals(BinaryState.ZERO, f.apply('0'));
        assertEquals(BinaryState.ONE, f.apply('1'));
        assertEquals(BinaryState.ACCEPT, f.apply(' '));
        assertEquals(BinaryState.REJECT, f.apply('2'));
    }
}
