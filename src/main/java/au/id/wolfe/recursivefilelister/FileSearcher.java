package au.id.wolfe.recursivefilelister;

import au.id.wolfe.recursivefilelister.impl.DefaultFileLister;
import au.id.wolfe.recursivefilelister.util.Assert;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.*;

/**
 *
 */
public class FileSearcher {
    final static Logger logger = LoggerFactory.getLogger(DefaultFileLister.class);

    BlockingQueue<File> queue = new LinkedBlockingQueue<File>(100);


    FileLister fileLister;

    ExecutorService executor = Executors.newFixedThreadPool(20);

    static final int N_CONSUMERS = 10;

    public void startMyApplication(String basePath) {
        logger.info("Processing started");

        File basePathFile = new File(basePath);

        Assert.argumentIsTrue("BasePath exists", basePathFile.exists());

        FileFilter filter = new FileFilter() {
            public boolean accept(File file) {
                return true;
            }
        };

        Future<String> resultFileCrawler = executor.submit(new FileCrawler(queue, filter, basePathFile));

        List<Future<String>> resultIndexerList = Lists.newArrayList();

        for (int i = 0; i < N_CONSUMERS; i++){
           resultIndexerList.add(executor.submit(new Indexer(queue)));
        }

        try {
            logger.info(resultFileCrawler.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        //File[] files = fileLister.getFileArrayFromDirectory(new File(basePath));


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

    public class FileCrawler implements Callable<String> {

        private final BlockingQueue<File> fileQueue;
        private final FileFilter fileFilter;
        private final File root;

        public FileCrawler(BlockingQueue<File> fileQueue, FileFilter fileFilter, File root) {
            this.fileQueue = fileQueue;
            this.fileFilter = fileFilter;
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
            File[] entries = root.listFiles(fileFilter);
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

        public Indexer(BlockingQueue<File> queue) {
            this.queue = queue;
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
            logger.info(String.format("%s | %s | %s | %s", file.getParentFile().getCanonicalPath(), file.getName(), file.length(), file.isDirectory()));
        }
    }


}
