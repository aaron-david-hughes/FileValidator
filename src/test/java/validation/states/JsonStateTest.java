package validation.states;

import org.junit.Test;

import static org.junit.Assert.*;

public class JsonStateTest {

    private final JsonState[] jsonStates = JsonState.values();

    private static final String GENERIC_MESSAGE = "Refer to suggested chars for possible solution";

    @Test
    public void getPossibleCharsReturnsArraysForAllStatesBarAcceptRejectKeyAndString() {
        for (JsonState jsonState : jsonStates) {
            if (usedMessageConstructor(jsonState)) assertNull(jsonState.getPossibleChars());
            else assertNotNull(jsonState.getPossibleChars());
        }
    }

    @Test
    public void getMsgReturnsCorrespondingMessage() {
        for (JsonState jsonState : jsonStates) {
            if (usedMessageConstructor(jsonState)) assertNotEquals(GENERIC_MESSAGE, jsonState.getMsg());
            else assertEquals(GENERIC_MESSAGE, jsonState.getMsg());
        }
    }

    @Test
    public void getRejectStateReturnsRejectForAllStates() {
        for (JsonState jsonState : jsonStates) {
            assertEquals(JsonState.REJECT, jsonState.getRejectState());
        }
    }

    @Test
    public void isAcceptReturnsFalseForAllStatesButAccept() {
        for (JsonState jsonState : jsonStates) {
            if (jsonState == JsonState.ACCEPT) continue;

            assertFalse(jsonState.isAccept());
        }
    }

    @Test
    public void isAcceptReturnsTrueForAccept() {
        assertTrue(JsonState.ACCEPT.isAccept());
    }

    private boolean usedMessageConstructor(JsonState jsonState) {
        return jsonState == JsonState.ACCEPT ||
                jsonState == JsonState.KEY ||
                jsonState == JsonState.REJECT ||
                jsonState == JsonState.STRING;
    }
}
