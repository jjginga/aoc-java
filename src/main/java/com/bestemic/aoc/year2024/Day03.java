package com.bestemic.aoc.year2024;

import com.bestemic.aoc.Solution;
import com.bestemic.aoc.utils.InputReader;
import com.bestemic.aoc.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.bestemic.aoc.utils.Constants.DAY_03;
import static com.bestemic.aoc.utils.Constants.YEAR_2024;

public class Day03 implements Solution {
    private static final Logger LOGGER = LoggerFactory.getLogger(Day03.class);
    private static final String MUL_REGEX = "mul\\(\\d{1,3},\\d{1,3}\\)";
    private static final String CONDITIONAL_MUL_REGEX = "(do\\(\\)|don't\\(\\)|" + MUL_REGEX + ")";

    /**
     * Expected output: 161085926
     */
    @Override
    public String part1(List<String> input) {
        Pattern pattern = Pattern.compile(MUL_REGEX);
        int sum = 0;

        for (String line : input) {
            Matcher matcher = pattern.matcher(line);
            while (matcher.find()) {
                String instruction = matcher.group();
                sum += parseMul(instruction);
            }
        }

        return String.valueOf(sum);
    }

    /**
     * Expected output: 82045421
     */
    @Override
    public String part2(List<String> input) {
        Pattern pattern = Pattern.compile(CONDITIONAL_MUL_REGEX);
        int sum = 0;
        boolean mulEnabled = true;

        for (String line : input) {
            Matcher matcher = pattern.matcher(line);
            while (matcher.find()) {
                String instruction = matcher.group();
                if ("do()".equals(instruction)) {
                    mulEnabled = true;
                } else if ("don't()".equals(instruction)) {
                    mulEnabled = false;
                } else if (mulEnabled && instruction.startsWith("mul")) {
                    sum += parseMul(instruction);
                }
            }
        }

        return String.valueOf(sum);
    }

    private int parseMul(String instruction) {
        String[] numbers = instruction.substring(4, instruction.length() - 1).split(",");
        return Integer.parseInt(numbers[0]) * Integer.parseInt(numbers[1]);
    }

    public static void main(String[] args) {
        try {
            List<String> input = InputReader.readLines(YEAR_2024, DAY_03);

            Solution solution = new Day03();
            String solutionPart1 = solution.part1(input);
            String solutionPart2 = solution.part2(input);

            Utils.logResults(DAY_03, solutionPart1, solutionPart2);
        } catch (IOException e) {
            LOGGER.error("Error while reading data: {}", e.getMessage(), e);
        }
    }
}
