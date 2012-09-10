package org.mmartinic.urial.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.Assert;

public class RenameResult {

    private final List<Episode> m_successList;
    private final List<UnnamedEpisode> m_failList;
    private final List<UnnamedEpisode> m_noNameList;

    public RenameResult() {
        m_successList = new ArrayList<Episode>();
        m_failList = new ArrayList<UnnamedEpisode>();
        m_noNameList = new ArrayList<UnnamedEpisode>();
    }

    public void addSuccess(final Episode p_episode) {
        Assert.notNull(p_episode);
        m_successList.add(p_episode);
    }

    public void addFail(final UnnamedEpisode p_unnamedEpisode) {
        Assert.notNull(p_unnamedEpisode);
        m_failList.add(p_unnamedEpisode);
    }

    public void addNoName(final UnnamedEpisode p_unnamedEpisode) {
        Assert.notNull(p_unnamedEpisode);
        m_noNameList.add(p_unnamedEpisode);
    }

    public String getSuccessResult() {
        StringBuffer stringBuffer = new StringBuffer();
        for (Episode episode : m_successList) {
            stringBuffer.append("Rename success for: ");
            stringBuffer.append(episode);
            stringBuffer.append("\n");
        }
        return stringBuffer.toString();
    }

    public String getFailResult() {
        StringBuffer stringBuffer = new StringBuffer();
        for (UnnamedEpisode unnamedEpisode : m_failList) {
            stringBuffer.append("Rename failed for: ");
            stringBuffer.append(unnamedEpisode);
            stringBuffer.append("\n");
        }
        return stringBuffer.toString();
    }

    public String getNoNameResult() {
        StringBuffer stringBuffer = new StringBuffer();
        for (UnnamedEpisode unnamedEpisode : m_noNameList) {
            stringBuffer.append("No name for: ");
            stringBuffer.append(unnamedEpisode);
            stringBuffer.append("\n");
        }
        return stringBuffer.toString();
    }

    @Override
    public String toString() {
        return getSuccessResult() + getFailResult() + getNoNameResult();
    }
}
