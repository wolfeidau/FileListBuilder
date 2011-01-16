package au.id.wolfe.recursivefilelister.impl;

import au.id.wolfe.recursivefilelister.FileLister;
import au.id.wolfe.recursivefilelister.util.Assert;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import java.io.File;
import java.io.FilenameFilter;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

/**
 *
 * Default implementation of the file lister which implements basic file listing and exclusions.
 *
 * TODO add some sort of name filter.
 */
public class DefaultFileLister implements FileLister {

    Set<String> fileNameExclusions = Sets.newHashSet();

    public void configureFileNameExclusions(Set<String> fileNameExcludes) {
        fileNameExclusions = Sets.newHashSet(fileNameExcludes);
    }

    public File[] getFileArrayFromDirectory(final File file) {
        Assert.argumentIsTrue("File must be a directory.", file.isDirectory());

        FilenameFilter filter = new FilenameFilter() {
            public boolean accept(File dir, String name) {
                // accept if this name is NOT contained in the set.
                return !fileNameExclusions.contains(name);
            }
        };

        return file.listFiles(filter);
    }
}
