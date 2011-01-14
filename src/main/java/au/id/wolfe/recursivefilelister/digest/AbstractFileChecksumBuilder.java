package au.id.wolfe.recursivefilelister.digest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base class for implementations of the file digest builder.
 */
public abstract class AbstractFileChecksumBuilder implements FileChecksumBuilder {

    public static final Logger logger = LoggerFactory.getLogger(AbstractFileChecksumBuilder.class);

    /**
     * The algorithm used to compute checksums.
     */
    protected final String algorithm;

    /**
     * Build a new instance of {@link AbstractFileChecksumBuilder}.
     *
     * @param algorithm The algorithm used to compute checksums.
     */
    public AbstractFileChecksumBuilder(String algorithm) {
        this.algorithm = algorithm;
    }

    /**
     * {@inheritDoc}
     */
    public String getAlgorithm() {
        return algorithm;
    }

    /**
     * {@inheritDoc}
     */
    public String getFileExtension() {
        return String.format(".%s", algorithm.toLowerCase().replace("-", ""));
    }

}
