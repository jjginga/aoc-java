package com.bestemic.aoc.year2024;

import com.bestemic.aoc.Solution;
import com.bestemic.aoc.utils.InputReader;
import com.bestemic.aoc.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

import static com.bestemic.aoc.utils.Constants.DAY_02;
import static com.bestemic.aoc.utils.Constants.YEAR_2024;

public class Day02 implements Solution {
    private static final Logger LOGGER = LoggerFactory.getLogger(Day02.class);

    /**
     * Expected output: 639
     */
    @Override
    public String part1(List<String> input) {
        int safeReportCount = 0;

        for (String line : input) {
            String[] levels = line.split(" ");
            if (isSafe(levels)) {
                safeReportCount++;
            }
        }

        return String.valueOf(safeReportCount);
    }

    /**
     * Expected output: 674
     */
    @Override
    public String part2(List<String> input) {
        int safeReportCount = 0;

        for (String line : input) {
            String[] levels = line.split(" ");
            if (isSafe(levels) || canBeSafeWithOneRemoval(levels)) {
                safeReportCount++;
            }
        }

        return String.valueOf(safeReportCount);
    }


    private boolean isSafe(String[] levels) {
        boolean increasing = true;

        for (int i = 0; i < levels.length - 1; i++) {
            int current = Integer.parseInt(levels[i]);
            int next = Integer.parseInt(levels[i + 1]);
            int difference = Math.abs(current - next);

            if (difference == 0 || difference > 3) {
                return false;
            }

            if (i == 0) {
                increasing = current <= next;
            } else if ((increasing && current > next) || (!increasing && current < next)) {
                return false;
            }
        }

        return true;
    }

    private boolean canBeSafeWithOneRemoval(String[] levels) {
        for (int i = 0; i < levels.length; i++) {
            if (isSafe(removeLevel(levels, i))) {
                return true;
            }
        }
        return false;
    }

    private String[] removeLevel(String[] levels, int indexToRemove) {
        String[] modifiedLevels = new String[levels.length - 1];
        int modifiedLevelsIndex = 0;

        for (int i = 0; i < levels.length; i++) {
            if (i != indexToRemove) {
                modifiedLevels[modifiedLevelsIndex++] = levels[i];
            }
        }

        return modifiedLevels;
    }

    public static void main(String[] args) {
        try {
            List<String> input = InputReader.readLines(YEAR_2024, DAY_02);

            Solution solution = new Day02();
            String solutionPart1 = solution.part1(input);
            String solutionPart2 = solution.part2(input);

            Utils.logResults(DAY_02, solutionPart1, solutionPart2);
        } catch (IOException e) {
            LOGGER.error("Error while reading data: {}", e.getMessage(), e);
        }
    }
}
