package validation;

import org.junit.Test;
import org.mockito.Mock;
import validation.exceptions.FileTypeException;
import validation.registry.FileTypeRegistry;
import validation.validator.Validator;

import java.io.File;
import java.io.FileNotFoundException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FileValidationTest {

    @Mock
    private final Validator validator = mock(Validator.class);

    @Mock
    private final File file = mock(File.class);

    @Test
    public void runValidationShouldThrowIllegalArgumentExceptionWhenStringContentsNull() {
        try {
            FileValidation.runValidation(null, "json");
            fail();
        } catch (FileTypeException e) {
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Contents may not be null", e.getMessage());
        }
    }

    @Test
    public void runValidationShouldThrowFileTypeExceptionWhenFileTypePassedNull() {
        try {
            FileValidation.runValidation("", null);
            fail();
        } catch (FileTypeException e) {
            assertEquals("No file extension specified", e.getMessage());
        }
    }

    @Test
    public void runValidationShouldThrowFileTypeExceptionWhenFileTypePassedNotRegistered() {
        try {
            FileValidation.runValidation("", "not");
            fail();
        } catch (FileTypeException e) {
            assertEquals("No validator registered for file extension 'not'", e.getMessage());
        }
    }

    @Test
    public void runValidationShouldReturnTrueIfFileTypeIsRegisteredAndContentStringIsValid() {
        String[][] pairs = {{"{ }", "json"}, {"1010 0101", "bin"}};

        for (String[] pair : pairs) {
            try {
                boolean result = FileValidation.runValidation(pair[0], pair[1]);
                assertTrue(result);
            } catch (FileTypeException e) {
                fail();
            }
        }
    }

    @Test
    public void runValidationShouldReturnFalseIfFileTypeIsRegisteredAndContentStringIsInvalid() {
        String[][] pairs = {{"{", "json"}, {"102", "bin"}};

        for (String[] pair : pairs) {
            try {
                boolean result = FileValidation.runValidation(pair[0], pair[1]);
                assertFalse(result);
            } catch (FileTypeException e) {
                fail();
            }
        }
    }

    @Test
    public void runValidationShouldThrowIllegalArgumentExceptionIfContentsFilePassedNull() {
        try {
            FileValidation.runValidation(null);
            fail();
        } catch (FileTypeException | FileNotFoundException e) {
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Contents may not be null", e.getMessage());
        }
    }

    @Test
    public void runValidationShouldThrowFileTypeExceptionWhenFileLacksExtension() throws FileNotFoundException {
        when(file.getName()).thenReturn("file");

        try {
            FileValidation.runValidation(file);
            fail();
        } catch (FileTypeException e) {
            assertEquals("No file extension specified", e.getMessage());
        }
    }

    @Test
    public void runValidationShouldThrowFileTypeExceptionWhenFileExtensionNotRegistered() throws FileNotFoundException {
        when(file.getName()).thenReturn("file.file");

        try {
            FileValidation.runValidation(file);
            fail();
        } catch (FileTypeException e) {
            assertEquals("No validator registered for file extension 'file'", e.getMessage());
        }
    }

    @Test
    public void runValidationShouldThrowFileNotFoundExceptionValidatorValidateMethodThrowsOne() throws FileNotFoundException {
        FileTypeRegistry.addFileType("test", validator);

        when(file.getName()).thenReturn(".test");
        when(validator.validate(file)).thenThrow(new FileNotFoundException("fileNotFoundThrown"));

        try {
            FileValidation.runValidation(file);
            fail();
        } catch (FileTypeException e) {
            fail();
        } catch (FileNotFoundException e) {
            assertEquals("fileNotFoundThrown", e.getMessage());
        }
    }

    @Test
    public void runValidationShouldReturnTrueIfFileHasRegisteredExtensionAndValidContents() throws FileNotFoundException {
        File[] files = {
                new File("src/test/resources/binaries/lowercase.bin"),
                new File("src/test/resources/jsons/lowercase.json")
        };

        for (File file : files) {
            try {
                boolean result = FileValidation.runValidation(file);
                assertTrue(result);
            } catch (FileTypeException e) {
                fail();
            }
        }
    }

    @Test
    public void runValidationShouldReturnTrueIfFileHasRegisteredExtensionNotMatchingRegisteredCaseAndValidContents() throws FileNotFoundException {
        File[] files = {
                new File("src/test/resources/binaries/uppercase.BIN"),
                new File("src/test/resources/jsons/uppercase.JSON")
        };

        for (File file : files) {
            try {
                boolean result = FileValidation.runValidation(file);
                assertTrue(result);
            } catch (FileTypeException e) {
                fail();
            }
        }
    }

    @Test
    public void runValidationShouldReturnFalseIfFileHasRegisteredExtensionButInvalidContents() throws FileNotFoundException {
        File[] files = {
                new File("src/test/resources/binaries/invalid.bin"),
                new File("src/test/resources/jsons/invalid.json")
        };

        for (File file : files) {
            try {
                boolean result = FileValidation.runValidation(file);
                assertFalse(result);
            } catch (FileTypeException e) {
                fail();
            }
        }
    }}
