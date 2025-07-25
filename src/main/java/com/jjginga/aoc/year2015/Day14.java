package com.jjginga.aoc.year2015;


import com.jjginga.aoc.Solution;
import com.jjginga.aoc.utils.InputReader;
import com.jjginga.aoc.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;

import static com.jjginga.aoc.utils.Constants.*;

public class Day14 implements Solution {
    private static final Logger LOGGER = LoggerFactory.getLogger(Day14.class);

    public static void main(String[] args) {
        try {
            List<String> input = InputReader.readLines(YEAR_2015, DAY_14);

            Day14 day14 = new Day14();
            String solutionPart1 = day14.part1(input);
            String solutionPart2 = day14.part2(input);

            Utils.logResults(DAY_14, solutionPart1, solutionPart2);
        } catch (IOException e) {
            LOGGER.error("Error reading input file for {}: {}", DAY_14, e.getMessage());
        } catch (Exception e) {
            LOGGER.error("An unexpected error occurred while processing {}: {}", DAY_14, e.getMessage(), e);
        }
    }

    final int TOTAL_TIME = 2503;

    record Reindeer(int speed, int runTime, int restTime){
        int distanceAfter(int t){
            int cycle = runTime + restTime; //duration of running/rest cycle
            int full = t / cycle; //number of total cycles
            int remaining = t % cycle; //remaining time
            //Reindeer runs during the runTime of each fullcycle, plus the remainder of time if this is smaller
            //than the remaining time. Else another run cycle
            return (full*runTime + Math.min(remaining, runTime)) * speed;
        }
    }

    @Override
    public String part1(List<String> input) {

        List<Reindeer> reindeers = parseReindeer(input);

        return String.valueOf(reindeers.stream()
                .mapToInt(reindeer -> reindeer.distanceAfter(TOTAL_TIME))
                .max()
                .orElse(0));

    }

    @Override
    public String part2(List<String> input) {

        List<Reindeer> reindeers = parseReindeer(input);
        int[] distances = new int[reindeers.size()];
        int[] points = new int[reindeers.size()];

        for(int i = 1; i <= TOTAL_TIME; i++){
            int maxDistance = Integer.MIN_VALUE;
            for(int j = 0; j < reindeers.size(); j++){
                var distance = reindeers.get(j).distanceAfter(i);
                distances[j] = distance;
                maxDistance = Math.max(maxDistance, distance);
            }

            for (int j = 0; j < distances.length; j++){
                if(distances[j] == maxDistance){
                    points[j] += 1;
                }
            }
        }

        return String.valueOf((Arrays.stream(points).max().orElseThrow()));
    }

    private List<Reindeer> parseReindeer(List<String> input) {
        return input.stream()
                .map(line -> {
                    var lineArray = line.split(" ");
                    return new Reindeer(
                            Integer.parseInt(lineArray[3]),
                            Integer.parseInt(lineArray[6]),
                            Integer.parseInt(lineArray[13])
                    );
                })
                .toList();
    }



}
