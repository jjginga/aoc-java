package com.bestemic.aoc.year2024;

import com.bestemic.aoc.Solution;
import com.bestemic.aoc.utils.Grid;
import com.bestemic.aoc.utils.InputReader;
import com.bestemic.aoc.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;

import static com.bestemic.aoc.utils.Constants.*;

public class Day06 implements Solution {
    private static final Logger LOGGER = LoggerFactory.getLogger(Day06.class);

    /**
     * Expected output: 4656
     */
    @Override
    public String part1(List<String> input) {
        Grid grid = new Grid(input);
        grid.findStart('^');
        grid.findDirection();

        while (grid.getNextCharacter() != '!') {
            char nextCharacter = grid.getNextCharacter();
            if (nextCharacter == '#') {
                grid.turnRight();
            } else {
                grid.moveForward('X');
            }
        }
        grid.moveForward('X');

        return String.valueOf(grid.countOccurrences('X'));
    }

    /**
     * Expected output:
     */
    @Override
    public String part2(List<String> input) {
        return null;
    }

    public static void main(String[] args) {
        try {
            List<String> input = InputReader.readLines(YEAR_2024, DAY_06);

            Solution solution = new Day06();
            String solutionPart1 = solution.part1(input);
            String solutionPart2 = solution.part2(input);

            Utils.logResults(DAY_06, solutionPart1, solutionPart2);
        } catch (IOException e) {
            LOGGER.error("Error while reading data: {}", e.getMessage(), e);
        }
    }
}
