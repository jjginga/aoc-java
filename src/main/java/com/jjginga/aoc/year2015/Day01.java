package com.jjginga.aoc.year2015;



import com.jjginga.aoc.Solution;
import com.jjginga.aoc.utils.InputReader;

import com.jjginga.aoc.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.jjginga.aoc.utils.Constants.DAY_01;
import static com.jjginga.aoc.utils.Constants.YEAR_2015;

public class Day01 implements Solution {
    private static final Logger LOGGER = LoggerFactory.getLogger(Day01.class);


    public static void main(String[] args) {
        try {
            List<String> input = InputReader.readLines(YEAR_2015, DAY_01);

            Day01 day01 = new Day01();
            String solutionPart1 = day01.part1(input);
            String solutionPart2 = day01.part2(input);

            Utils.logResults(DAY_01, solutionPart1, solutionPart2);
        } catch (IOException e) {
            LOGGER.error("Error reading input file for {}: {}", DAY_01, e.getMessage());
        } catch (Exception e) {
            LOGGER.error("An unexpected error occurred while processing {}: {}", DAY_01, e.getMessage(), e);
        }
    }

    @Override
    public String part1(List<String> input) {
        return String.valueOf(String.join("", input)
                .chars()
                // Convert each character to 1 for '(' and -1 for ')'
                .map(c -> c=='(' ? 1 : -1)
                .sum());
    }

    @Override
    public String part2(List<String> input) {
        String line = String.join("", input);
        AtomicInteger sum = new AtomicInteger(0);

        return String.valueOf(
                IntStream.range(0, line.length())
                        // Use takeWhile to find the first position where the sum goes negative
                        .takeWhile(i -> {
                            int delta = line.charAt(i) == '(' ? 1 : -1;
                            return sum.addAndGet(delta) >= 0;
                        })
                        .count()
                        // Add 1 to the count to get the position in the string
                        + 1);
    }
}
