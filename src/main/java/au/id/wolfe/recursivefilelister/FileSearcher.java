package au.id.wolfe.recursivefilelister;

import au.id.wolfe.recursivefilelister.impl.DefaultFileLister;
import au.id.wolfe.recursivefilelister.util.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 */
public class FileSearcher {
    final static Logger logger = LoggerFactory.getLogger(DefaultFileLister.class);

    FileLister fileLister;

    ExecutorService executor = Executors.newFixedThreadPool(10);

    public void startMyApplication(String basePath) {
        logger.info("Processing started");

        File basePathFile = new File(basePath);

        Assert.argumentIsTrue("BasePath exists", basePathFile.exists());

        File[] files = fileLister.getFileArrayFromDirectory(new File(basePath));



        logger.info("Processing finished");
    }

    public static void main(String[] args) {

        logger.info("Application Started");

        if (args.length == 1) {
            String basePath = args[0];

            new FileSearcher().startMyApplication(basePath);

        } else {
            logger.error("FileSearch directory");
        }



    }


}
