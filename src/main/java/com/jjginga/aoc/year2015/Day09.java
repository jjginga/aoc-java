package com.jjginga.aoc.year2015;


import com.jjginga.aoc.Solution;
import com.jjginga.aoc.utils.InputReader;
import com.jjginga.aoc.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.jjginga.aoc.utils.Constants.DAY_09;
import static com.jjginga.aoc.utils.Constants.YEAR_2015;

public class Day09 implements Solution {
    private static final Logger LOGGER = LoggerFactory.getLogger(Day09.class);

    public static void main(String[] args) {
        try {
            List<String> input = InputReader.readLines(YEAR_2015, DAY_09);

            Day08 day01 = new Day08();
            String solutionPart1 = day01.part1(input);
            String solutionPart2 = day01.part2(input);

            Utils.logResults(DAY_09, solutionPart1, solutionPart2);
        } catch (IOException e) {
            LOGGER.error("Error reading input file for {}: {}", DAY_09, e.getMessage());
        } catch (Exception e) {
            LOGGER.error("An unexpected error occurred while processing {}: {}", DAY_09, e.getMessage(), e);
        }
    }

    // These variables are global state because we need the recursive dfs to update them
    private int bestCost;
    private List<String> bestPath;
    private Map<String, Map<String, Integer>> distances;


    @Override
    public String part1(List<String> input) {
        // Build map with distance and city maps
        distances = new HashMap<>();
        Set<String> cities = new HashSet<>();
        parseCities(input, cities);

        //we want the shortest path, so we start with an infinit cost and improve
        bestPath = null;
        bestCost = Integer.MAX_VALUE;

        // We try every city, so we don't miss the optimum
        for (String startCity : cities) {
            // Initialize DFS state
            List<String> visited = new ArrayList<>();
            visited.add(startCity);
            Set<String> unvisited = new HashSet<>(cities);
            unvisited.remove(startCity);

            //start dfs with branch and bound, we want to minimize so, maximize is false
            dfs(visited, unvisited, 0, false);
        }
        return String.valueOf(bestCost);
    }

    @Override
    public String part2(List<String> input) {
        // Same parsing as part 1
        distances = new HashMap<>();
        Set<String> cities = new HashSet<>();
        parseCities(input, cities);

        // We want the longest path, so start with the lowest value possible and improve
        bestPath = null;
        bestCost = Integer.MIN_VALUE;

        //we interate over every city
        for(String s : cities) {
            // Initialize DFS state and run branch-and-bound
            List<String> visited = new ArrayList<>();
            visited.add(s);
            Set<String> unvisited = new HashSet<>(cities);
            unvisited.remove(s);

            // We want the longest so maximize is true
            dfs(visited, unvisited, 0, true);
        }
        return String.valueOf(bestCost);
    }

    /**
     * Parse lines of the form "A to B = 123" and fills the 'distances' map and cities set
     *
     * @param input - the input with all the lines
     * @param cities - empty set for cities
     */
    private void parseCities(List<String> input, Set<String> cities) {
        // Parse input into distances and cities
        Pattern pattern = Pattern.compile("^(.+) to (.+) = (\\d+)$");
        input.forEach(line -> {
            Matcher matcher = pattern.matcher(line);
            if (!matcher.matches()) throw new IllegalArgumentException("Invalid input: " + line);
            //get data
            String origin = matcher.group(1);
            String destination = matcher.group(2);
            int distance = Integer.parseInt(matcher.group(3));
            //add to structures
            distances
                    .computeIfAbsent(origin, k -> new HashMap<>())
                    .put(destination, distance);
            distances
                    .computeIfAbsent(destination, k -> new HashMap<>())
                    .put(origin, distance);

            cities.add(origin);
            cities.add(destination);
        });
    }

    /**
     * Recursive DFS with Branch-and_Bound
     * @param visited - partial path, already built (ordered list)
     * @param unvisited - set of cities left to visit
     * @param cost - accumolated cost of the partial path
     * @param maximize - false = find the minimum path, true = find maximum path
     * <p>
     * Strategy
     * 1. If there are no more cities, we compare the cost with the best cost
     * 2. We calculate a Bound and prune that branch if it can't beat the current bestCost
     * 3. For branching we try each next city in unvisited, recurse,  and then we backtrack to explore alternatives
     */
    private void dfs(List<String> visited, Set<String> unvisited, int cost, boolean maximize) {
        // All cities visited, update best if the path beats the current best
        if(unvisited.isEmpty()) {
            if (better(cost, maximize)) {
                bestCost = cost;
                bestPath = new ArrayList<>(visited); //we create a snapshot so that it isn't
                //changed later
            }
            return;
        }

        var current = last(visited);

        // Compute bound: cost + MST(unvisited) + edge to connect current path to that set
        // For minimize we use the lower bound and for maximize the upper bound.
        int bound = cost
                  + mstBound(unvisited, maximize) //min or max cost to visit all the remaining cities
                  + (maximize ? maxEdge(unvisited, current) : minEdge(unvisited, current)); //connect to the rest

        // Prune - If bound is more costly (for min) or less costrly (for max) than what we already have we prune
        if(prune(bound, maximize)) {
            return;
        }

        // Branch: try each next city
        // We must snapshot the current contents of unvisited to a fresh list
        // to avoid concurrent modification
        for (String next : new ArrayList<>(unvisited)) {
            // We reuse same visited and unvisited to keet backtracking efficient
            // without extra allocations
            visited.add(next);
            unvisited.remove(next);

            var distance = distances.get(current).get(next);

            dfs(visited, unvisited, cost + distance, maximize);
            //Backtrack
            unvisited.add(next);
            visited.remove(next);
        }
    }

    /** Return true if this path beats the current best (min or max depending on 'maximize'). */
    private boolean prune(int bound, boolean maximize) {
        return maximize ? bound <= bestCost : bound >= bestCost;
    }

    /** Return true if branch can be pruned given the bound and the current best */
    private boolean better(int cost, boolean maximize) {
        return maximize ?  cost > bestCost : cost < bestCost;
    }


    // https://www.w3schools.com/dsa/dsa_algo_mst_prim.php

    /**
     * Prim-based MST bound over the remaining nodes
     * For minimize: we sum the lightest edges
     * For maximize: we sum the heaviest edges
     */
    private int mstBound(Set<String> nodes, boolean maximize) {
        if (nodes.size() <= 1) return 0;

        Set<String> seen = new HashSet<>();
        //start from any node
        var start = nodes.iterator().next();
        seen.add(start);

        record Edge(int w, String to) {}

        Comparator<Edge> comparator = Comparator.comparingInt(Edge::w);
        comparator = maximize ? comparator.reversed() : comparator;

        PriorityQueue<Edge> pq = new PriorityQueue<>(comparator);

        //push all edges from starting node into PQ
        for(String n : nodes){
            if(!n.equals(start)){
                pq.add(new Edge(distances.get(start).get(n), n));
            }
        }

        int bound = 0;

        //Prim's main loop
        while(seen.size() < nodes.size() && !pq.isEmpty()){
            //get longest/shortest distance
            var edge = pq.poll();

            if(seen.contains(edge.to)) continue; //if already connected

            seen.add(edge.to);
            bound += edge.w;

            // add new edges from the currently added node
            for(String n : nodes){
                if(!seen.contains(n)){
                    pq.add(new Edge(distances.get(edge.to).get(n), n));
                }
            }

        }

        return bound;
    }

    /** Last city in the current path*/
    private String last(List<String> visited) {
        return visited.getLast();
    }

    /** Cheapest edge from current into unvisited set (0 if empty) */
    private int minEdge(Set<String> unvisited, String current) {
        return unvisited.stream()
                .mapToInt(n -> distances.get(current).get(n))
                .min()
                .orElse(0);

    }

    /** Most expensive edge from 'current' ino the 'unvisited set' (0 if empty)*/
    private int maxEdge(Set<String> unvisited, String current) {
        return unvisited.stream()
                .mapToInt(n -> distances.get(current).get(n))
                .max()
                .orElse(0);

    }

}
