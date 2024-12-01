package com.bestemic.aoc.year2024;

import com.bestemic.aoc.Solution;
import com.bestemic.aoc.utils.InputReader;
import com.bestemic.aoc.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.bestemic.aoc.utils.Constants.DAY_01;
import static com.bestemic.aoc.utils.Constants.YEAR_2024;

public class Day01 implements Solution {
    private static final Logger LOGGER = LoggerFactory.getLogger(Day01.class);

    /**
     * Expected output: 2375403
     */
    @Override
    public String part1(List<String> input) {
        List<Integer> leftColumn = new ArrayList<>();
        List<Integer> rightColumn = new ArrayList<>();
        extractColumns(input, leftColumn, rightColumn);

        Collections.sort(leftColumn);
        Collections.sort(rightColumn);

        int result = calculateSimilarity(leftColumn, rightColumn);
        return String.valueOf(result);
    }

    /**
     * Expected output: 23082277
     */
    @Override
    public String part2(List<String> input) {
        List<Integer> leftColumn = new ArrayList<>();
        List<Integer> rightColumn = new ArrayList<>();
        extractColumns(input, leftColumn, rightColumn);

        int result = calculateWeightedSimilarity(leftColumn, rightColumn);
        return String.valueOf(result);
    }

    private void extractColumns(List<String> input, List<Integer> leftColumn, List<Integer> rightColumn) {
        for (String line : input) {
            String[] numbers = line.split(" {3}");
            leftColumn.add(Integer.parseInt(numbers[0]));
            rightColumn.add(Integer.parseInt(numbers[1]));
        }
    }

    private int calculateSimilarity(List<Integer> leftColumn, List<Integer> rightColumn) {
        int result = 0;
        for (int i = 0; i < leftColumn.size(); i++) {
            result += Math.abs(leftColumn.get(i) - rightColumn.get(i));
        }
        return result;
    }

    private int calculateWeightedSimilarity(List<Integer> leftColumn, List<Integer> rightColumn) {
        int result = 0;
        for (int leftNumber : leftColumn) {
            int count = (int) rightColumn.stream().filter(rightNumber -> rightNumber == leftNumber).count();
            result += (leftNumber * count);
        }
        return result;
    }

    public static void main(String[] args) {
        try {
            List<String> input = InputReader.readLines(YEAR_2024, DAY_01);

            Solution solution = new Day01();
            String solutionPart1 = solution.part1(input);
            String solutionPart2 = solution.part2(input);

            Utils.logResults(DAY_01, solutionPart1, solutionPart2);
        } catch (IOException e) {
            LOGGER.error("Error while reading data: {}", e.getMessage(), e);
        }
    }
}
