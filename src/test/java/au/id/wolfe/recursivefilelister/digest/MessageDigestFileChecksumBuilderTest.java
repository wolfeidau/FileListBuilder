package au.id.wolfe.recursivefilelister.digest;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 *
 */
public class MessageDigestFileChecksumBuilderTest {

    final static Logger logger = LoggerFactory.getLogger(MessageDigestFileChecksumBuilderTest.class);

    @Test
    public void testCalculate() throws Exception {

        MessageDigestFileChecksumBuilder messageDigestFileChecksumBuilder
                = new MessageDigestFileChecksumBuilder("MD5");

        String result = messageDigestFileChecksumBuilder.calculate(new File("src/test/sample/Text_Data_01.txt"));

        logger.info("result = " + result);
    }
}
