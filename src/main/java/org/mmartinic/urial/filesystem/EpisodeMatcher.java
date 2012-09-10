package org.mmartinic.urial.filesystem;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.mmartinic.urial.model.Episode;
import org.mmartinic.urial.model.RenameResult;
import org.mmartinic.urial.model.UnnamedEpisode;
import org.mmartinic.urial.service.EpisodeService;
import org.mmartinic.urial.util.NamingUtils;
import org.mmartinic.urial.util.RenameHistory;
import org.springframework.util.Assert;

/**
 * 
 * Pairs unnamed and named episodes
 */
public class EpisodeMatcher {

    private final UnnamedEpisodesSearcher m_unnamedEpisodesSearcher;
    private final EpisodeService m_episodeService;
    private final double m_minMatchPercentage;

    public EpisodeMatcher(final UnnamedEpisodesSearcher p_unnamedEpisodesSearcher, final EpisodeService p_episodeService, final double p_minMatchPercentage) {
        Assert.notNull(p_unnamedEpisodesSearcher);
        Assert.notNull(p_episodeService);
        Assert.isTrue(p_minMatchPercentage >= 0, "Minimal match percantage is less than 0");

        m_unnamedEpisodesSearcher = p_unnamedEpisodesSearcher;
        m_episodeService = p_episodeService;
        m_minMatchPercentage = p_minMatchPercentage;
    }

    private Map<UnnamedEpisode, Episode> matchEpisodes(final Set<UnnamedEpisode> p_unnamedEpisodes) {
        Set<Episode> episodes = m_episodeService.getEpisodes();
        Map<UnnamedEpisode, Episode> episodeMap = new LinkedHashMap<UnnamedEpisode, Episode>();
        for (UnnamedEpisode unnamedEpisode : p_unnamedEpisodes) {
            for (Episode episode : episodes) {
                if (match(episode, unnamedEpisode)) {
                    episodeMap.put(unnamedEpisode, episode);
                    break;
                }
                episodeMap.put(unnamedEpisode, null);
            }
        }
        return episodeMap;
    }

    private boolean match(final Episode p_episode, final UnnamedEpisode p_unnamedEpisode) {
        if (p_episode.getEpisodeNumber() != p_unnamedEpisode.getNumber()) {
            return false;
        }

        if (p_episode.getSeasonNumber() != p_unnamedEpisode.getSeasonNumber()) {
            return false;
        }

        return NamingUtils.areSeriesNamesEqual(p_episode.getShowName(), p_unnamedEpisode.getSeriesName(), m_minMatchPercentage);
    }

    public RenameResult rename() {
        Set<UnnamedEpisode> unnamedEpisodes = m_unnamedEpisodesSearcher.getUnnamedEpisodes();
        if (RenameHistory.isDifferent(unnamedEpisodes)) {
            RenameHistory.setFiles(unnamedEpisodes);
            return renameEpisodes(unnamedEpisodes);
        }
        return null;
    }

    private RenameResult renameEpisodes(final Set<UnnamedEpisode> p_unnamedEpisodes) {
        Map<UnnamedEpisode, Episode> episodeMap = matchEpisodes(p_unnamedEpisodes);
        Set<UnnamedEpisode> keyset = episodeMap.keySet();
        RenameResult renameResult = new RenameResult();
        for (UnnamedEpisode unnamedEpisode : keyset) {
            Episode episode = episodeMap.get(unnamedEpisode);

            if (episode == null) {
                renameResult.addNoName(unnamedEpisode);
                continue;
            }

            boolean success = NamingUtils.rename(unnamedEpisode, episode);
            if (success) {
                renameResult.addSuccess(episode);
            }
            else {
                renameResult.addFail(unnamedEpisode);
            }
        }
        return renameResult;
    }
}
