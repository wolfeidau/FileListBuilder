package au.id.wolfe.recursivefilelister.digest;

/**
 * Thrown when an error occurs while using a {@link FileChecksumBuilder}.
 */
public class FileChecksumException extends Exception {

    /**
     * Build a new instance of {@link FileChecksumException}.
     *
     * @param message The message describing the error.
     */
    public FileChecksumException(String message) {
        super(message);
    }

}
