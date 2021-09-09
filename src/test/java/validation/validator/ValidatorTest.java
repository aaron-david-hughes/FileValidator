package validation.validator;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import validation.machines.TuringMachine;
import validation.states.State;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ValidatorTest {

    @Mock
    private final TuringMachine turingMachine = mock(TuringMachine.class);

    @Mock
    private final State state = mock(State.class);

    private Validator validator;

    private final File file = new File("src/test/resources/binaries/lowercase.bin");

    @Before
    public void before() {
        Map<State, Function<Character, State>> stateMachine = new HashMap<>();

        stateMachine.put(state, c -> state);

        when(turingMachine.getStateMachine()).thenReturn(stateMachine);

        validator = new Validator(turingMachine, state);
    }

    @Test
    public void validateShouldThrowIllegalArgumentExceptionIfFileIsNull() throws FileNotFoundException {
        try {
            validator.validate((File) null);
        } catch (IllegalArgumentException e) {
            assertEquals("Contents may not be null", e.getMessage());
        }
    }

    @Test
    public void validateShouldReturnTrueWhenFileContentsPassesStateMachine() throws FileNotFoundException {
        when(state.getRejectState()).thenReturn(null);
        when(state.isAccept()).thenReturn(true);

        boolean result = validator.validate(file);

        assertTrue(result);
    }

    @Test
    public void validateShouldReturnFalseWhenFileContentsGoesThroughStateMachineButIsNotInAcceptState() throws FileNotFoundException {
        when(state.getRejectState()).thenReturn(null);
        when(state.isAccept()).thenReturn(false);

        boolean result = validator.validate(file);

        assertFalse(result);
    }

    @Test
    public void validateShouldReturnFalseWhenFileContentsFailsStateMachineEarly() throws FileNotFoundException {
        when(state.getRejectState()).thenReturn(state);
        when(state.isAccept()).thenReturn(false);

        boolean result = validator.validate(file);

        assertFalse(result);
    }

    @Test
    public void validateShouldThrowIllegalArgumentExceptionIfStringIsNull() {
        try {
            validator.validate((String) null);
        } catch (IllegalArgumentException e) {
            assertEquals("Contents may not be null", e.getMessage());
        }
    }

    @Test
    public void validateShouldReturnTrueWhenStringContentsPassesStateMachine() {
        when(state.getRejectState()).thenReturn(null);
        when(state.isAccept()).thenReturn(true);

        boolean result = validator.validate("state");

        assertTrue(result);
    }

    @Test
    public void validateShouldReturnFalseWhenStringContentsGoesThroughStateMachineButIsNotInAcceptState() {
        when(state.getRejectState()).thenReturn(null);
        when(state.isAccept()).thenReturn(false);

        boolean result = validator.validate("state");

        assertFalse(result);
    }

    @Test
    public void validateShouldReturnFalseWhenStringContentsFailsStateMachineEarly() {
        when(state.getRejectState()).thenReturn(state);
        when(state.isAccept()).thenReturn(false);

        boolean result = validator.validate("state");

        assertFalse(result);
    }
}
