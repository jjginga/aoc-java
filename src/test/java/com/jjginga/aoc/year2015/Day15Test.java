package com.jjginga.aoc.year2015;

import com.jjginga.aoc.Solution;
import com.jjginga.aoc.utils.InputReader;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static com.jjginga.aoc.utils.Constants.DAY_15;
import static com.jjginga.aoc.utils.Constants.YEAR_2015;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day15Test {

    private static Solution solution;

    @BeforeAll
    static void beforeAll() {
        solution = new Day15();
    }

    @Test
    void testPart1() throws IOException {
        List<String> input = InputReader.readLines(YEAR_2015, DAY_15);

        long start = System.nanoTime();
        String result = solution.part1(input);
        long duration = System.nanoTime() - start;
        System.out.printf("%s %s -> Part 1, result: %s | took %.2f ms%n", YEAR_2015, DAY_15, result, duration / 1_000_000.0);

        assertEquals("21367368", result);
    }

    @Test
    void testPart2() throws IOException {
        List<String> input = InputReader.readLines(YEAR_2015, DAY_15);

        long start = System.nanoTime();
        String result = solution.part2(input);
        long duration = System.nanoTime() - start;
        System.out.printf("%s %s -> Part 2, result: %s | took %.2f ms%n", YEAR_2015, DAY_15, result, duration / 1_000_000.0);

        assertEquals("1766400", result);
    }


}
