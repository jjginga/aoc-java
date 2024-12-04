package com.bestemic.aoc.year2024;

import com.bestemic.aoc.Solution;
import com.bestemic.aoc.utils.InputReader;
import com.bestemic.aoc.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

import static com.bestemic.aoc.utils.Constants.DAY_04;
import static com.bestemic.aoc.utils.Constants.YEAR_2024;

public class Day04 implements Solution {
    private static final Logger LOGGER = LoggerFactory.getLogger(Day04.class);

    /**
     * Expected output: 2530
     */
    @Override
    public String part1(List<String> input) {
        int rows = input.size();
        int cols = input.getFirst().length();
        int count = 0;

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (input.get(row).charAt(col) == 'X') {
                    count += countXMASFromStart(input, row, col);
                }
            }
        }

        return String.valueOf(count);
    }

    /**
     * Expected output: 1921
     */
    @Override
    public String part2(List<String> input) {
        int rows = input.size();
        int cols = input.getFirst().length();
        int count = 0;

        for (int row = 1; row < rows - 1; row++) {
            for (int col = 1; col < cols - 1; col++) {
                if (input.get(row).charAt(col) == 'A' && validCrossedMAS(input, row, col)) {
                    count++;
                }
            }
        }

        return String.valueOf(count);
    }

    private int countXMASFromStart(List<String> grid, int row, int col) {
        String pattern = "XMAS";
        int count = 0;

        int[] directions = {-1, 0, 1};
        for (int rowDirection : directions) {
            for (int colDirection : directions) {
                if (rowDirection == 0 && colDirection == 0) continue;

                if (isEnoughSpace(grid, row, col, rowDirection, colDirection, pattern.length()) && validatePattern(grid, row, col, rowDirection, colDirection, pattern)) {
                    count++;
                }
            }
        }
        return count;
    }

    private boolean isEnoughSpace(List<String> grid, int row, int col, int rowDirection, int colDirection, int patternLength) {
        int newRow = row + (patternLength - 1) * rowDirection;
        int newCol = col + (patternLength - 1) * colDirection;
        return newRow >= 0 && newRow < grid.size() && newCol >= 0 && newCol < grid.getFirst().length();
    }

    private boolean validatePattern(List<String> grid, int row, int col, int rowDirection, int colDirection, String pattern) {
        for (int i = 0; i < pattern.length(); i++) {
            int newRow = row + i * rowDirection;
            int newCol = col + i * colDirection;
            if (grid.get(newRow).charAt(newCol) != pattern.charAt(i)) {
                return false;
            }
        }
        return true;
    }

    private boolean validCrossedMAS(List<String> grid, int row, int col) {
        char topLeftChar = grid.get(row - 1).charAt(col - 1);
        char topRightChar = grid.get(row - 1).charAt(col + 1);
        char bottomLeftChar = grid.get(row + 1).charAt(col - 1);
        char bottomRightChar = grid.get(row + 1).charAt(col + 1);

        String positions = "" + topLeftChar + topRightChar + bottomLeftChar + bottomRightChar;
        List<String> validPatterns = List.of("MMSS", "MSMS", "SMSM", "SSMM");
        return validPatterns.contains(positions);
    }

    public static void main(String[] args) {
        try {
            List<String> input = InputReader.readLines(YEAR_2024, DAY_04);

            Solution solution = new Day04();
            String solutionPart1 = solution.part1(input);
            String solutionPart2 = solution.part2(input);

            Utils.logResults(DAY_04, solutionPart1, solutionPart2);
        } catch (IOException e) {
            LOGGER.error("Error while reading data: {}", e.getMessage(), e);
        }
    }
}
