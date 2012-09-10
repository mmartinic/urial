package org.mmartinic.urial.util;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.mmartinic.urial.model.UnnamedEpisode;
import org.springframework.util.Assert;

public final class RenameHistory {

    private static Set<UnnamedEpisode> c_unnamedEpisodes = new HashSet<UnnamedEpisode>();

    private RenameHistory() {
    }

    public static void remove(final UnnamedEpisode p_unnamedEpisode) {
        Assert.notNull(p_unnamedEpisode);

        c_unnamedEpisodes.remove(p_unnamedEpisode);
    }

    public static void setFiles(final Collection<UnnamedEpisode> p_unnamedEpisodes) {
        Assert.notEmpty(p_unnamedEpisodes);

        c_unnamedEpisodes = new HashSet<UnnamedEpisode>(p_unnamedEpisodes);
    }

    public static boolean isDifferent(final Set<UnnamedEpisode> p_unnamedEpisodes) {
        Assert.notEmpty(p_unnamedEpisodes);

        return !c_unnamedEpisodes.equals(p_unnamedEpisodes);
    }
}
