package validation.exceptions;

public class FileTypeException extends Exception {

    /**
     * If extension is null, or is active whitespace it will return the lacking of an extension as the message
     * Otherwise, it will follow on
     * @param extension
     */
    public FileTypeException(String extension) {
        super(
                extension == null || (!extension.isEmpty() && extension.isBlank())
                        ? "No file extension specified"
                        : String.format("No validator registered for file extension '%s'", extension)
        );
    }
}
