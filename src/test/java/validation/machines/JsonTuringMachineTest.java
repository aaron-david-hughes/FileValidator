package validation.machines;

import org.junit.Test;
import validation.states.JsonState;
import validation.states.State;

import java.util.Map;
import java.util.Stack;
import java.util.function.Function;

import static org.junit.Assert.*;

public class JsonTuringMachineTest {

    private final JsonTuringMachine jsonTuringMachine = new JsonTuringMachine();
    private final Map<State, Function<Character, State>> stateMachine = jsonTuringMachine.getStateMachine();

    @Test
    public void startStateTransitions() {
        Function<Character, State> f = stateMachine.get(JsonState.START);

        assertEquals(JsonState.OBJECT_ELEMENT_START, f.apply('{'));
        assertStackContents(1, "object");

        assertEquals(JsonState.ARRAY_ELEMENT_START, f.apply('['));
        assertStackContents(2, "array");

        assertEquals(JsonState.REJECT, f.apply('f'));
        assertStackContents(0);
    }

    @Test
    public void ArrayElementStartStateTransitions() {
        Function<Character, State> f = stateMachine.get(JsonState.ARRAY_ELEMENT_START);

        assertEquals(JsonState.ARRAY_ELEMENT_START, f.apply('['));
        assertStackContents(1, "array");

        assertEquals(JsonState.ELEMENT_END, f.apply(']'));
        assertStackContents(0);

        assertEquals(JsonState.OBJECT_ELEMENT_START, f.apply('{'));
        assertStackContents(1, "object");

        assertEquals(JsonState.STRING, f.apply('"'));
        assertStackContents(1, "object");

        assertEquals(JsonState.WHOLE_NUMBER, f.apply('9'));
        assertStackContents(1, "object");

        assertEquals(JsonState.REJECT, f.apply(':'));
        assertStackContents(0);
    }

    @Test
    public void objectElementStartStateTransitions() {
        Function<Character, State> f = stateMachine.get(JsonState.OBJECT_ELEMENT_START);

        assertEquals(JsonState.KEY, f.apply('"'));
        assertStackContents(0);

        addToStack('{');
        assertStackContents(1, "object");
        assertEquals(JsonState.ELEMENT_END, f.apply('}'));
        assertStackContents(0);

        addToStack('[');
        assertStackContents(1, "array");
        assertEquals(JsonState.REJECT, f.apply('o'));
        assertStackContents(0);
    }

    @Test
    public void deliminatorStateTransitions() {
        Function<Character, State> f = stateMachine.get(JsonState.DELIMINATOR);

        assertEquals(JsonState.VALUE, f.apply(':'));

        assertEquals(JsonState.REJECT, f.apply(';'));
    }

    @Test
    public void valueStateTransitions() {
        Function<Character, State> f = stateMachine.get(JsonState.VALUE);

        assertEquals(JsonState.OBJECT_ELEMENT_START, f.apply('{'));
        assertStackContents(1, "object");

        assertEquals(JsonState.ARRAY_ELEMENT_START, f.apply('['));
        assertStackContents(2, "array");

        assertEquals(JsonState.STRING, f.apply('"'));

        assertEquals(JsonState.WHOLE_NUMBER, f.apply('0'));

        assertEquals(JsonState.REJECT, f.apply('h'));
        assertStackContents(0);
    }

    @Test
    public void elementEndStateTransitions() {
        Function<Character, State> f = stateMachine.get(JsonState.ELEMENT_END);

        assertEquals(JsonState.ACCEPT, f.apply(','));

        addToStack('{');
        assertStackContents(1, "object");

        assertEquals(JsonState.HAS_NEXT, f.apply(','));

        addToStack('{');
        assertStackContents(2, "object");
        assertEquals(JsonState.ELEMENT_END, f.apply('}'));
        assertStackContents(1);

        addToStack('[');
        assertStackContents(2, "array");
        assertEquals(JsonState.ELEMENT_END, f.apply(']'));
        assertStackContents(1);

        assertEquals(JsonState.REJECT, f.apply('j'));
        assertStackContents(0);
    }

    @Test
    public void keyStateTransitions() {
        Function<Character, State> f = stateMachine.get(JsonState.KEY);

        assertEquals(JsonState.ESCAPE_CHAR, f.apply('\\'));
        assertTrue(jsonTuringMachine.isKey());

        assertEquals(JsonState.DELIMINATOR, f.apply('"'));
        assertTrue(jsonTuringMachine.isKey());

        assertEquals(JsonState.KEY, f.apply('k'));
        assertTrue(jsonTuringMachine.isKey());
    }

    @Test
    public void stringStateTransitions() {
        Function<Character, State> f = stateMachine.get(JsonState.STRING);

        assertEquals(JsonState.ESCAPE_CHAR, f.apply('\\'));
        assertFalse(jsonTuringMachine.isKey());

        assertEquals(JsonState.ELEMENT_END, f.apply('"'));
        assertFalse(jsonTuringMachine.isKey());

        assertEquals(JsonState.STRING, f.apply('k'));
        assertFalse(jsonTuringMachine.isKey());
    }

    @Test
    public void escapeCharStateTransitions() {
        Function<Character, State> f = stateMachine.get(JsonState.ESCAPE_CHAR);

        setIsKey(true);
        assertEquals(JsonState.KEY, f.apply('"'));
        assertEquals(JsonState.KEY, f.apply('b'));
        assertEquals(JsonState.KEY, f.apply('n'));
        assertEquals(JsonState.KEY, f.apply('t'));
        assertEquals(JsonState.KEY, f.apply('\\'));
        assertEquals(JsonState.REJECT, f.apply(';'));

        setIsKey(false);
        assertEquals(JsonState.STRING, f.apply('"'));
        assertEquals(JsonState.STRING, f.apply('b'));
        assertEquals(JsonState.STRING, f.apply('n'));
        assertEquals(JsonState.STRING, f.apply('t'));
        assertEquals(JsonState.STRING, f.apply('\\'));
        assertEquals(JsonState.REJECT, f.apply(';'));
    }

    @Test
    public void hasNextStateTransitions() {
        Function<Character, State> f = stateMachine.get(JsonState.HAS_NEXT);

        assertEquals(JsonState.OBJECT_ELEMENT_START, f.apply('{'));
        assertStackContents(1, "object");

        assertEquals(JsonState.KEY, f.apply('"'));

        assertEquals(JsonState.ARRAY_ELEMENT_START, f.apply('['));
        assertStackContents(2, "array");

        assertEquals(JsonState.STRING, f.apply('"'));

        assertEquals(JsonState.WHOLE_NUMBER, f.apply('0'));

        assertEquals(JsonState.REJECT, f.apply(';'));
        assertStackContents(0);
    }

    @Test
    public void wholeNumberStateTransitions() {
        Function<Character, State> f = stateMachine.get(JsonState.WHOLE_NUMBER);

        assertEquals(JsonState.WHOLE_NUMBER, f.apply('9'));

        assertEquals(JsonState.DECIMAL_NUMBER_REQUIRED, f.apply('.'));

        assertEquals(JsonState.ACCEPT, f.apply(','));
    }

    @Test
    public void decimalNumberRequiredStateTransitions() {
        Function<Character, State> f = stateMachine.get(JsonState.DECIMAL_NUMBER_REQUIRED);

        assertEquals(JsonState.DECIMAL_NUMBER_OPTIONAL, f.apply('9'));

        assertEquals(JsonState.REJECT, f.apply('.'));
    }

    @Test
    public void decimalNumberOptionalStateTransitions() {
        Function<Character, State> f = stateMachine.get(JsonState.DECIMAL_NUMBER_OPTIONAL);

        assertEquals(JsonState.DECIMAL_NUMBER_OPTIONAL, f.apply('9'));

        assertEquals(JsonState.ACCEPT, f.apply(','));
    }

    @Test
    public void acceptStateTransitions() {
        char[] chars = {'[', '{', ':', '"'};

        for (char c : chars) assertEquals(JsonState.REJECT, stateMachine.get(JsonState.ACCEPT).apply(c));
    }

    private void assertStackContents(int size, String... top) {
        Stack<String> stack = jsonTuringMachine.getNestingLevel();

        assertEquals(size, stack.size());
        if (top != null && top.length > 0) assertEquals(top[0], stack.peek());
    }

    private void addToStack(char c) {
        stateMachine.get(JsonState.START).apply(c);
    }

    private void setIsKey(boolean b) {
        stateMachine.get(b ? JsonState.KEY : JsonState.STRING).apply('k');
    }
}
