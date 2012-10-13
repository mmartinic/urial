package org.mmartinic.urial.util;

import org.junit.Assert;
import org.junit.Test;

public class NamingUtilsTest {

    @Test
    public void testAreSeriesNamesEqual() {
        String named = "Community";
        String unnamed = "Community";
        Assert.assertTrue(NamingUtils.areSeriesNamesEqual(named, unnamed));

        named = "Terminator: The Sarah Connor Chronicles";
        unnamed = "Terminator.The.Sarah.Connor.Chronicles";
        Assert.assertTrue(NamingUtils.areSeriesNamesEqual(named, unnamed));

        named = "Top Gear";
        unnamed = "Top Gear - [";
        Assert.assertTrue(NamingUtils.areSeriesNamesEqual(named, unnamed));

        named = "The Wire";
        unnamed = "The Shield";
        Assert.assertFalse(NamingUtils.areSeriesNamesEqual(named, unnamed));

        named = "The Office (US)";
        unnamed = "The.Office.US";
        Assert.assertTrue(NamingUtils.areSeriesNamesEqual(named, unnamed));

        named = "Castle (2009)";
        unnamed = "Castle.2009";
        Assert.assertTrue(NamingUtils.areSeriesNamesEqual(named, unnamed));
    }

    @Test
    public void testGetSeasonNumberFromUnnamedFileName() {
        Assert.assertEquals(12, NamingUtils.getSeasonNumberFromUnnamedFileName("top.gear.s12e04.ws.pdtv.xvid-river.avi"));
    }

    @Test
    public void testGetEpisodeNumberFromUnnamedFileName() {
        Assert.assertEquals(4, NamingUtils.getEpisodeNumberFromUnnamedFileName("top.gear.s12e04.ws.pdtv.xvid-river.avi"));
    }

}
