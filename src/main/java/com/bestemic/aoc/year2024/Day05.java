package com.bestemic.aoc.year2024;

import com.bestemic.aoc.Solution;
import com.bestemic.aoc.utils.InputReader;
import com.bestemic.aoc.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;

import static com.bestemic.aoc.utils.Constants.DAY_05;
import static com.bestemic.aoc.utils.Constants.YEAR_2024;

public class Day05 implements Solution {
    private static final Logger LOGGER = LoggerFactory.getLogger(Day05.class);

    /**
     * Expected output: 4662
     */
    @Override
    public String part1(List<String> input) {
        ParsedData data = parseInput(input);
        Map<Integer, Set<Integer>> rules = data.rules;
        List<List<Integer>> updates = data.updates;

        int sum = 0;
        for (List<Integer> update : updates) {
            if (isValidOrder(update, rules)) {
                sum += findMiddleElement(update);
            }
        }

        return String.valueOf(sum);
    }

    /**
     * Expected output: 5900
     */
    @Override
    public String part2(List<String> input) {
        ParsedData data = parseInput(input);
        Map<Integer, Set<Integer>> rules = data.rules;
        List<List<Integer>> updates = data.updates;

        int sum = 0;
        for (List<Integer> update : updates) {
            if (!isValidOrder(update, rules)) {
                List<Integer> sortedUpdate = sortUpdate(update, rules);
                sum += findMiddleElement(sortedUpdate);
            }
        }

        return String.valueOf(sum);
    }

    private ParsedData parseInput(List<String> input) {
        Map<Integer, Set<Integer>> rules = new HashMap<>();
        List<List<Integer>> updates = new ArrayList<>();
        boolean ruledEnd = false;

        for (String line : input) {
            if (ruledEnd) {
                List<Integer> update = Arrays.stream(line.split(","))
                        .map(Integer::parseInt)
                        .toList();
                updates.add(update);
            } else {
                if (line.isEmpty()) {
                    ruledEnd = true;
                } else {
                    String[] parts = line.split("\\|");
                    int before = Integer.parseInt(parts[0]);
                    int after = Integer.parseInt(parts[1]);
                    rules.computeIfAbsent(before, k -> new HashSet<>()).add(after);
                }
            }
        }

        return new ParsedData(rules, updates);
    }

    private boolean isValidOrder(List<Integer> update, Map<Integer, Set<Integer>> rules) {
        for (int i = 0; i < update.size(); i++) {
            int page = update.get(i);
            if (rules.containsKey(page)) {
                for (int after : rules.get(page)) {
                    if (update.contains(after) && update.indexOf(after) < i) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private int findMiddleElement(List<Integer> list) {
        return list.get(list.size() / 2);
    }

    private List<Integer> sortUpdate(List<Integer> update, Map<Integer, Set<Integer>> rules) {
        List<Integer> sortedUpdate = new ArrayList<>(update);
        sortedUpdate.sort((page1, page2) -> {
            if (rules.containsKey(page1) && rules.get(page1).contains(page2)) {
                return -1;
            }
            if (rules.containsKey(page2) && rules.get(page2).contains(page1)) {
                return 1;
            }
            return 0;
        });
        return sortedUpdate;
    }

    private static class ParsedData {
        Map<Integer, Set<Integer>> rules;
        List<List<Integer>> updates;

        ParsedData(Map<Integer, Set<Integer>> rules, List<List<Integer>> updates) {
            this.rules = rules;
            this.updates = updates;
        }
    }

    public static void main(String[] args) {
        try {
            List<String> input = InputReader.readLines(YEAR_2024, DAY_05);

            Solution solution = new Day05();
            String solutionPart1 = solution.part1(input);
            String solutionPart2 = solution.part2(input);

            Utils.logResults(DAY_05, solutionPart1, solutionPart2);
        } catch (IOException e) {
            LOGGER.error("Error while reading data: {}", e.getMessage(), e);
        }
    }
}
