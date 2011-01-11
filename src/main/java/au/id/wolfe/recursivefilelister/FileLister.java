package au.id.wolfe.recursivefilelister;


import java.io.File;
import java.util.Set;

/**
 *
 */
public interface FileLister {

    /**
     * Configure a match string for filtering.
     *
     * @param fileNameExcludes List of names to be excluded.
     */
    public void configureFileNameExclusions(Set<String> fileNameExcludes);

    /**
     * @param file Directory to list.
     * @return Array of File listing the files within.
     * @throws IllegalArgumentException when argument is not a directory.
     */
    File[] getFileArrayFromDirectory(File file);
}
