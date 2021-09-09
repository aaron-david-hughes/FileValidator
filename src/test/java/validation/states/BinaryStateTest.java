package validation.states;

import org.junit.Test;

import static org.junit.Assert.*;

public class BinaryStateTest {

    private final BinaryState[] binaryStates = BinaryState.values();

    @Test
    public void getRejectStateReturnsRejectStateForAllStates() {
        for (BinaryState binaryState : binaryStates) {
            BinaryState rejectState = binaryState.getRejectState();

            assertEquals(BinaryState.REJECT, rejectState);
        }
    }

    @Test
    public void isAcceptReturnsFalseForAllStatesExceptAccept() {
        for (BinaryState binaryState : binaryStates) {
            if (binaryState == BinaryState.ACCEPT) continue;

            boolean result = binaryState.isAccept();

            assertFalse(result);
        }
    }

    @Test
    public void isAcceptReturnsTrueForAcceptState() {
        boolean result = BinaryState.ACCEPT.isAccept();

        assertTrue(result);
    }
}
