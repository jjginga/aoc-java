package com.jjginga.aoc.year2015;


import com.jjginga.aoc.Solution;
import com.jjginga.aoc.utils.InputReader;
import com.jjginga.aoc.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static com.jjginga.aoc.utils.Constants.*;

public class Day02 implements Solution {
    private static final Logger LOGGER = LoggerFactory.getLogger(Day02.class);

    private record Dim(int l, int w, int h) {
        int minArea() {
            return Math.min(l*w, Math.min(w*h, h*l));
        }

        int minPerimeter() {
            return Stream.of(l, w, h)
                    .sorted()
                    .limit(2)
                    .mapToInt(Integer::intValue)
                    .sum() * 2;
        }

        int volume() {
            return l * w * h;
        }
    }

    public static void main(String[] args) {
        try {
            List<String> input = InputReader.readLines(YEAR_2015, DAY_02);

            Day02 day01 = new Day02();
            String solutionPart1 = day01.part1(input);
            String solutionPart2 = day01.part2(input);

            Utils.logResults(DAY_02, solutionPart1, solutionPart2);
        } catch (IOException e) {
            LOGGER.error("Error reading input file for {}: {}", DAY_02, e.getMessage());
        } catch (Exception e) {
            LOGGER.error("An unexpected error occurred while processing {}: {}", DAY_02, e.getMessage(), e);
        }
    }

    @Override
    public String part1(List<String> input) {
        return String.valueOf(input.stream()
                .map( line -> {
                    int[] dims =  Arrays.stream(line.split("x"))
                            .mapToInt(Integer::parseInt)
                            .toArray();
                    return new Dim(dims[0], dims[1], dims[2]);
                })
                .mapToInt(dim -> 2*dim.l*dim.w + 2*dim.w*dim.h + 2*dim.h*dim.l + dim.minArea())
                .sum());
    }

    @Override
    public String part2(List<String> input) {
        return String.valueOf(input.stream()
                .map( line -> {
                    int[] dims =  Arrays.stream(line.split("x"))
                            .mapToInt(Integer::parseInt)
                            .toArray();
                    return new Dim(dims[0], dims[1], dims[2]);
                })
                .mapToInt(dim -> dim.minPerimeter() + dim.volume())
                .sum());
    }
}
