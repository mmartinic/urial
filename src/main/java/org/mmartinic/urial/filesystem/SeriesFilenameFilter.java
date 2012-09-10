package org.mmartinic.urial.filesystem;

import java.io.File;

import org.apache.commons.io.IOCase;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.mmartinic.urial.util.NamingUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public class SeriesFilenameFilter implements IOFileFilter {

    private final SuffixFileFilter m_suffixFileFilter;

    @Autowired
    public SeriesFilenameFilter(@Value("${file.extensions}") final String[] p_fileExtensions) {
        Assert.notEmpty(p_fileExtensions);
        Assert.noNullElements(p_fileExtensions);

        m_suffixFileFilter = new SuffixFileFilter(toSuffixes(p_fileExtensions), IOCase.INSENSITIVE);
    }

    @Override
    public boolean accept(final File p_dir, final String p_name) {
        Assert.hasText(p_name);

        return m_suffixFileFilter.accept(p_dir, p_name) && NamingUtils.isFileNameUnnamed(p_name);
    }

    @Override
    public boolean accept(final File p_file) {
        Assert.notNull(p_file);

        return accept(null, p_file.getName());
    }

    // -----------------------------------------------------------------------
    /**
     * Converts an array of file extensions to suffixes for use with IOFileFilters.
     * 
     * @param p_extensions
     *            an array of extensions. Format: {"java", "xml"}
     * @return an array of suffixes. Format: {".java", ".xml"}
     */
    private static String[] toSuffixes(final String[] p_extensions) {
        String[] suffixes = new String[p_extensions.length];
        for (int i = 0; i < p_extensions.length; i++) {
            suffixes[i] = "." + p_extensions[i];
        }
        return suffixes;
    }
}
