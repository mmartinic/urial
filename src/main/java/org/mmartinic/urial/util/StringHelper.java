package org.mmartinic.urial.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

public final class StringHelper {

    private StringHelper() {

    }

    /**
     * Fuzzy compares two strings
     * 
     * @param p_first
     * @param p_second
     * @param p_minMatchPercentage
     * @return
     */

    public static boolean areStringsFuzzyEqual(final String p_first, final String p_second, final double p_minMatchPercentage) {
        Assert.notNull(p_first);
        Assert.notNull(p_second);
        Assert.isTrue(p_minMatchPercentage >= 0);

        int len = p_first.length();
        if (p_second.length() > len) {
            len = p_second.length();
        }

        double levenshteinDistance = StringUtils.getLevenshteinDistance(p_first, p_second);
        double precentage = 1 - (levenshteinDistance / len);

        return precentage > p_minMatchPercentage;
    }

    public static int countAllOccurances(final String p_string, final String p_regex) {
        Assert.notNull(p_string);
        Assert.notNull(p_regex);

        Pattern pattern = Pattern.compile(p_regex);
        Matcher matcher = pattern.matcher(p_string);
        int count = 0;
        while (matcher.find()) {
            count++;
        }
        return count;
    }

    public static String substringBefore(final String p_string, final String p_regex) {
        Assert.notNull(p_string);
        Assert.notNull(p_regex);

        String[] parts = p_string.split(p_regex);
        if (parts.length < 2) {
            return null;
        }
        return parts[0];
    }

    public static String getFirstOccurance(final String p_string, final String p_regex) {
        Assert.notNull(p_string);
        Assert.notNull(p_regex);

        return getNthOccurance(p_string, p_regex, 1);
    }

    public static String getNthOccurance(final String p_string, final String p_regex, final int p_n) {
        Assert.notNull(p_string);
        Assert.notNull(p_regex);
        Assert.isTrue(p_n > 0);

        List<String> occurances = getAllOccurances(p_string, p_regex);
        if (p_n > occurances.size()) {
            return null;
        }
        else {
            return occurances.get(p_n - 1);
        }
    }

    public static List<String> getAllOccurances(final String p_string, final String p_regex) {
        Assert.notNull(p_string);
        Assert.notNull(p_regex);

        List<String> occurances = new ArrayList<String>();
        Pattern pattern = Pattern.compile(p_regex);
        Matcher matcher = pattern.matcher(p_string);
        while (matcher.find()) {
            occurances.add(matcher.group());
        }
        return occurances;
    }

    public static String replaceAll(final String p_string, final String p_regex, final String p_newValue) {
        Assert.notNull(p_string);
        Assert.notNull(p_regex);
        Assert.notNull(p_newValue);

        return p_string.replaceAll(p_regex, p_newValue);
    }
}
