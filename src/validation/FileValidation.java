package validation;

import validation.registry.FileTypeRegistry;

import java.io.File;
import java.io.FileNotFoundException;

public class FileValidation {

    private FileValidation() {}

    public static boolean runValidation(String contents, String fileType) {
        return FileTypeRegistry.getRegistry().get(fileType).validate(contents);
    }

    public static boolean runValidation(File contents, String fileType) throws FileNotFoundException {
        return FileTypeRegistry.getRegistry().get(fileType).validate(contents);
    }
}
