package validation.states;

public enum JsonState implements State {

    ACCEPT() {
        @Override
        public boolean isAccept() {
            return true;
        }
    },
    ARRAY_ELEMENT_START('[', '{', '"', ']'),
    DECIMAL_NUMBER_REQUIRED('}', ']', ',', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'),
    DECIMAL_NUMBER_OPTIONAL('}', ']', ',', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'),
    DELIMINATOR(':'),
    ELEMENT_END(',', '}', ']'),
    ESCAPE_CHAR('"', 'n', 'b', 't', '\\'),
    HAS_NEXT('{', '[', '"'),
    KEY("either missing closing tag or a non-escaped character is present"),
    OBJECT_ELEMENT_START('"', '}'),
    REJECT(),
    START('{', '['),
    STRING("either missing closing tag or a non-escaped character is present"),
    VALUE('{', '[', '"'),
    WHOLE_NUMBER('}', ']', ',', '.', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9');

    private final char[] possibleChars;
    private final String msg;

    JsonState(char... possibleChars) {
        this.possibleChars = possibleChars;
        this.msg = "Refer to possible chars for possible solution";
    }

    JsonState(String msg) {
        this.possibleChars = null;
        this.msg = msg;
    }

    @Override
    public JsonState getStartState() {
        return START;
    }

    @Override
    public JsonState getRejectState() {
        return REJECT;
    }

    public char[] getPossibleChars() {
        return this.possibleChars;
    }

    public String getMsg() {
        return this.msg;
    }
}
