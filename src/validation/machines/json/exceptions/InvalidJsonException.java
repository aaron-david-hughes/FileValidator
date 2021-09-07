package validation.machines.json.exceptions;

import java.util.Arrays;

public class InvalidJsonException extends Exception {
    public InvalidJsonException(char c, int i, char... expected) {
        super(
                String.format(
                        "Invalid character '%s' found at index '%s'.%nExpected one of the following: %s",
                        c,
                        i,
                        //causing odd bugs on display of message
                        Arrays.toString(expected).substring(1, expected.length - 1)
                )
        );
    }

    public InvalidJsonException(int i, String msg) {
        super(
                String.format(
                        "Invalid character found at index '%s': %s",
                        i,
                        msg
                )
        );
    }
}
