package org.mmartinic.urial.filesystem;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.io.filefilter.IOFileFilter;
import org.junit.Assert;
import org.junit.Test;
import org.mmartinic.urial.model.UnnamedEpisode;

public class UnnamedEpisodesSearcherTest {

    @Test
    public void testGetUnnamedFiles() {
        IOFileFilter seriesFilenameFilter = new SeriesFilenameFilter("avi,srt,sub".split(","));
        UnnamedEpisodesSearcher searcher = new UnnamedEpisodesSearcher(seriesFilenameFilter, "src/test/resources/files");
        Set<UnnamedEpisode> expected = new HashSet<>();
        expected.add(new UnnamedEpisode(new File("src/test/resources/files/Breaking.Bad.s05e08.HDTV.XviD-XOR.avi")));
        expected.add(new UnnamedEpisode(new File("src/test/resources/files/Community.s02e23.HDTV.XviD-XOR.avi")));
        expected.add(new UnnamedEpisode(new File("src/test/resources/files/Firefly.S02E01'(.avi")));
        expected.add(new UnnamedEpisode(new File("src/test/resources/files/Game.of.Thrones.S02E10.HDTV.XviD-XOR.avi")));
        expected.add(new UnnamedEpisode(new File("src/test/resources/files/Terminator.The.Sarah.Connor.Chronicles.S02E10.HDTV.XviD-XOR.avi")));
        expected.add(new UnnamedEpisode(new File("src/test/resources/files/The.Wire.S03E04.HDTV.XviD-XOR.avi")));
        Set<UnnamedEpisode> files = searcher.getUnnamedEpisodes();
        Assert.assertEquals(expected, files);
    }
}
