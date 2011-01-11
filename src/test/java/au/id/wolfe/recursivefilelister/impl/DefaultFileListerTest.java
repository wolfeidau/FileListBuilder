package au.id.wolfe.recursivefilelister.impl;

import au.id.wolfe.recursivefilelister.FileLister;
import com.google.common.collect.Sets;
import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 *
 */
public class DefaultFileListerTest {

    private static final String SRC_TEST_SAMPLE = "src/test/sample/";
    private static final String TARGET_SAMPLE = "target/sample/";

    FileLister fileLister;

    final static Logger logger = LoggerFactory.getLogger(DefaultFileLister.class);

    @Before
    public void setup() throws Exception {

        logger.info("setup()");

        //FileUtils.copyFileToDirectory("src/test/sample/Text");
        FileUtils.copyDirectory(new File(SRC_TEST_SAMPLE), new File(TARGET_SAMPLE));

        fileLister = new DefaultFileLister();

        fileLister.configureFileNameExclusions(Sets.<String>newHashSet("~snapshot"));
    }

    @Test
    public void testGetFileArrayFromDirectory() throws Exception {

        logger.info("testGetFileArrayFromDirectory()");

        File targetSampleDirectory = new File(TARGET_SAMPLE);

        assertTrue(targetSampleDirectory.isDirectory());

        File[] sampleFiles = fileLister.getFileArrayFromDirectory(targetSampleDirectory);

        for(File file : sampleFiles){
            logger.info(String.format("%s | %s | %s | %s", file.getParentFile().getCanonicalPath(), file.getName(), file.length(), file.isDirectory()));
        }

        assertEquals("Check file count", 4l, sampleFiles.length);
    }

}
