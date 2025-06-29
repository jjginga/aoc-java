package com.jjginga.aoc.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    private static final Logger LOGGER = LoggerFactory.getLogger(Utils.class);
    private static final Pattern DAY_NUMBER_PATTERN = Pattern.compile("\\d+");

    private Utils() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated.");
    }

    public static void logResults(String day, String solutionPart1, String solutionPart2) {
        String dayNumber = extractDayNumber(day);
        LOGGER.info("Day {}, Part 1: {}", dayNumber, solutionPart1);
        LOGGER.info("Day {}, Part 2: {}", dayNumber, solutionPart2);
    }

    private static String extractDayNumber(String day) {
        Matcher matcher = DAY_NUMBER_PATTERN.matcher(day);
        return matcher.find() ? matcher.group() : day;
    }
}
