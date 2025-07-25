package com.jjginga.aoc.year2015;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.jjginga.aoc.Solution;
import com.jjginga.aoc.utils.InputReader;
import com.jjginga.aoc.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;

import static com.jjginga.aoc.utils.Constants.*;

public class Day12 implements Solution {
    private static final Logger LOGGER = LoggerFactory.getLogger(Day12.class);

    public static void main(String[] args) {
        try {
            List<String> input = InputReader.readLines(YEAR_2015, DAY_12);

            Day12 day12 = new Day12();
            String solutionPart1 = day12.part1(input);
            String solutionPart2 = day12.part2(input);

            Utils.logResults(DAY_11, solutionPart1, solutionPart2);
        } catch (IOException e) {
            LOGGER.error("Error reading input file for {}: {}", DAY_12, e.getMessage());
        } catch (Exception e) {
            LOGGER.error("An unexpected error occurred while processing {}: {}", DAY_12, e.getMessage(), e);
        }
    }

    @Override
    public String part1(List<String> input) {
        Pattern pattern = Pattern.compile("[-]?\\d+");
        Matcher matcher = pattern.matcher(input.getFirst());
        int sum = 0;
        while(matcher.find()) {
            sum += Integer.parseInt(matcher.group());
        }
        return String.valueOf(sum);
    }



    @Override
    public String part2(List<String> input) {
       ObjectMapper objectMapper = new ObjectMapper();
       JsonNode rootNode;
        try {
            rootNode = objectMapper.readTree(input.getFirst());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        List<JsonNode> redNodes = new ArrayList<>();
        findRedObjects(rootNode, redNodes);

        ArrayNode arryNode = objectMapper.createArrayNode();
        redNodes.forEach(arryNode::add);
        String discaterString = arryNode.toString();

        int result = Integer.parseInt(part1(input)) - Integer.parseInt(part1(List.of(discaterString)));
        return String.valueOf(result);
    }

    private void findRedObjects(JsonNode node, List<JsonNode> redNodes) {
        if(node.isArray()) {
            node.forEach(n->findRedObjects(n, redNodes));
            return;
        }

        boolean hasRed = false;
        for(JsonNode n : node) {
            if(n.isTextual() && n.asText().equals("red")) hasRed = true;
        }

        if(hasRed){
            redNodes.add(node);
            return;
        }

        node.forEach(n->findRedObjects(n, redNodes));
    }


}
