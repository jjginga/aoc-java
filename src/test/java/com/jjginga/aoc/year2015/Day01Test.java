package com.jjginga.aoc.year2015;

import com.jjginga.aoc.Solution;
import com.jjginga.aoc.utils.InputReader;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static com.jjginga.aoc.utils.Constants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day01Test {

    private static Solution solution;

    @BeforeAll
    static void beforeAll() {
        solution = new Day01();
    }

    @Test
    void testPart1() throws IOException {
        List<String> input = InputReader.readLines(YEAR_2015, DAY_01);

        long start = System.nanoTime();
        String result = solution.part1(input);
        long duration = System.nanoTime() - start;
        System.out.printf("Part 1, result: %s | took %.2f ms%n", result, duration / 1_000_000.0);

        assertEquals("232", result);
    }

    @Test
    void testPart2() throws IOException {
        List<String> input = InputReader.readLines(YEAR_2015, DAY_01);

        long start = System.nanoTime();
        String result = solution.part2(input);
        long duration = System.nanoTime() - start;
        System.out.printf("Part 2, result: %s | took %.2f ms%n", result, duration / 1_000_000.0);

        assertEquals("1783", result);
    }


}
