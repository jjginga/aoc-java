package com.jjginga.aoc.year2015;


import com.jjginga.aoc.Solution;
import com.jjginga.aoc.utils.InputReader;
import com.jjginga.aoc.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

import static com.jjginga.aoc.utils.Constants.DAY_15;
import static com.jjginga.aoc.utils.Constants.YEAR_2015;

public class Day15 implements Solution {
    private static final Logger LOGGER = LoggerFactory.getLogger(Day15.class);

    public static void main(String[] args) {
        try {
            List<String> input = InputReader.readLines(YEAR_2015, DAY_15);

            Day15 day15 = new Day15();
            String solutionPart1 = day15.part1(input);
            String solutionPart2 = day15.part2(input);

            Utils.logResults(DAY_15, solutionPart1, solutionPart2);
        } catch (IOException e) {
            LOGGER.error("Error reading input file for {}: {}", DAY_15, e.getMessage());
        } catch (Exception e) {
            LOGGER.error("An unexpected error occurred while processing {}: {}", DAY_15, e.getMessage(), e);
        }
    }

    private final static int TOTAL_TEASPOONS = 100;
    private final static int CALORIES = 500;
    private static final Pattern INTEGER= Pattern.compile("-?\\d+");

    @Override
    public String part1(List<String> input) {
        return solve(input, false);
    }

    @Override
    public String part2(List<String> input) {
        return solve(input, true);
    }

    private String solve(List<String> input, boolean limitCalories) {
        var ingredients = parse(input);

        // Properties indices 0 to 3 and calories at index 4
        int maxScore = 0;
        // Sums per property and calories accumulator
        int[] sums = new int[5];

        maxScore = dfs(0, TOTAL_TEASPOONS, limitCalories, ingredients, sums, maxScore);
        return String.valueOf(maxScore);
    }

    private int dfs(int idx, int remaining, boolean limitCalories, int[][] ingredients, int[] sums, int maxScore) {
        int numIngredienst = ingredients.length;

        //last ingredient
        //compute property totals and clamp
        if(idx == numIngredienst - 1) {
            sums[idx] = remaining;
            int score = 1;
            for (int p = 0 ; p < 4; p++){
                int sumP = 0;
                for(int i = 0 ; i < numIngredienst ; i++) {
                    sumP += sums[i] * ingredients[i][p];//negative values are zeroed before multiplying
                }
                sumP = Math.max(sumP, 0);
                score *= sumP;
            }

            int calories = remaining * ingredients[idx][4] + IntStream.range(0,idx)
                    .map(i -> sums[i] * ingredients[i][4]).sum();

            if(!limitCalories || calories == CALORIES){
                return Math.max(score, maxScore);
            }

            return maxScore;

        }

        for (int q = 0; q <= remaining; q++) {
            sums[idx] = q;
            maxScore = dfs(idx + 1, remaining - q, limitCalories, ingredients, sums, maxScore);
        }

        return maxScore;
    }

   private int[][] parse(List<String> input) {
        return input.stream()
                .map(line -> INTEGER.matcher(line)
                        .results()
                        .mapToInt(m -> Integer.parseInt(m.group()))
                        .toArray()
                )
                .toArray(int[][]::new);
   }
}
