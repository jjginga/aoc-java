package com.jjginga.aoc.year2015;


import com.jjginga.aoc.Solution;
import com.jjginga.aoc.utils.InputReader;
import com.jjginga.aoc.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
    private final static int NUM_PROPERTIES = 4;
    private final static int CALORIES = 500;
    private final static int CALORIE_POSITION = 4;

   @Override
   public String part1(List<String> input) {


       int maxScore = getMaxScore(input, false);

       return String.valueOf(maxScore);
   }

    private int getMaxScore(List<String> input, boolean limitCalories) {
        var ingredients = parseIngredients(input);
        //var numProperties = Arrays.stream(ingredients).mapToInt(arr -> arr.length).max().orElseThrow();
        var numIngredients = ingredients.length;

        // Get all possible combinations that total number of teaspoons
        List<List<Integer>> allCombinations = new ArrayList<>();
        int[] currentCombination = new int[numIngredients]; //array to construct each combination

        findAllCombinations(0, TOTAL_TEASPOONS, currentCombination, allCombinations);

        int maxScore = Integer.MIN_VALUE;
        for(var combination : allCombinations) {
            int score = 1;
            int calories = 0;
            //for each property - we want for first four only
            for (int p = 0; p < NUM_PROPERTIES; p++) {
                int sum = 0;
                //for each ingredient, for property P
                for(int i = 0; i < numIngredients; i++) {
                    sum += combination.get(i)*ingredients[i][p];

                }
                sum = Math.max(0, sum);

                score *= sum;
            }
            if (limitCalories) {
                for(int i = 0 ; i < numIngredients; i++) {
                    calories += combination.get(i)*ingredients[i][CALORIE_POSITION];
                }
                if(calories == CALORIES) {
                    maxScore = Math.max(maxScore, score);
                }
                continue;
            }
            maxScore = Math.max(maxScore, score);
        }
        return maxScore;
    }

    @Override
   public String part2(List<String> input) {

        return String.valueOf(getMaxScore(input, true));
   }

   private static final Pattern INTEGER= Pattern.compile("-?\\d+");

   private int[][] parseIngredients(List<String> input) {
        return input.stream()
                .map(line -> INTEGER.matcher(line)
                        .results()
                        .mapToInt(m -> Integer.parseInt(m.group()))
                        .toArray()
                )
                .toArray(int[][]::new);
   }

   private void findAllCombinations(int ingredientIndex, int remainingTeaspoons, int[] currentCombination, List<List<Integer>> allCombinations) {
       // All variables have been filled
       if(ingredientIndex == currentCombination.length) {
           // If there are no teaspoons left, the combination is vallid
           if(remainingTeaspoons == 0) {
               //A copy of the current combination is added to the combination list
               var validCombinations = Arrays.stream(currentCombination).boxed().toList();
               allCombinations.add(validCombinations);
           }
           return; // Ramifications of recursion
       }

       // There are no more teaspoons
       // All remaining ingredients must be filled with zero
       if(remainingTeaspoons == 0) {
           IntStream.range(ingredientIndex, currentCombination.length)
                   .forEach(i -> currentCombination[i] = 0);
           // Call recursivelly to add this combination
           findAllCombinations(currentCombination.length, remainingTeaspoons, currentCombination, allCombinations);
           return;
       }

       // Distribute teaspoons for current variable
       // Iterate over all quantities possible for current variabla
       // It can go up to 0
       for (int quantity = 0 ; quantity <= remainingTeaspoons ; quantity++) {
           currentCombination[ingredientIndex] = quantity;

           findAllCombinations(ingredientIndex + 1, remainingTeaspoons - quantity, currentCombination, allCombinations);
       }
   }
}
