package com.jjginga.aoc.year2015;


import com.jjginga.aoc.Solution;
import com.jjginga.aoc.utils.InputReader;
import com.jjginga.aoc.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;

import static com.jjginga.aoc.utils.Constants.*;

public class Day16 implements Solution {
    private static final Logger LOGGER = LoggerFactory.getLogger(Day16.class);

    public static void main(String[] args) {
        try {
            List<String> input = InputReader.readLines(YEAR_2015, DAY_16);

            Day16 day16 = new Day16();
            String solutionPart1 = day16.part1(input);
            String solutionPart2 = day16.part2(input);

            Utils.logResults(DAY_16, solutionPart1, solutionPart2);
        } catch (IOException e) {
            LOGGER.error("Error reading input file for {}: {}", DAY_16, e.getMessage());
        } catch (Exception e) {
            LOGGER.error("An unexpected error occurred while processing {}: {}", DAY_16, e.getMessage(), e);
        }
    }

    private enum Compound {
        CHILDREN("children"),
        CATS("cats"),
        SAMOYEDS("samoyeds"),
        POMERANIANS("pomeranians"),
        AKITAS("akitas"),
        VIZSLAS("vizslas"),
        GOLDFISH("goldfish"),
        TREES("trees"),
        CARS("cars"),
        PERFUMES("perfumes");

        private final String name;

        //lookup map
        private static final Map<String, Compound> map = new HashMap<>();

        static {
            for(Compound compound : Compound.values()) {
                map.put(compound.name, compound);
            }
        }

        Compound(String name) {
            this.name = name;
        }

        //property from name
        public static Compound get(String name) {
            return map.getOrDefault (name, null);
        }

    }


    private record Auntie(
            int number,
            Map<Compound, Integer> compounds
    ){
        public Set<Compound> getCompounds() {
            return compounds.keySet();
        }

        public OptionalInt getNumber(Compound compound) {
            return compounds.containsKey(compound) ? OptionalInt.of(compounds.get(compound)) : OptionalInt.empty();
        }
    }


    // Reference Auntie to compare others
    private static final Auntie REFERENCE = new Auntie( 0,
            Map.ofEntries(
                    Map.entry(Compound.CHILDREN,      3),
                    Map.entry(Compound.CATS,          7),
                    Map.entry(Compound.SAMOYEDS,      2),
                    Map.entry(Compound.POMERANIANS,   3),
                    Map.entry(Compound.AKITAS,        0),
                    Map.entry(Compound.VIZSLAS,       0),
                    Map.entry(Compound.GOLDFISH,      5),
                    Map.entry(Compound.TREES,         3),
                    Map.entry(Compound.CARS,          2),
                    Map.entry(Compound.PERFUMES,      1)
            ));

    @Override
    public String part1(List<String> input) {

        Comparator<Auntie> comparator = Comparator.comparing(candidate -> {
            var compoundsToCompare = candidate.getCompounds();

            int dist = 0;

            for(var compound : compoundsToCompare) {
                int value1 = candidate.getNumber(compound).orElseThrow();
                int value2 = REFERENCE.getNumber(compound).orElseThrow();
                dist += Math.abs(value1 - value2);
            }
            return dist;
        });

        return solve(input, comparator);
    }

    @Override
    public String part2(List<String> input) {

        Comparator<Auntie> comparator = Comparator.comparingInt(candidate -> {
            int failures = 0;

            var compoundsToCompare = candidate.getCompounds();

            for(var compound : compoundsToCompare) {
                int value1 = candidate.getNumber(compound).orElseThrow();
                int value2 = REFERENCE.getNumber(compound).orElseThrow();
                switch (compound) {
                    case CATS, TREES -> {
                        if (value1 <= value2) failures++;
                    }
                    case GOLDFISH, POMERANIANS -> {
                        if (value1 >= value2) failures++;
                    }
                    default -> {
                        if (value1 != value2) failures++;
                    }
                }
            }

            return failures;
        });
        return solve(input, comparator);
    }

    private static String solve(List<String> input, Comparator<Auntie> comparator) {
        PriorityQueue<Auntie> aunties = new PriorityQueue<>(comparator);

        //parse aunties and add them to priority queue
        for(var line: input) {
            line = line.replaceAll(":", "");
            line = line.replaceAll(",", "");
            var lineArr = line.split(" ");
            var auntieNumber = Integer.parseInt(lineArr[1]);
            Map<Compound, Integer> compounds = new HashMap<>();
            for(int i = 2; i < lineArr.length; ) {
                compounds.put(Compound.get(lineArr[i]) , Integer.parseInt(lineArr[i+1]));
                i+=2;
            }
            var auntie = new Auntie(auntieNumber, compounds);
            aunties.add(auntie);
        }


        assert aunties.peek() != null;
        return String.valueOf(aunties.peek().number());
    }



}
