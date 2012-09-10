package org.mmartinic.urial.model;

import java.io.File;
import java.io.StringWriter;
import java.util.Formatter;

import org.mmartinic.urial.util.NamingUtils;
import org.springframework.util.Assert;

public class UnnamedEpisode {

    private final String m_seriesName;
    private final int m_seasonNumber;
    private final int m_number;
    private final File m_episodeFile;

    public UnnamedEpisode(final File p_file) {
        this(NamingUtils.getSeriesNameFromUnnamedFileName(p_file.getName()), NamingUtils.getSeasonNumberFromUnnamedFileName(p_file.getName()), NamingUtils
                .getEpisodeNumberFromUnnamedFileName(p_file.getName()), p_file);
    }

    public UnnamedEpisode(final String p_seriesName, final int p_seasonNumber, final int p_number, final File p_episodeFile) {
        Assert.hasText(p_seriesName);
        Assert.isTrue(p_seasonNumber >= 0);
        Assert.isTrue(p_number >= 0);
        Assert.notNull(p_episodeFile);
        m_seriesName = p_seriesName;
        m_seasonNumber = p_seasonNumber;
        m_number = p_number;
        m_episodeFile = p_episodeFile;
    }

    public String getSeriesName() {
        return m_seriesName;
    }

    public int getSeasonNumber() {
        return m_seasonNumber;
    }

    public int getNumber() {
        return m_number;
    }

    public File getEpisodeFile() {
        return m_episodeFile;
    }

    @Override
    public String toString() {
        StringWriter stringWriter = new StringWriter();
        Formatter formatter = new Formatter(stringWriter);
        formatter.format("[ %s ][ %02dx%02d ]", getSeriesName(), getSeasonNumber(), getNumber());
        return stringWriter.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((m_episodeFile == null) ? 0 : m_episodeFile.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object p_obj) {
        if (this == p_obj) {
            return true;
        }
        if (p_obj == null) {
            return false;
        }
        if (!(p_obj instanceof UnnamedEpisode)) {
            return false;
        }
        UnnamedEpisode other = (UnnamedEpisode) p_obj;
        if (m_episodeFile == null) {
            if (other.m_episodeFile != null) {
                return false;
            }
        }
        else if (!m_episodeFile.equals(other.m_episodeFile)) {
            return false;
        }
        return true;
    }

}
