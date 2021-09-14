package validation.registry;

import validation.machines.BinaryTuringMachine;
import validation.machines.JsonTuringMachine;
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
        if (fileType !=  null) REGISTRY.put(fileType.toLowerCase(), validator);
    }

    public static Validator removeFileType(String fileType) {
        if (fileType == null) return null;

        return REGISTRY.remove(fileType.toLowerCase());
    }

    static {
        REGISTRY.put("json", new Validator(new JsonTuringMachine(), JsonState.START));
        REGISTRY.put("bin", new Validator(new BinaryTuringMachine(), BinaryState.START));
    }
}
