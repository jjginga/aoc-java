package com.jjginga.aoc.year2015;


import com.jjginga.aoc.Solution;
import com.jjginga.aoc.utils.InputReader;
import com.jjginga.aoc.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import static com.jjginga.aoc.utils.Constants.*;

public class Day05 implements Solution {
    private static final Logger LOGGER = LoggerFactory.getLogger(Day05.class);



    public static void main(String[] args) {
        try {
            List<String> input = InputReader.readLines(YEAR_2015, DAY_05);

            Day05 day01 = new Day05();
            String solutionPart1 = day01.part1(input);
            String solutionPart2 = day01.part2(input);

            Utils.logResults(DAY_05, solutionPart1, solutionPart2);
        } catch (IOException e) {
            LOGGER.error("Error reading input file for {}: {}", DAY_05, e.getMessage());
        } catch (Exception e) {
            LOGGER.error("An unexpected error occurred while processing {}: {}", DAY_05, e.getMessage(), e);
        }
    }

    @Override
    public String part1(List<String> input) {
        return String.valueOf(input.stream()
                .filter(this::isNiceString)
                .count());
    }

    private boolean isNiceString(String str) {
        return hasThreeVowels(str) && hasDoubleLetter(str) && hasNoForbiddenSubstrings(str);
    }

    private boolean hasThreeVowels(String str) {
        return str.chars()
                .filter(c -> "aeiou".indexOf(c) >= 0)
                .count() >= 3;
    }

    private boolean hasDoubleLetter(String str) {
        return IntStream.range(0, str.length() - 1)
                .anyMatch(i -> str.charAt(i) == str.charAt(i + 1));
    }

    private boolean hasNoForbiddenSubstrings(String str) {
        return !str.contains("ab") && !str.contains("cd") && !str.contains("pq") && !str.contains("xy");
    }



    @Override
    public String part2(List<String> input) {
        return String.valueOf(input.stream()
                .filter(this::isNotNaughtyString)
                .count());
    }

    private boolean isNotNaughtyString(String str) {
        return hasPairOfLetters(str) && hasRepeatedLetterWithOneBetween(str);
    }

    private boolean hasPairOfLetters(String str) {
        //O(n)
        Map<String, Integer> pairs = new HashMap<>();
        for (int i = 0 ; i < str.length() - 1 ; i++) {
            String pair = str.substring(i, i + 2);
            //seen pair of letters && no overlap
            if(pairs.containsKey(pair) && pairs.get(pair) < i - 1) {
                return true; // Found a pair of letters that appears again later
            }
            pairs.putIfAbsent(pair, i);
        }
        return false; // No pair of letters found that appears again later

    }

    private boolean hasRepeatedLetterWithOneBetween(String str) {
        return IntStream.range(0, str.length() - 2)
                .anyMatch(i -> str.charAt(i) == str.charAt(i + 2));
    }
}
