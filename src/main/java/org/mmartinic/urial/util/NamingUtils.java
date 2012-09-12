package org.mmartinic.urial.util;

import java.io.File;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Formatter;
import java.util.Locale;
import java.util.regex.Pattern;

import org.apache.commons.lang3.text.WordUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.mmartinic.urial.model.Episode;
import org.mmartinic.urial.model.UnnamedEpisode;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

public final class NamingUtils {

    private static final Logger logger = LogManager.getLogger(NamingUtils.class);

    private static final String C_FORBIDDEN_FILE_CHARACTERS_REGEX = "[\\\\/:*?\"<>|]";
    private static final String C_EPISODE_NAME_DELIMITERS_REGEX = "[._\\[\\]-]";
    private static final String C_UNNAMED_EPISODE_REGEX = ".+[sS]?\\d+[eExX]+\\d+.+";
    private static final String C_UNNAMED_EPISODE_NUMBERING_REGEX = "[sS]?\\d+[eExX]+\\d+";
    private static final String C_NAMED_EPISODE_DELIMITER = "-";
    private static final String C_SEASON_EPISODE_NUMBER_DELIMITER = "x";
    private static final int C_NAME_FORMAT_SEASON_NUMBER_LIMIT = 10;
    private static final double C_DEFAULT_MIN_MATCH_PERCENTAGE = 0.9;

    public static final SimpleDateFormat C_AIR_DATE_FORMAT = new SimpleDateFormat("dd-MMMM-yyyy", Locale.ENGLISH);

    private NamingUtils() {

    }

    public static boolean areSeriesNamesEqual(final String p_named, final String p_unnamed) {
        return areSeriesNamesEqual(p_named, p_unnamed, C_DEFAULT_MIN_MATCH_PERCENTAGE);
    }

    public static boolean areSeriesNamesEqual(final String p_named, final String p_unnamed, final double p_minMatchPercentage) {
        Assert.hasText(p_named);
        Assert.hasText(p_unnamed);
        Assert.isTrue(p_minMatchPercentage >= 0);

        String unnamed = p_unnamed;
        String named = p_named;

        named = replaceAllForbiddenChars(named);
        named = named.trim();
        named = removeDuplicateSpaces(named);
        named = named.toLowerCase();

        unnamed = replaceAllDelimiters(unnamed);
        unnamed = unnamed.trim();
        unnamed = removeDuplicateSpaces(unnamed);
        unnamed = unnamed.toLowerCase();

        return StringHelper.areStringsFuzzyEqual(named, unnamed, p_minMatchPercentage);
    }

    private static String replaceAllDelimiters(final String p_string) {
        return StringHelper.replaceAll(p_string, C_EPISODE_NAME_DELIMITERS_REGEX, " ");
    }

    private static String replaceAllForbiddenChars(final String p_string, final String p_newValue) {
        return StringHelper.replaceAll(p_string, C_FORBIDDEN_FILE_CHARACTERS_REGEX, p_newValue);
    }

    private static String replaceAllForbiddenChars(final String p_string) {
        return replaceAllForbiddenChars(p_string, " ");
    }

    private static String removeDuplicateSpaces(final String p_string) {
        return p_string.replaceAll("\\s+", " ").trim();
    }

    public static boolean isFileNameUnnamed(final String p_fileName) {
        Assert.hasText(p_fileName);

        int spaceCount = StringHelper.countAllOccurances(p_fileName, " ");
        if (spaceCount > 0) {
            return false;
        }

        int delimitersCount = StringHelper.countAllOccurances(p_fileName, C_EPISODE_NAME_DELIMITERS_REGEX);
        if (delimitersCount < 2) {
            return false;
        }

        return Pattern.matches(C_UNNAMED_EPISODE_REGEX, p_fileName);
    }

    public static int getSeasonNumberFromUnnamedFileName(final String p_fileName) {
        Assert.hasText(p_fileName);

        int number = 0;
        String numbering = StringHelper.getFirstOccurance(p_fileName, C_UNNAMED_EPISODE_NUMBERING_REGEX);
        String numberString = StringHelper.getNthOccurance(numbering, "\\d+", 1);
        try {
            number = Integer.parseInt(numberString);
        }
        catch (NumberFormatException e) {
            // ignore
        }
        return number;
    }

    public static int getEpisodeNumberFromUnnamedFileName(final String p_fileName) {
        Assert.hasText(p_fileName);

        int number = 0;
        String numbering = StringHelper.getFirstOccurance(p_fileName, C_UNNAMED_EPISODE_NUMBERING_REGEX);
        String numberString = StringHelper.getNthOccurance(numbering, "\\d+", 2);
        try {
            number = Integer.parseInt(numberString);
        }
        catch (NumberFormatException e) {
            // ignore
        }
        return number;
    }

    public static String getSeriesNameFromUnnamedFileName(final String p_fileName) {
        Assert.hasText(p_fileName);

        return StringHelper.substringBefore(p_fileName, C_UNNAMED_EPISODE_NUMBERING_REGEX);
    }

    public static String createFileName(final Episode p_episode) {
        Assert.notNull(p_episode);

        StringWriter stringWriter = new StringWriter();
        Formatter formatter = new Formatter(stringWriter);

        String seriesName = normalizeName(p_episode.getShowName());
        String name = normalizeName(p_episode.getEpisodeName());

        if (p_episode.getSeasonNumber() < C_NAME_FORMAT_SEASON_NUMBER_LIMIT) {
            formatter.format("%s - %d%02d - %s", seriesName, p_episode.getSeasonNumber(), p_episode.getEpisodeNumber(), name);
        }
        else {
            formatter.format("%s - %d%s%02d - %s", seriesName, p_episode.getSeasonNumber(), C_SEASON_EPISODE_NUMBER_DELIMITER,
                    p_episode.getEpisodeNumber(), name);
        }

        formatter.close();
        return stringWriter.toString();
    }

    private static String normalizeName(final String p_name) {
        String name = p_name;
        name = replaceAllForbiddenChars(name, C_NAMED_EPISODE_DELIMITER);
        name = removeDuplicateSpaces(name);
        name = WordUtils.capitalize(name);
        return name;
    }

    public static boolean rename(final UnnamedEpisode p_unnamedEpisode, final Episode p_episode) {

        Assert.notNull(p_unnamedEpisode);
        Assert.notNull(p_episode);

        File file = p_unnamedEpisode.getEpisodeFile();
        String newName = createFileName(p_episode) + "." + StringUtils.getFilenameExtension(file.getName()).toLowerCase();

        File newFile = new File(file.getParent(), newName);

        boolean returnValue = false;
        try {
            returnValue = file.renameTo(newFile);
        }
        catch (Exception e) {
            logger.error("Error renaming file: " + file + " to new file: " + newFile, e);
        }

        return returnValue;
    }
}
