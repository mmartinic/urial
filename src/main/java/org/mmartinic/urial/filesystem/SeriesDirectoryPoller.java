package org.mmartinic.urial.filesystem;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.mmartinic.urial.model.RenameResult;
import org.springframework.util.Assert;

public class SeriesDirectoryPoller {

    private final EpisodeMatcher m_episodeMatcher;
    private final long m_pollInterval;

    public SeriesDirectoryPoller(final EpisodeMatcher p_episodeMatcher, final long p_pollInterval) {
        super();
        Assert.notNull(p_episodeMatcher);
        Assert.isTrue(p_pollInterval > 0);

        m_episodeMatcher = p_episodeMatcher;
        m_pollInterval = p_pollInterval;
    }

    public void run() {
        while (true) {
            RenameResult renameResult = pollAndRename();
            if (renameResult != null) {
                System.out.println(new Date() + " ------------------------------------------------");
                System.out.println(renameResult);
            }
            try {
                TimeUnit.SECONDS.sleep(m_pollInterval);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public RenameResult pollAndRename() {
        return m_episodeMatcher.rename();
    }

    public long getPollInterval() {
        return m_pollInterval;
    }
}
