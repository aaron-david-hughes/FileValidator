package validation.exceptions;

public class FileTypeException extends Exception {

    public FileTypeException(String extension) {
        super(
                extension == null || (!extension.isEmpty() && extension.isBlank())
                        ? "No file extension specified"
                        : String.format("No validator registered for file extension '%s'", extension)
        );
    }
}
