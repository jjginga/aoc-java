package com.jjginga.aoc.year2015;


import com.jjginga.aoc.Solution;
import com.jjginga.aoc.utils.InputReader;
import com.jjginga.aoc.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;

import static com.jjginga.aoc.utils.Constants.*;

public class Day13 implements Solution {
    private static final Logger LOGGER = LoggerFactory.getLogger(Day13.class);

    public static void main(String[] args) {
        try {
            List<String> input = InputReader.readLines(YEAR_2015, DAY_13);

            Day13 day13 = new Day13();
            String solutionPart1 = day13.part1(input);
            String solutionPart2 = day13.part2(input);

            Utils.logResults(DAY_13, solutionPart1, solutionPart2);
        } catch (IOException e) {
            LOGGER.error("Error reading input file for {}: {}", DAY_13, e.getMessage());
        } catch (Exception e) {
            LOGGER.error("An unexpected error occurred while processing {}: {}", DAY_13, e.getMessage(), e);
        }
    }


    private List<String> bestArrangment;
    private int bestHappiness;

    @Override
    public String part1(List<String> input) {

        var sittingPossibilities = parse(input);

        return String.valueOf(solve(sittingPossibilities, false));
    }

    @Override
    public String part2(List<String> input) {

        var sittingPossibilities = parse(input);

        return String.valueOf(solve(sittingPossibilities, true));
    }

    private int solve(Map<String, Map<String, Integer>> sittingPossibilities, boolean includeMyself) {

        if (includeMyself) {
            for (String person : new ArrayList<>(sittingPossibilities.keySet())) {
                sittingPossibilities.computeIfAbsent("me", k -> new HashMap<>()).put(person, 0);
                sittingPossibilities.get(person).put("me", 0);
            }
        }

        findMaxHappiness(sittingPossibilities);

        return bestHappiness;
    }

    private Map<String, Map<String, Integer>> parse(List<String> input) {
        Map<String, Map<String, Integer>> sittingPossibilities = new HashMap<>();
        //get info from strings
        input.forEach(s -> {
            var sList = s.split(" ");
            var person1 = sList[0];
            var person2 = sList[10].substring(0, sList[10].length() - 1);
            var happiness = Integer.parseInt(sList[3]) * (sList[2].equals("gain") ? 1 : -1);

            sittingPossibilities
                    .computeIfAbsent(person1, k-> new HashMap<>())
                    .put(person2, happiness);
        });
        return sittingPossibilities;
    }

    private void findMaxHappiness(Map<String, Map<String, Integer>> sittingPossibilities) {
        // We want to maximize happiness
        // This is a combinatorial optimization problem.
        // We can use DFS to explore all possible arrangements recursively
        // with branch and bound and backtracking.
        bestArrangment = null;
        bestHappiness = Integer.MIN_VALUE;

        // We try every person first, so we don't miss the optimum
        for(String person : sittingPossibilities.keySet()) {
            // Initialize DFS state
            List<String> sitted = new ArrayList<>();
            sitted.add(person);
            Set<String> unsitted = new HashSet<>(sittingPossibilities.keySet());
            unsitted.remove(person);

            dfs(sitted, unsitted, 0, sittingPossibilities);
        }
    }





    private void dfs(
            List<String> sitted,
            Set<String> unsitted,
            int happiness,
            Map<String, Map<String, Integer>> sittingPossibilities
    ) {
        // If all persons have been sitted, we update the best if the total happiness is bigger than current
        if(unsitted.isEmpty()){
            String person1 = sitted.getFirst();
            String person2 = sitted.getLast();
            int totalHappiness = happiness
                    + getHapiness(person1, sittingPossibilities, person2);
            if( totalHappiness > bestHappiness){
                bestArrangment = new ArrayList<>(sitted);
                bestHappiness = totalHappiness;
            }
            return;
        }

        var currentPerson = sitted.getLast();

        // Compute Bound: hapiness + max hapiness to sit remaning people + hapiness to connect bot sets
        int maxHappinessBound = happiness
                + mstBound(unsitted, sittingPossibilities)
                + maxEdge(unsitted, currentPerson, sittingPossibilities);

        // Prune - if bound is less happy than what we already havem we prune
        if(maxHappinessBound <= bestHappiness){
            return;
        }

        //We try sitting each person
        for(String person : new ArrayList<>(unsitted)) {
            sitted.add(person);
            unsitted.remove(person);

            var currHappiness = getHapiness(currentPerson, sittingPossibilities, person);

            dfs(sitted, unsitted, happiness + currHappiness, sittingPossibilities );

            //backtrack
            unsitted.add(person);
            sitted.remove(person);

        }
    }

    private int mstBound(Set<String> unsitted, Map<String, Map<String, Integer>> sittingPossibilities) {
        if(unsitted.size() <= 1) return 0;

        Set<String> tried = new HashSet<>();
        // Start from any person
        var startPerson =  unsitted.iterator().next();
        tried.add(startPerson);

        record Edge(int h, String sidePerson) {}

        Comparator<Edge> comparator = Comparator.comparingInt(Edge::h).reversed();

        PriorityQueue<Edge> pq = new PriorityQueue<>(comparator);

        // Push all edges from starting person into PQ
        for(String person : unsitted) {
            if(!person.equals(startPerson)) {
                int h = getHapiness(startPerson, sittingPossibilities, person);
                pq.add(new Edge(h, person));
            }
        }

        int bound = 0;

        //Prim's main loop
        while(tried.size() < unsitted.size() && !pq.isEmpty()) {
            //get the longest distance
            Edge edge = pq.poll();

            if(tried.contains(edge.sidePerson())) continue;

            tried.add(edge.sidePerson());
            bound += edge.h();

            //add new edges for currently added person
            for(String person : unsitted) {
                if(!tried.contains(person)) {
                    int h = getHapiness(edge.sidePerson(), sittingPossibilities, person);
                    pq.add(new Edge(h, person));
                }
            }
        }

        return bound;
    }

    private int maxEdge(Set<String> unsitted, String currentPerson, Map<String, Map<String, Integer>> sittingPossibilities) {
        return unsitted.stream()
                .mapToInt(p ->
                        getHapiness(currentPerson, sittingPossibilities, p)
                )
                .max()
                .orElse(0);
    }

    private int getHapiness(String currentPerson, Map<String, Map<String, Integer>> sittingPossibilities, String p) {
        return sittingPossibilities.get(currentPerson).get(p)
                + sittingPossibilities.get(p).get(currentPerson);
    }
}
