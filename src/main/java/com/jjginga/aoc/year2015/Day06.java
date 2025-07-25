package com.jjginga.aoc.year2015;


import com.jjginga.aoc.Solution;
import com.jjginga.aoc.utils.InputReader;
import com.jjginga.aoc.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

import static com.jjginga.aoc.utils.Constants.*;

public class Day06 implements Solution {
    private static final Logger LOGGER = LoggerFactory.getLogger(Day06.class);

    public static void main(String[] args) {
        try {
            List<String> input = InputReader.readLines(YEAR_2015, DAY_06);

            Day06 day06 = new Day06();
            String solutionPart1 = day06.part1(input);
            String solutionPart2 = day06.part2(input);

            Utils.logResults(DAY_06, solutionPart1, solutionPart2);
        } catch (IOException e) {
            LOGGER.error("Error reading input file for {}: {}", DAY_06, e.getMessage());
        } catch (Exception e) {
            LOGGER.error("An unexpected error occurred while processing {}: {}", DAY_06, e.getMessage(), e);
        }
    }

    //dimension of the grid
    private static final int SIZE = 1000;

    //Command enum
    private enum Cmd { TURN_ON, TURN_OFF, TOGGLE }

    //pattern: "turn on|turn off|toogle xi,yi through xf, yf"
    private static final Pattern LINE_RX =
            Pattern.compile("^(turn on|turn off|toggle) (\\d+),(\\d+) through (\\d+),(\\d+)$");

    @Override
    public String part1(List<String> input) {

        boolean[][] on = new boolean[SIZE][SIZE];

        input
                .forEach(line -> {
                    Matcher matcher = LINE_RX.matcher(line);

                    if(!matcher.matches()) {
                        throw new IllegalStateException("Line does not match pattern: " + LINE_RX.pattern());
                    }

                    //get command
                    Cmd cmd = switch (matcher.group(1)) {
                        case "turn on" -> Cmd.TURN_ON;
                        case "turn off" -> Cmd.TURN_OFF;
                        default -> Cmd.TOGGLE;
                    };

                    //initial coordinates
                    int xi = Integer.parseInt(matcher.group(2));
                    int yi = Integer.parseInt(matcher.group(3));
                    //final coordinates
                    int xf = Integer.parseInt(matcher.group(4));
                    int yf = Integer.parseInt(matcher.group(5));

                    //apply command to every cell in [xi..xf]×[yi..yf]
                    for(int x = xi; x <= xf; x++) {
                        for(int y = yi; y <= yf; y++) {
                            switch(cmd) {
                                case TURN_ON -> on[x][y] = true;
                                case TURN_OFF -> on[x][y] = false;
                                case TOGGLE -> on[x][y] = !on[x][y];
                            }
                        }
                    }

                });
        //count cells where on[x][y]
        return String.valueOf(Arrays.stream(on)
                .flatMapToInt(row -> IntStream.range(0, row.length)
                                                        .filter(j->row[j]))
                .count());
    }


    @Override
    public String part2(List<String> input) {

        int[][] intensity = new int[SIZE][SIZE];

        input
                .forEach(line -> {
                    Matcher matcher = LINE_RX.matcher(line);

                    if(!matcher.matches()) {
                        throw new IllegalStateException("Line does not match pattern: " + LINE_RX.pattern());
                    }

                    //get command
                    Cmd cmd = switch (matcher.group(1)) {
                        case "turn on" -> Cmd.TURN_ON;
                        case "turn off" -> Cmd.TURN_OFF;
                        default -> Cmd.TOGGLE;
                    };

                    //initial coordinates
                    int xi = Integer.parseInt(matcher.group(2));
                    int yi = Integer.parseInt(matcher.group(3));
                    //final coordinates
                    int xf = Integer.parseInt(matcher.group(4));
                    int yf = Integer.parseInt(matcher.group(5));

                    //apply command to every cell in [xi..xf]×[yi..yf]
                    for(int x = xi; x <= xf; x++) {
                        for(int y = yi; y <= yf; y++) {
                            switch(cmd) {
                                case TURN_ON -> intensity[x][y] += 1;
                                case TURN_OFF -> intensity[x][y] = Math.max(0, intensity[x][y] - 1);
                                case TOGGLE -> intensity[x][y] +=2 ;
                            }
                        }
                    }

                });

        //sum all brightness values in intensity[][]
        return String.valueOf(Arrays.stream(intensity)
                .flatMapToInt(row -> IntStream.of(Arrays.stream(row).sum()))
                .sum());
    }
}
