package au.id.wolfe.recursivefilelister.digest;

import java.io.File;
import java.security.DigestException;

/**
 * Interface for classes which compute the checksum of a file.
 */
public interface FileChecksumBuilder {

    /**
     * The default buffer size used while generating a checksum for the supplied file.
     */
    static final int DEFAULT_STREAM_BUFFER_SIZE = 32768;

    /**
     * Get the algorithm used to compute the checksum.
     *
     * @return The name of the algorithm.
     */
    String getAlgorithm();

    /**
     * The filename extension for this checksum.
     *
     * @return The filename extension.
     */
    String getFileExtension();

    /**
     * Calculate the digest for the supplied file.
     *
     * @param file The file to compute the digest from.
     * @return the computed checksum encoded as a hexadecimal string.
     * @throws DigestException Thrown if there as an issue computing the checksum.
     */
    String calculate( File file ) throws FileChecksumException;
}
