package com.jjginga.aoc.year2015;

import com.jjginga.aoc.Solution;
import com.jjginga.aoc.utils.InputReader;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static com.jjginga.aoc.utils.Constants.DAY_02;
import static com.jjginga.aoc.utils.Constants.YEAR_2015;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day02Test {

    private static Solution solution;

    @BeforeAll
    static void beforeAll() {
        solution = new Day02();
    }

    @Test
    void testPart1() throws IOException {
        List<String> input = InputReader.readLines(YEAR_2015, DAY_02);

        long start = System.nanoTime();
        String result = solution.part1(input);
        long duration = System.nanoTime() - start;
        System.out.printf("Part 1, result: %s | took %.2f ms%n", result, duration / 1_000_000.0);

        assertEquals("1588178", result);
    }

    @Test
    void testPart2() throws IOException {
        List<String> input = InputReader.readLines(YEAR_2015, DAY_02);

        long start = System.nanoTime();
        String result = solution.part2(input);
        long duration = System.nanoTime() - start;
        System.out.printf("Part 2, result: %s | took %.2f ms%n", result, duration / 1_000_000.0);

        assertEquals("3783758", result);
    }


}
