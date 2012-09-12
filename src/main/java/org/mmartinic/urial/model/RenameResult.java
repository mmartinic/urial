package org.mmartinic.urial.model;

import java.util.HashSet;
import java.util.Set;

import org.springframework.util.Assert;

public class RenameResult {

    private final Set<Episode> m_successSet;
    private final Set<UnnamedEpisode> m_failSet;
    private final Set<UnnamedEpisode> m_noNameSet;

    public RenameResult() {
        m_successSet = new HashSet<>();
        m_failSet = new HashSet<>();
        m_noNameSet = new HashSet<>();
    }

    public void addSuccess(final Episode p_episode) {
        Assert.notNull(p_episode);
        m_successSet.add(p_episode);
    }

    public void addFail(final UnnamedEpisode p_unnamedEpisode) {
        Assert.notNull(p_unnamedEpisode);
        m_failSet.add(p_unnamedEpisode);
    }

    public void addNoName(final UnnamedEpisode p_unnamedEpisode) {
        Assert.notNull(p_unnamedEpisode);
        m_noNameSet.add(p_unnamedEpisode);
    }

    public Set<Episode> getSuccessSet() {
        return m_successSet;
    }

    public Set<UnnamedEpisode> getFailSet() {
        return m_failSet;
    }

    public Set<UnnamedEpisode> getNoNameSet() {
        return m_noNameSet;
    }

    public String getSuccessResultString() {
        StringBuffer stringBuffer = new StringBuffer();
        for (Episode episode : m_successSet) {
            stringBuffer.append("Rename success for: ");
            stringBuffer.append(episode);
            stringBuffer.append("\n");
        }
        return stringBuffer.toString();
    }

    public String getFailResultString() {
        StringBuffer stringBuffer = new StringBuffer();
        for (UnnamedEpisode unnamedEpisode : m_failSet) {
            stringBuffer.append("Rename failed for: ");
            stringBuffer.append(unnamedEpisode);
            stringBuffer.append("\n");
        }
        return stringBuffer.toString();
    }

    public String getNoNameResultString() {
        StringBuffer stringBuffer = new StringBuffer();
        for (UnnamedEpisode unnamedEpisode : m_noNameSet) {
            stringBuffer.append("No name for: ");
            stringBuffer.append(unnamedEpisode);
            stringBuffer.append("\n");
        }
        return stringBuffer.toString();
    }

    @Override
    public String toString() {
        return getSuccessResultString() + getFailResultString() + getNoNameResultString();
    }
}
