package org.mmartinic.urial.util;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class StringHelperTest {

    @Test
    public void testAreStringsFuzzyEqual() {
        String first = "It's";
        String second = "Its";
        Assert.assertFalse(StringHelper.areStringsFuzzyEqual(first, second, 0.9));

        first = "Terminator: The Sarah Connor Chronicles";
        second = "Terminator The Sarah Connor Chronicles";
        Assert.assertTrue(StringHelper.areStringsFuzzyEqual(first, second, 0.9));

        first = "a";
        second = "b";
        Assert.assertFalse(StringHelper.areStringsFuzzyEqual(first, second, 0.9));
    }

    @Test
    public void testGetAllOccurances() {
        List<String> list = StringHelper.getAllOccurances(" Top Gear - \\[12x04]/ - 2:00_" + "*8.1?1.2\"3 <[R>iVE|R].avi", "[\\\\/:*?\"<>|]");
        for (String string : list) {
            System.out.println(string);
        }
    }

}
