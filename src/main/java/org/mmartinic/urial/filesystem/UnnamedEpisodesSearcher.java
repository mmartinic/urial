package org.mmartinic.urial.filesystem;

import java.io.File;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FalseFileFilter;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.mmartinic.urial.model.UnnamedEpisode;
import org.springframework.util.Assert;

/**
 * Searches for unnamed episode files
 * 
 * @author Marin Martinic
 * @version $Revision: 41 $, $Date: 2008-12-17 23:32:43 +0100 (Wed, 17 Dec 2008) $, $Author: mmartinic $
 */
public class UnnamedEpisodesSearcher {

    private final File m_searchFolder;
    private final IOFileFilter m_seriesFilenameFilter;
    private boolean m_searchSubfolders = false;

    public UnnamedEpisodesSearcher(final IOFileFilter p_seriesFilenameFilter, final String p_searchFolderPath) {
        Assert.notNull(p_searchFolderPath, "Search folder path is null");
        Assert.notNull(p_seriesFilenameFilter, "SeriesFilenameFilter is null");

        m_searchFolder = new File(p_searchFolderPath);
        if (!m_searchFolder.exists()) {
            throw new IllegalArgumentException("Search folder does not exist");
        }
        if (!m_searchFolder.isDirectory()) {
            throw new IllegalArgumentException("Search folder is not a folder");
        }

        m_seriesFilenameFilter = p_seriesFilenameFilter;
    }

    public Set<UnnamedEpisode> getUnnamedEpisodes() {

        Collection<File> files = getUnnamedFiles();
        Set<UnnamedEpisode> unnamed = new LinkedHashSet<UnnamedEpisode>();
        for (File file : files) {
            try {
                UnnamedEpisode episode = new UnnamedEpisode(file);
                unnamed.add(episode);
            }
            catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
        return unnamed;
    }

    @SuppressWarnings("unchecked")
    private Set<File> getUnnamedFiles() {
        Collection<File> files;
        if (m_searchSubfolders) {
            files = FileUtils.listFiles(m_searchFolder, m_seriesFilenameFilter, TrueFileFilter.TRUE);
        }
        else {
            files = FileUtils.listFiles(m_searchFolder, m_seriesFilenameFilter, FalseFileFilter.FALSE);
        }
        return new LinkedHashSet<File>(files);
    }

    public void setSearchSubfolders(final boolean p_searchSubfolders) {
        m_searchSubfolders = p_searchSubfolders;
    }
}
