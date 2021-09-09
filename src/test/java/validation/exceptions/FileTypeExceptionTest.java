package validation.exceptions;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class FileTypeExceptionTest {

    @Test
    public void fileTypeExceptionMessageShouldBeGenericWhenExtensionNull() {
        FileTypeException e = new FileTypeException(null);

        assertEquals("No file extension specified", e.getMessage());
    }

    @Test
    public void fileTypeExceptionMessageShouldBeGenericWhenExtensionEmpty() {
        FileTypeException e = new FileTypeException("");

        assertEquals("No file extension specified", e.getMessage());
    }

    @Test
    public void fileTypeExceptionMessageShouldBeSpecificWhenExtensionNotNullOrEmpty() {
        FileTypeException e = new FileTypeException("present");

        assertEquals("No validator registered for file extension 'present'", e.getMessage());
    }
}
