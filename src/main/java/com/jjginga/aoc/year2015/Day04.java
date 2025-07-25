package com.jjginga.aoc.year2015;


import com.jjginga.aoc.Solution;
import com.jjginga.aoc.utils.InputReader;
import com.jjginga.aoc.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.stream.IntStream;

import static com.jjginga.aoc.utils.Constants.*;

public class Day04 implements Solution {
    private static final Logger LOGGER = LoggerFactory.getLogger(Day04.class);



    public static void main(String[] args) {
        try {
            List<String> input = InputReader.readLines(YEAR_2015, DAY_04);

            Day04 day04 = new Day04();
            String solutionPart1 = day04.part1(input);
            String solutionPart2 = day04.part2(input);

            Utils.logResults(DAY_04, solutionPart1, solutionPart2);
        } catch (IOException e) {
            LOGGER.error("Error reading input file for {}: {}", DAY_04, e.getMessage());
        } catch (Exception e) {
            LOGGER.error("An unexpected error occurred while processing {}: {}", DAY_04, e.getMessage(), e);
        }
    }

    @Override
    public String part1(List<String> input) {
        //brute force search for the lowest number that produces a hash starting with 5 zeros
        return String.valueOf(findFirstValidHash(input.getFirst(), 20));
    }

    private int findFirstValidHash(String secretKey, int expectedLeadingZeros) {
        return IntStream.iterate(1, i-> i + 1)
                .filter(num -> isValidHash(secretKey, num, expectedLeadingZeros))
                .findFirst()
                .orElseThrow();
    }

    private boolean isValidHash(String secretKey, int number, int expectedLeadingZeros) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] hash = md5.digest((secretKey+number).getBytes());

            //check first n bits
            //if 5 zeros are required, we need to check the first 20 bits
            //if 6 zeros are required, we need to check the first 24 bits
            return switch (expectedLeadingZeros){
                case 20 -> hash[0] == 0 && hash[1] == 0 && (hash[2] & 0xF0) == 0;
                case 24 -> hash[0] == 0 && hash[1] == 0 && hash[2] == 0;
                default -> throw new IllegalArgumentException("Unsupported number of leading zeros: " + expectedLeadingZeros);
            };
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public String part2(List<String> input) {
        return String.valueOf(findFirstValidHash(input.getFirst(), 24));
    }
}
