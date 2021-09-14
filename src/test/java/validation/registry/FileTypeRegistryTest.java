package validation.registry;

import org.junit.After;
import org.junit.Test;
import org.mockito.Mock;
import validation.machines.BinaryTuringMachine;
import validation.machines.JsonTuringMachine;
import validation.states.BinaryState;
import validation.states.JsonState;
import validation.validator.Validator;

import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class FileTypeRegistryTest {

    @Mock
    private final Validator validator = mock(Validator.class);

    private Map<String, Validator> registry;

    @After
    public void after() {
        for(String key : registry.keySet()) FileTypeRegistry.removeFileType(key);

        FileTypeRegistry.addFileType("json", new Validator(new JsonTuringMachine(), JsonState.START));
        FileTypeRegistry.addFileType("bin", new Validator(new BinaryTuringMachine(), BinaryState.START));
    }

    @Test
    public void getRegistryReturnsMapShowingDefaultValidatorsInPlace() {
        registry = FileTypeRegistry.getRegistry();

        assertEquals(2, registry.size());
        assertTrue(registry.containsKey("json"));
        assertTrue(registry.containsKey("bin"));
    }

    @Test
    public void addFileTypeRegistersAnotherValidatorUsingLowercase() {
        String[] fileTypes = {"test", "TEST"};

        for (String fileType : fileTypes) {
            FileTypeRegistry.addFileType(fileType, validator);

            registry = FileTypeRegistry.getRegistry();

            assertEquals(3, registry.size());
            assertEquals(validator, registry.get("test"));
        }
    }

    @Test
    public void addFileTypeIgnoresAnotherValidatorIfRegisteringUnderNull() {
        FileTypeRegistry.addFileType(null, validator);

        registry = FileTypeRegistry.getRegistry();

        assertEquals(2, registry.size());
    }

    @Test
    public void removeFileTypeDoesNothingIfNoValidatorFound() {
        Validator removedValidator = FileTypeRegistry.removeFileType("not present");

        registry = FileTypeRegistry.getRegistry();

        assertEquals(2, registry.size());
        assertNull(removedValidator);
    }

    @Test
    public void removeFileTypeUnregistersValidatorIfPresentUsingLowercase() {
        String[] fileTypes = {"bin", "BIN"};

        for (String fileType : fileTypes) {
            registry = FileTypeRegistry.getRegistry();
            assertEquals(2, registry.size());

            FileTypeRegistry.removeFileType(fileType);

            registry = FileTypeRegistry.getRegistry();
            assertEquals(1, registry.size());

            after();
        }
    }

    @Test
    public void removeFileTypeDoesNotUnregisterValidatorIfNull() {
        registry = FileTypeRegistry.getRegistry();
        assertEquals(2, registry.size());

        FileTypeRegistry.removeFileType(null);

        registry = FileTypeRegistry.getRegistry();
        assertEquals(2, registry.size());
    }

    @Test
    public void canOverrideExistingRegistrations() {
        registry = FileTypeRegistry.getRegistry();
        assertEquals(2, registry.size());

        FileTypeRegistry.addFileType("bin", validator);

        registry = FileTypeRegistry.getRegistry();

        assertEquals(2, registry.size());
        assertEquals(validator, registry.get("bin"));
    }
}
