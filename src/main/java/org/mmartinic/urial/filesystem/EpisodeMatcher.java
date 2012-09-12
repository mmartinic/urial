package org.mmartinic.urial.filesystem;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.mmartinic.urial.model.Episode;
import org.mmartinic.urial.model.RenameResult;
import org.mmartinic.urial.model.UnnamedEpisode;
import org.mmartinic.urial.service.EpisodeService;
import org.mmartinic.urial.util.NamingUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

/**
 * 
 * Pairs unnamed and named episodes
 */
@Component
public class EpisodeMatcher {

    private static final Logger logger = LogManager.getLogger(EpisodeMatcher.class);

    private final UnnamedEpisodesSearcher m_unnamedEpisodesSearcher;
    private final EpisodeService m_episodeService;
    private final double m_minMatchPercentage;
    private final long m_pollInterval;

    @Autowired
    public EpisodeMatcher(final UnnamedEpisodesSearcher p_unnamedEpisodesSearcher, final EpisodeService p_episodeService,
            @Value("${min.match.percentage}") final double p_minMatchPercentage, @Value("${poll.interval}") final long p_pollInterval) {
        Assert.notNull(p_unnamedEpisodesSearcher);
        Assert.notNull(p_episodeService);
        Assert.isTrue(p_minMatchPercentage >= 0, "Minimal match percantage is less than 0");
        Assert.isTrue(p_pollInterval > 0, "Poll interval is less than 0");

        m_unnamedEpisodesSearcher = p_unnamedEpisodesSearcher;
        m_episodeService = p_episodeService;
        m_minMatchPercentage = p_minMatchPercentage;
        m_pollInterval = p_pollInterval;
    }

    public long getPollInterval() {
        return m_pollInterval;
    }

    public RenameResult pollAndRename() {
        logger.info("New pollAndRename iteration");
        Set<UnnamedEpisode> unnamedEpisodes = m_unnamedEpisodesSearcher.getUnnamedEpisodes();
        return renameEpisodes(unnamedEpisodes);
    }

    private RenameResult renameEpisodes(final Set<UnnamedEpisode> p_unnamedEpisodes) {
        RenameResult renameResult = new RenameResult();
        if (!CollectionUtils.isEmpty(p_unnamedEpisodes)) {
            Map<UnnamedEpisode, Episode> episodeMap = matchEpisodes(p_unnamedEpisodes);
            for (UnnamedEpisode unnamedEpisode : episodeMap.keySet()) {
                Episode episode = episodeMap.get(unnamedEpisode);

                if (episode == null) {
                    logger.info("No name found for " + unnamedEpisode);
                    renameResult.addNoName(unnamedEpisode);
                    continue;
                }

                boolean success = NamingUtils.rename(unnamedEpisode, episode);
                if (success) {
                    logger.info("Renamed " + unnamedEpisode + " to " + episode);
                    renameResult.addSuccess(episode);
                }
                else {
                    renameResult.addFail(unnamedEpisode);
                }
            }
        }
        return renameResult;
    }

    private Map<UnnamedEpisode, Episode> matchEpisodes(final Set<UnnamedEpisode> p_unnamedEpisodes) {
        Set<Episode> episodes = m_episodeService.getEpisodes();
        Map<UnnamedEpisode, Episode> episodeMap = new LinkedHashMap<UnnamedEpisode, Episode>();
        for (UnnamedEpisode unnamedEpisode : p_unnamedEpisodes) {
            for (Episode episode : episodes) {
                if (match(episode, unnamedEpisode)) {
                    logger.info("Matched " + unnamedEpisode + " with " + episode);
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
}
