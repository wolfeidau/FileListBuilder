package au.id.wolfe.recursivefilelister;

import au.id.wolfe.recursivefilelister.digest.FileChecksumBuilder;
import au.id.wolfe.recursivefilelister.digest.FileChecksumException;
import au.id.wolfe.recursivefilelister.digest.MessageDigestFileChecksumBuilder;
import au.id.wolfe.recursivefilelister.impl.DefaultFileLister;
import au.id.wolfe.recursivefilelister.util.Assert;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.concurrent.*;

/**
 *
 */
public class FileSearcher {
    final static Logger logger = LoggerFactory.getLogger(DefaultFileLister.class);

    BlockingQueue<File> queue = new LinkedBlockingQueue<File>(100);


    FileLister fileLister;

    FileChecksumBuilder fileChecksumBuilder;

    ExecutorService executor = Executors.newFixedThreadPool(20);

    static final int N_CONSUMERS = 10;

    public void startMyApplication(String basePath) throws NoSuchAlgorithmException {
        logger.info("Processing started");

        File basePathFile = new File(basePath);

        Assert.argumentIsTrue("BasePath exists", basePathFile.exists());

        fileLister = new DefaultFileLister();
        fileChecksumBuilder = new MessageDigestFileChecksumBuilder("MD5");

        fileLister.configureFileNameExclusions(Sets.<String>newHashSet("~snapshot"));

        Future<String> resultFileCrawler = executor.submit(new FileCrawler(queue, fileLister, basePathFile));

        List<Future<String>> resultIndexerList = Lists.newArrayList();

        for (int i = 0; i < N_CONSUMERS; i++) {
            resultIndexerList.add(executor.submit(new Indexer(queue, LoggerFactory.getLogger("file.LOG"), fileChecksumBuilder)));
        }

        try {
            logger.info(resultFileCrawler.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        logger.info("Waiting for completion");

        while (!queue.isEmpty()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        for (Future<String> resultIndexer : resultIndexerList) {
            resultIndexer.cancel(true);
        }

        executor.shutdown();

        logger.info("Processing finished");
    }

    public static void main(String[] args) {

        logger.info("Application Started");

        if (args.length == 1) {
            String basePath = args[0];

            try {
                new FileSearcher().startMyApplication(basePath);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }

        } else {
            logger.error("FileSearch directory");
        }

    }

    public class FileCrawler implements Callable<String> {

        private final BlockingQueue<File> fileQueue;
        private final FileLister fileLister;
        private final File root;

        public FileCrawler(BlockingQueue<File> fileQueue, FileLister fileLister, File root) {
            this.fileQueue = fileQueue;
            this.fileLister = fileLister;
            this.root = root;
        }

        public String call() throws Exception {
            try {
                crawl(root);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            return "DONE";
        }

        private void crawl(File root) throws InterruptedException {
            File[] entries = fileLister.getFileArrayFromDirectory(root);
            if (entries != null) {
                for (File entry : entries)
                    if (entry.isDirectory())
                        crawl(entry);
                    else
                        fileQueue.put(entry);
            }
        }

    }

    public class Indexer implements Callable<String> {

        private final BlockingQueue<File> queue;
        private final Logger fileLogger;
        private final FileChecksumBuilder fileChecksumBuilder;


        public Indexer(BlockingQueue<File> queue, Logger fileLogger, FileChecksumBuilder fileChecksumBuilder) {
            this.queue = queue;
            this.fileLogger = fileLogger;
            this.fileChecksumBuilder = fileChecksumBuilder;
        }

        public String call() throws Exception {
            try {
                while (true)
                    indexFile(queue.take());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            return "DONE";
        }

        private void indexFile(File file) throws IOException {
            String checksum = "";

            try {
                checksum = fileChecksumBuilder.calculate(file);
            } catch (FileChecksumException e) {
                // do nothing
            }

            fileLogger.info(String.format("%s | %s | %s | %s | %s | %s", file.getParentFile().getCanonicalPath(), file.getName(), file.length(), file.isDirectory(), file.lastModified(), checksum));
        }
    }


}
