package com.jjginga.aoc.year2015;


import com.jjginga.aoc.Solution;
import com.jjginga.aoc.utils.InputReader;
import com.jjginga.aoc.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.jjginga.aoc.utils.Constants.*;

public class Day11 implements Solution {
    private static final Logger LOGGER = LoggerFactory.getLogger(Day11.class);

    public static void main(String[] args) {
        try {
            List<String> input = InputReader.readLines(YEAR_2015, DAY_11);

            Day11 day11 = new Day11();
            String solutionPart1 = day11.part1(input);
            String solutionPart2 = day11.part2(input);

            Utils.logResults(DAY_11, solutionPart1, solutionPart2);
        } catch (IOException e) {
            LOGGER.error("Error reading input file for {}: {}", DAY_11, e.getMessage());
        } catch (Exception e) {
            LOGGER.error("An unexpected error occurred while processing {}: {}", DAY_11, e.getMessage(), e);
        }
    }

    @Override
    public String part1(List<String> input) {
        var pass = input.getFirst().chars().mapToObj(c -> (char)c).collect(Collectors.toCollection(ArrayList::new));
        
        while(!isValidPassword(pass)){
            increment(pass);
        }
        return pass.stream().map(String::valueOf).collect(Collectors.joining());
    }

    private String increment(String s) {
        List<Character> chars = s.chars().mapToObj(c -> (char)c).collect(Collectors.toList());
        increment(chars);
        return chars.stream().map(String::valueOf).collect(Collectors.joining());
    }

    private void increment(List<Character> pass) {
        int pos = pass.size()-1;
        increment(pass, pos);
    }

    private void increment(List<Character> pass, int pos) {
        if(pass.get(pos) == 'z'){
            pass.set(pos, 'a');
            increment(pass, pos - 1);
            return;
        }
        pass.set(pos, (char) (pass.get(pos) + 1));
    }

    private boolean isValidPassword(ArrayList<Character> pass) {
        return !hasForbidenLetters(pass) && isIncreasing(pass) && hasPairs(pass);
    }

    private boolean hasPairs(ArrayList<Character> pass) {
        Set<Character> foundPairs = new HashSet<>();
        for(int i = 0; i < pass.size()-1; ){
            if (pass.get(i) == pass.get(i+1)) {
                foundPairs.add(pass.get(i));
                if(foundPairs.size() >= 2) {
                    return true;
                }
                i += 2;
                continue;
            }

            i++;
        }
        return false;
    }

    private boolean isIncreasing(ArrayList<Character> pass) {
        return IntStream.range(0,pass.size()-2).anyMatch(i ->{
            char a = pass.get(i);
            char b = pass.get(i+1);
            char c = pass.get(i+2);
            return b == a + 1 && c == b + 1;
        });
    }

    private boolean hasForbidenLetters(ArrayList<Character> pass) {
        return pass.stream().anyMatch(c -> c=='i' || c=='o' || c=='l' );
    }


    @Override
    public String part2(List<String> input) {
        String oldPass = increment(part1(input));
        return part1(new ArrayList<>(List.of(oldPass)));
    }

}
