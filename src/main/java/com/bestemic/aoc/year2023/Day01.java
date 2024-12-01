package com.bestemic.aoc.year2023;

import com.bestemic.aoc.Solution;
import com.bestemic.aoc.utils.InputReader;
import com.bestemic.aoc.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static com.bestemic.aoc.utils.Constants.DAY_01;
import static com.bestemic.aoc.utils.Constants.YEAR_2023;

public class Day01 implements Solution {
    private static final Logger LOGGER = LoggerFactory.getLogger(Day01.class);
    private static final Map<String, Character> DIGITS_AS_STRING = Map.of(
            "one", '1',
            "two", '2',
            "three", '3',
            "four", '4',
            "five", '5',
            "six", '6',
            "seven", '7',
            "eight", '8',
            "nine", '9'
    );

    /**
     * Expected output: 54916
     */
    @Override
    public String part1(List<String> input) {
        int sum = 0;
        for (String line : input) {
            StringBuilder currentNumber = extractDigits(line, false);
            sum += getSumOfFirstAndLastDigit(currentNumber);
        }
        return String.valueOf(sum);
    }

    /**
     * Expected output: 54728
     */
    @Override
    public String part2(List<String> input) {
        int sum = 0;
        for (String line : input) {
            StringBuilder currentNumber = extractDigits(line, true);
            sum += getSumOfFirstAndLastDigit(currentNumber);
        }
        return String.valueOf(sum);
    }

    private StringBuilder extractDigits(String line, boolean convertText) {
        StringBuilder currentNumber = new StringBuilder();
        int length = line.length();

        for (int i = 0; i < length; i++) {
            char currentChar = line.charAt(i);

            if (Character.isDigit(currentChar)) {
                currentNumber.append(currentChar);
            } else if (convertText) {
                processTextToDigit(line, currentNumber, i);
            }
        }

        return currentNumber;
    }

    private void processTextToDigit(String line, StringBuilder currentNumber, int index) {
        for (Map.Entry<String, Character> entry : DIGITS_AS_STRING.entrySet()) {
            if (line.startsWith(entry.getKey(), index)) {
                currentNumber.append(entry.getValue());
            }
        }
    }

    private int getSumOfFirstAndLastDigit(StringBuilder currentNumber) {
        char firstDigit = currentNumber.charAt(0);
        char lastDigit = currentNumber.length() == 1 ? firstDigit : currentNumber.charAt(currentNumber.length() - 1);
        return Integer.parseInt("" + firstDigit + lastDigit);
    }

    public static void main(String[] args) {
        try {
            List<String> input = InputReader.readLines(YEAR_2023, DAY_01);

            Solution solution = new Day01();
            String solutionPart1 = solution.part1(input);
            String solutionPart2 = solution.part2(input);

            Utils.logResults(DAY_01, solutionPart1, solutionPart2);
        } catch (IOException e) {
            LOGGER.error("Error while reading data: {}", e.getMessage(), e);
        }
    }
}