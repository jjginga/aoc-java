package com.jjginga.aoc.year2015;


import com.jjginga.aoc.Solution;
import com.jjginga.aoc.utils.InputReader;
import com.jjginga.aoc.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.IntStream;

import static com.jjginga.aoc.utils.Constants.DAY_03;
import static com.jjginga.aoc.utils.Constants.YEAR_2015;

public class Day03 implements Solution {
    private static final Logger LOGGER = LoggerFactory.getLogger(Day03.class);

    private record Pos(int x, int y) {
        Pos move(Pos delta){
            return new Pos(x + delta.x, y + delta.y);
        }
    }

    private static final Map<Character, Pos> DELTAS = Map.of(
            '>', new Pos(1, 0),
            '<', new Pos(-1, 0),
            '^', new Pos(0, 1),
            'v', new Pos(0, -1)
    );



    public static void main(String[] args) {
        try {
            List<String> input = InputReader.readLines(YEAR_2015, DAY_03);

            Day03 day03 = new Day03();
            String solutionPart1 = day03.part1(input);
            String solutionPart2 = day03.part2(input);

            Utils.logResults(DAY_03, solutionPart1, solutionPart2);
        } catch (IOException e) {
            LOGGER.error("Error reading input file for {}: {}", DAY_03, e.getMessage());
        } catch (Exception e) {
            LOGGER.error("An unexpected error occurred while processing {}: {}", DAY_03, e.getMessage(), e);
        }
    }

    @Override
    public String part1(List<String> input) {
        //Santa moves throught all directions
        //count unique houses visited
        Set<Pos> visitedHouses = new HashSet<>();
        AtomicReference<Pos> pos = new AtomicReference<>(new Pos(0, 0));

        String.join("", input)
                .chars()
                .forEach(c ->{
                    Pos delta = DELTAS.get((char) c);
                    pos.set(pos.get().move(delta));
                    visitedHouses.add(pos.get());
                });
        return String.valueOf(visitedHouses.size());
    }

    @Override
    public String part2(List<String> input) {
        //Santa and Robot take turns moving
        //count unique houses visited
        Set<Pos> visitedHouses = new HashSet<>();
        AtomicReference<Pos> posSanta = new AtomicReference<>(new Pos(0, 0));
        AtomicReference<Pos> posRobot = new AtomicReference<>(new Pos(0, 0));
        String directions = String.join("", input);

        IntStream.range(0, directions.length())
                .forEach(i ->{
                    char c = directions.charAt(i);
                    Pos delta = DELTAS.get(c);

                    AtomicReference<Pos> active = (i % 2 == 0) ? posSanta : posRobot;
                    active.set(active.get().move(delta));
                    visitedHouses.add(active.get());
                });
        return String.valueOf(visitedHouses.size());
    }
}
