package org.mmartinic.urial.filesystem;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Test;
import org.mmartinic.urial.model.Episode;
import org.mmartinic.urial.model.RenameResult;
import org.mmartinic.urial.model.UnnamedEpisode;
import org.mmartinic.urial.service.EpisodeService;
import org.mockito.Mockito;

public class EpisodeMatcherTest {

    @Test
    public void testRename() {
        IOFileFilter seriesFilenameFilter = new SeriesFilenameFilter("avi,srt,sub".split(","));
        UnnamedEpisodesSearcher searcher = new UnnamedEpisodesSearcher(seriesFilenameFilter, "target/test-classes/files");
        EpisodeService episodeService = Mockito.mock(EpisodeService.class);
        Episode episode1 = new Episode("Breaking Bad", "Gliding Over All", 5, 8, LocalDate.now());
        Episode episode2 = new Episode("Community", "A Fistful of Paintballs", 2, 23, LocalDate.now());
        Episode episode3 = new Episode("Game of Thrones", "Valar Morghulis", 2, 10, LocalDate.now());
        Episode episode4 =
                new Episode("Terminator: The Sarah Connor Chronicles", "Strange Things Happen at the One Two Point", 2, 10, LocalDate.now());
        Episode episode5 = new Episode("The Wire", "Hamsterdam", 3, 4, LocalDate.now());
        Set<Episode> episodes = new HashSet<>();
        episodes.add(episode1);
        episodes.add(episode2);
        episodes.add(episode3);
        episodes.add(episode4);
        episodes.add(episode5);
        Mockito.when(episodeService.getEpisodes()).thenReturn(episodes);
        EpisodeMatcher matcher = new EpisodeMatcher(searcher, episodeService, 0.9, 1);

        RenameResult renameResult = matcher.pollAndRename();
        Assert.assertEquals(episodes, renameResult.getSuccessSet());
        Set<UnnamedEpisode> noNameExpected = new HashSet<>();
        noNameExpected.add(new UnnamedEpisode(new File("target/test-classes/files/Firefly.S02E01'(.avi")));
        Assert.assertEquals(noNameExpected, renameResult.getNoNameSet());
        Assert.assertTrue(CollectionUtils.isEmpty(renameResult.getFailSet()));
    }
}
