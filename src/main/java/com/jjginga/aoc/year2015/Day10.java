package com.jjginga.aoc.year2015;


import com.jjginga.aoc.Solution;
import com.jjginga.aoc.utils.InputReader;
import com.jjginga.aoc.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;

import static com.jjginga.aoc.utils.Constants.*;

public class Day10 implements Solution {
    private static final Logger LOGGER = LoggerFactory.getLogger(Day10.class);

    public static void main(String[] args) {
        try {
            List<String> input = InputReader.readLines(YEAR_2015, DAY_10);

            Day08 day01 = new Day08();
            String solutionPart1 = day01.part1(input);
            String solutionPart2 = day01.part2(input);

            Utils.logResults(DAY_10, solutionPart1, solutionPart2);
        } catch (IOException e) {
            LOGGER.error("Error reading input file for {}: {}", DAY_10, e.getMessage());
        } catch (Exception e) {
            LOGGER.error("An unexpected error occurred while processing {}: {}", DAY_10, e.getMessage(), e);
        }
    }

    @Override
    public String part1(List<String> input) {
        var sequence = input.getFirst();
        List<Integer> numbers = new ArrayList<>();

        lookAndSay(sequence, numbers);

        for(int i = 1 ; i < 40 ; i++) {
            lookAndSay(numbers);
        }

        return String.valueOf(numbers.size());
    }

    @Override
    public String part2(List<String> input) {
        var sequence = input.getFirst();
        List<Integer> numbers = new ArrayList<>();

        lookAndSay(sequence, numbers);

        for(int i = 1 ; i < 50 ; i++) {
            lookAndSay(numbers);
        }

        return String.valueOf(numbers.size());
    }

    private void lookAndSay(List<Integer> numbers) {
        int quantity;
        List<Integer> prev = new ArrayList<>(numbers);
        numbers.clear();

        int algarism = prev.getFirst();
        prev.removeFirst();
        quantity = 1;
        for(int curr : prev) {
            if(curr != algarism) {
                numbers.add(quantity);
                numbers.add(algarism);
                algarism = curr;
                quantity = 1;
                continue;
            }

            quantity++;
        }
        numbers.add(quantity);
        numbers.add(algarism);
    }

    private void lookAndSay(String sequence, List<Integer> numbers) {
        // Seed the list.
        char digit = sequence.charAt(0);
        int quantity = 1;

        for (int i = 1; i < sequence.length(); i++) {
            char curr = sequence.charAt(i);
            // If we find a new digit then we populate the numbers list
            if (curr != digit){
                numbers.add(quantity);
                numbers.add(digit-'0');
                digit = curr;
                quantity = 1;
                continue;
            }

            // If the digit is the same we just increase the quantity
            quantity++;
        }

        // Close last pair
        numbers.add(quantity);
        numbers.add(digit-'0');
    }



}
