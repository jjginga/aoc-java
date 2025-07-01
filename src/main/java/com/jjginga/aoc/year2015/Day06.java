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

            Day06 day01 = new Day06();
            String solutionPart1 = day01.part1(input);
            String solutionPart2 = day01.part2(input);

            Utils.logResults(DAY_06, solutionPart1, solutionPart2);
        } catch (IOException e) {
            LOGGER.error("Error reading input file for {}: {}", DAY_06, e.getMessage());
        } catch (Exception e) {
            LOGGER.error("An unexpected error occurred while processing {}: {}", DAY_06, e.getMessage(), e);
        }
    }

    private record Pos(int x, int y){}

    private enum Cmd { TURN_ON, TURN_OFF, TOGGLE }

    private static final Pattern LINE_RX =
            Pattern.compile("^(turn on|turn off|toggle) (\\d+),(\\d+) through (\\d+),(\\d+)$");

    @Override
    public String part1(List<String> input) {

        Set<Pos> onLights = new HashSet<>();

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

                    //create Pos
                    Pos from = new Pos(Integer.parseInt(matcher.group(2)), Integer.parseInt(matcher.group(3)));
                    Pos to = new Pos(Integer.parseInt(matcher.group(4)), Integer.parseInt(matcher.group(5)));

                    //turn lights on or off
                    IntStream.rangeClosed(from.x, to.x).forEach(x ->
                            IntStream.rangeClosed(from.y, to.y)
                                    .mapToObj(y -> new Pos(x, y))
                                    .forEach(p -> {
                                        switch (cmd) {
                                            case TURN_ON -> onLights.add(p);
                                            case TURN_OFF -> onLights.remove(p);
                                            case TOGGLE -> {
                                                if (!onLights.remove(p)) onLights.add(p);
                                            }
                                        }
                                    })
                    );
                });

        return String.valueOf(onLights.size());
    }


    @Override
    public String part2(List<String> input) {

        Map<Pos, Integer> onLights = new HashMap<>();

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

                    //create Pos
                    Pos from = new Pos(Integer.parseInt(matcher.group(2)), Integer.parseInt(matcher.group(3)));
                    Pos to = new Pos(Integer.parseInt(matcher.group(4)), Integer.parseInt(matcher.group(5)));

                    //turn lights on or off
                    IntStream.rangeClosed(from.x, to.x).forEach(x ->
                            IntStream.rangeClosed(from.y, to.y)
                                    .mapToObj(y -> new Pos(x, y))
                                    .forEach(p -> {
                                        switch (cmd) {
                                            case TURN_ON -> onLights.put(p, onLights.getOrDefault(p, 0) + 1);
                                            case TURN_OFF -> {
                                                int val = onLights.getOrDefault(p, 0) - 1;
                                                val = Math.max(val, 0);
                                                onLights.put(p, val);
                                            }
                                            case TOGGLE -> onLights.put(p, onLights.getOrDefault(p, 0) + 2);
                                        }
                                    })
                    );
                });

        return String.valueOf(onLights.values().stream().mapToInt(x -> x).sum());
    }
}
