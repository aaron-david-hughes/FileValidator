package validation;

import validation.exceptions.FileTypeException;
import validation.registry.FileTypeRegistry;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Optional;

public class FileValidation {

    private FileValidation() {}

    public static boolean runValidation(String contents, String fileType) throws FileTypeException {
        if (contents == null) throw new IllegalArgumentException("Contents may not be null");

        try {
            return FileTypeRegistry.getRegistry().get(fileType).validate(contents);
        } catch (NullPointerException e) {
            throw new FileTypeException(fileType);
        }
    }

    public static boolean runValidation(File contents) throws FileNotFoundException, FileTypeException {
        if (contents == null) throw new IllegalArgumentException("Contents may not be null");

        String fileType = getFileExtension(contents.getName()).orElse(" ");

        try {
            return FileTypeRegistry.getRegistry().get(fileType.toLowerCase()).validate(contents);
        } catch (NullPointerException e) {
            throw new FileTypeException(fileType);
        }
    }

    private static Optional<String> getFileExtension(String filename) {
        return Optional.ofNullable(filename)
                .filter(f -> f.contains("."))
                .map(f -> f.substring(filename.lastIndexOf(".") + 1));
    }
}