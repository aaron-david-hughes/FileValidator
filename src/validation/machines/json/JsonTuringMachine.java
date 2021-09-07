package validation.machines.json;

import validation.machines.TuringMachine;
import validation.states.JsonState;
import validation.states.State;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.function.Function;

public class JsonTuringMachine implements TuringMachine {
    private final Map<State, Function<Character, State>> stateMachine = new HashMap<>();
    private static final String OBJECT = "object";
    private static final String ARRAY = "array";
    private boolean isKey = false;
    private final Stack<String> nestingLevel = new Stack<>();

    public JsonTuringMachine() {
        setStateMachine();
    }

    public Map<State, Function<Character, State>> getStateMachine() {
        return new HashMap<>(stateMachine);
    }

    /*
     * State Transitions
     */

    private void setStateMachine() {
        stateMachine.put(JsonState.START, c -> {
            switch(c) {
                case '{': return enterObject();
                case '[': return enterArray();
                default: return reject();
            }
        });

        stateMachine.put(JsonState.ARRAY_ELEMENT_START, c -> {
            switch(c) {
                case '[': return enterArray();
                case '{': return enterObject();
                case '"': return JsonState.STRING;
                case ']': return exitElement();
                default: return isNumeric(c);
            }
        });

        stateMachine.put(JsonState.OBJECT_ELEMENT_START, c -> {
            switch(c) {
                case '"': return JsonState.KEY;
                case '}': return exitElement();
                default: return reject();
            }
        });

        stateMachine.put(JsonState.DELIMINATOR, c -> c == ':' ? JsonState.VALUE : reject());

        stateMachine.put(JsonState.VALUE, c -> {
            switch(c) {
                case '{': return enterObject();
                case '[': return enterArray();
                case '"': return JsonState.STRING;
                default: return isNumeric(c);
            }
        });

        stateMachine.put(JsonState.ELEMENT_END, c -> {
            if (nestingLevel.isEmpty()) return JsonState.ACCEPT;
            switch(c) {
                case ',': return JsonState.HAS_NEXT;
                case '}':
                case ']': return exitElement();
                default: return reject();
            }
        });

        stateMachine.put(JsonState.KEY, c -> {
            isKey = true;
            switch(c) {
                case '\\': return JsonState.ESCAPE_CHAR;
                case '"': return JsonState.DELIMINATOR;
                default: return JsonState.KEY;
            }
        });

        stateMachine.put(JsonState.STRING, c -> {
            isKey = false;
            switch(c) {
                case '\\': return JsonState.ESCAPE_CHAR;
                case '"': return JsonState.ELEMENT_END;
                default: return JsonState.STRING;
            }
        });

        stateMachine.put(JsonState.ESCAPE_CHAR, c -> {
            JsonState s = isKey ? JsonState.KEY : JsonState.STRING;
            switch(c) {
                case '"':
                case 'b':
                case 'n':
                case 't':
                case '\\': return s;
                default: return reject();
            }
        });

        stateMachine.put(JsonState.HAS_NEXT, c -> {
            switch(c) {
                case '{': return enterObject();
                case '[': return enterArray();
                case '"': return isKeyRequired();
                default: return isNumeric(c);
            }
        });

        stateMachine.put(JsonState.WHOLE_NUMBER, c -> {
            if(Character.isDigit(c)) return JsonState.WHOLE_NUMBER;
            else if (c == '.') return JsonState.DECIMAL_NUMBER_REQUIRED;

            //essentially is an instance of epsilon to not burn the character
            return stateMachine.get(JsonState.ELEMENT_END).apply(c);
        });

        stateMachine.put(JsonState.DECIMAL_NUMBER_REQUIRED, c -> Character.isDigit(c) ? JsonState.DECIMAL_NUMBER_OPTIONAL: reject());

        stateMachine.put(JsonState.DECIMAL_NUMBER_OPTIONAL, c -> {
            if(Character.isDigit(c)) return JsonState.DECIMAL_NUMBER_OPTIONAL;

            //essentially is an instance of epsilon to not burn the character
            return stateMachine.get(JsonState.ELEMENT_END).apply(c);
        });

        stateMachine.put(JsonState.ACCEPT, c -> reject());
    }

    /*
     * Utility Methods
     */

    private JsonState enterObject() {
        nestingLevel.push(OBJECT);
        return JsonState.OBJECT_ELEMENT_START;
    }

    private JsonState exitElement() {
        nestingLevel.pop();
        return JsonState.ELEMENT_END;
    }

    private JsonState enterArray() {
        nestingLevel.push(ARRAY);
        return JsonState.ARRAY_ELEMENT_START;
    }

    private JsonState isNumeric(char c) {
        if (Character.isDigit(c)) {
            return JsonState.WHOLE_NUMBER;
        }

        return reject();
    }

    private JsonState isKeyRequired() {
        if (nestingLevel.peek().equals(OBJECT)) {
            return JsonState.KEY;
        }

        if (nestingLevel.peek().equals(ARRAY)) {
            return JsonState.STRING;
        }

        return reject();
    }

    private JsonState reject() {
        nestingLevel.clear();
        return JsonState.REJECT;
    }
}
