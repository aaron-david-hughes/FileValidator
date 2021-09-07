package validation.registry;

import validation.machines.binary.BinaryTuringMachine;
import validation.machines.json.JsonTuringMachine;
import validation.states.BinaryState;
import validation.states.JsonState;
import validation.validator.Validator;

import java.util.HashMap;
import java.util.Map;

public class FileTypeRegistry {
    private static final Map<String, Validator> REGISTRY = new HashMap<>();

    private FileTypeRegistry() {}

    public static Map<String, Validator> getRegistry() {
        return new HashMap<>(REGISTRY);
    }

    public static void addFileType(String fileType, Validator validator) {
        REGISTRY.put(fileType, validator);
    }

    public static Validator removeFileType(String fileType) {
        return REGISTRY.get(fileType);
    }

    static {
        REGISTRY.put("json", new Validator(new JsonTuringMachine(), JsonState.START));
        REGISTRY.put("binary", new Validator(new BinaryTuringMachine(), BinaryState.START));
    }
}
