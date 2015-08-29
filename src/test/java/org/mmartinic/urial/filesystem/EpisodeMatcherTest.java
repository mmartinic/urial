package org.mmartinic.urial.filesystem;

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

import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class EpisodeMatcherTest {

    @Test
    public void testRename() {
        IOFileFilter seriesFilenameFilter = new SeriesFilenameFilter("avi,srt,sub,mp4".split(","));
        UnnamedEpisodesSearcher searcher = new UnnamedEpisodesSearcher(seriesFilenameFilter, "build/resources/test/files");
        EpisodeService episodeService = Mockito.mock(EpisodeService.class);
        Episode episode1 = new Episode("Breaking Bad", "Gliding Over All", 5, 8, LocalDate.now());
        Episode episode2 = new Episode("Community", "A Fistful of Paintballs", 2, 23, LocalDate.now());
        Episode episode3 = new Episode("Game of Thrones", "Valar Morghulis", 2, 10, LocalDate.now());
        Episode episode4 =
                new Episode("Terminator: The Sarah Connor Chronicles", "Strange Things Happen at the One Two Point", 2, 10, LocalDate.now());
        Episode episode5 = new Episode("The Wire", "Hamsterdam", 3, 4, LocalDate.now());
        Episode episode6 = new Episode("The Office (US)", "Roy's Wedding", 9, 2, LocalDate.now());
        Episode episode7 = new Episode("Castle (2009)", "Secret's Safe With Me", 5, 3, LocalDate.now());
        Set<Episode> episodes = new HashSet<>();
        episodes.add(episode1);
        episodes.add(episode2);
        episodes.add(episode3);
        episodes.add(episode4);
        episodes.add(episode5);
        episodes.add(episode6);
        episodes.add(episode7);
        Mockito.when(episodeService.getEpisodes()).thenReturn(episodes);
        EpisodeMatcher matcher = new EpisodeMatcher(searcher, episodeService, 0.8, 1);

        RenameResult renameResult = matcher.pollAndRename();
        Assert.assertEquals(episodes, renameResult.getSuccessSet());
        Set<UnnamedEpisode> noNameExpected = new HashSet<>();
        noNameExpected.add(new UnnamedEpisode(new File("build/resources/test/files/Firefly.S02E01'(.avi")));
        Assert.assertEquals(noNameExpected, renameResult.getNoNameSet());
        Assert.assertTrue(CollectionUtils.isEmpty(renameResult.getFailSet()));
    }
}
