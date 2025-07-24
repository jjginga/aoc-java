package com.jjginga.aoc.year2015;

import com.jjginga.aoc.Solution;
import com.jjginga.aoc.utils.InputReader;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static com.jjginga.aoc.utils.Constants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day10Test {

    private static Solution solution;

    @BeforeAll
    static void beforeAll() {
        solution = new Day10();
    }

    @Test
    void testPart1() throws IOException {
        List<String> input = InputReader.readLines(YEAR_2015, DAY_10);

        long start = System.nanoTime();
        String result = solution.part1(input);
        long duration = System.nanoTime() - start;
        System.out.printf("%s %s -> Part 1, result: %s | took %.2f ms%n", YEAR_2015, DAY_10, result, duration / 1_000_000.0);

        assertEquals("492982", result);
    }

    @Test
    void testPart2() throws IOException {
        List<String> input = InputReader.readLines(YEAR_2015, DAY_10);

        long start = System.nanoTime();
        String result = solution.part2(input);
        long duration = System.nanoTime() - start;
        System.out.printf("%s %s -> Part 2, result: %s | took %.2f ms%n", YEAR_2015, DAY_10, result, duration / 1_000_000.0);

        assertEquals("6989950", result);
    }


}
