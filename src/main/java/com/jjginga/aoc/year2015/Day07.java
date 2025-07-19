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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.jjginga.aoc.utils.Constants.DAY_07;
import static com.jjginga.aoc.utils.Constants.YEAR_2015;

public class Day07 implements Solution {
    private static final Logger LOGGER = LoggerFactory.getLogger(Day07.class);

    public static void main(String[] args) {
        try {
            List<String> input = InputReader.readLines(YEAR_2015, DAY_07);

            Day07 day01 = new Day07();
            String solutionPart1 = day01.part1(input);
            String solutionPart2 = day01.part2(input);

            Utils.logResults(DAY_07, solutionPart1, solutionPart2);
        } catch (IOException e) {
            LOGGER.error("Error reading input file for {}: {}", DAY_07, e.getMessage());
        } catch (Exception e) {
            LOGGER.error("An unexpected error occurred while processing {}: {}", DAY_07, e.getMessage(), e);
        }
    }

    //possible gates
    private enum Gate { AND, LSHIFT, NOT, OR, RSHIFT, ASSIGN}

    private record Instruction(
            Gate gate,
            String in1, // for ASSIGN/NOT operand; for others left operand
            String in2, // for binary: right operand or shift amount; null otherwise
            String dest // the destination wire
    ) {}

    private final Map<String, Instruction> program = new HashMap<>();
    private final Map<String, Integer> cache = new HashMap<>();
    private int part1 = 0;

    // regex pattern that matches any of the instruction formats
    private static final Pattern WIRE_RX = Pattern.compile(
            "^(?:" +
                    // 1)direct assignment: 123 -> x   or   foo -> bar
                    "(?<in1>\\d+|[a-z]+)\\s+->\\s+(?<dest>[a-z]+)" +
                    "|" +
                    // 2)binary ops: x AND y -> d   or   x LSHIFT 2 -> f
                    "(?<lhs>\\d+|[a-z]+)\\s+" +
                    "(?<op>AND|OR|LSHIFT|RSHIFT)\\s+" +
                    "(?<rhs>\\d+|[a-z]+)\\s+->\\s+(?<dest2>[a-z]+)" +
                    "|" +
                    // 3)unary NOT: NOT x -> h
                    "NOT\\s+(?<src>[a-z]+)\\s+->\\s+(?<dest3>[a-z]+)" +
                    ")$"
    );

    @Override
    public String part1(List<String> input) {

        parseLines(input);

        part1  = eval("a");

        return String.valueOf(part1);
    }

    private void parseLines(List<String> input) {
        for (String line : input) {
            Matcher matcher = WIRE_RX.matcher(line);

            if(!matcher.matches()) {
                throw new IllegalStateException("Line does not match pattern: " + WIRE_RX.pattern());
            }

            //binary operatin
            String opText = matcher.group("op");
            if(opText != null) {
                Gate gate = Gate.valueOf(opText.toUpperCase());
                String left = matcher.group("lhs");
                String right = matcher.group("rhs");
                String dest = matcher.group("dest2");
                program.put(dest,
                        new Instruction(gate, left, right, dest));
                continue;
            }

            //not
            if(matcher.group("src") != null) {
                String src = matcher.group("src");
                String dest = matcher.group("dest3");
                program.put(dest,
                        new Instruction(Gate.NOT, src, null, dest));
                continue;
            }

            //direct assign
            String in1 = matcher.group("in1");
            String dest = matcher.group("dest");
            program.put(dest, new Instruction(Gate.ASSIGN, in1, null, dest));
        }
    }


    private int eval(String wireOrLiteral) {

        // If it is a literal number then parse and mask to 16 bits
        if (wireOrLiteral.matches("\\d+")) {
            return Integer.parseInt(wireOrLiteral) & 0XFFFF;
        }

        // If already computed
        // Each wire can only get a signal from one source
        if (cache.containsKey(wireOrLiteral)) {
            return cache.get(wireOrLiteral);
        }

        Instruction intstruction = program.get(wireOrLiteral);

        //if no instruction, then wire is undefined -> treated as zero
        if (intstruction == null) {
            cache.put(wireOrLiteral, 0);
            return 0;
        }

        int res;

        switch (intstruction.gate()){
            case ASSIGN -> res = eval(intstruction.in1());
            case NOT    -> res = ~eval(intstruction.in1());
            case AND    -> res = eval(intstruction.in1()) & eval(intstruction.in2());
            case OR     -> res = eval(intstruction.in1()) | eval(intstruction.in2());
            case LSHIFT -> res = eval(intstruction.in1()) << eval(intstruction.in2());
            case RSHIFT -> res = eval(intstruction.in1()) >>> eval(intstruction.in2());
            default -> throw new IllegalStateException("Unexpected gate: " + intstruction.gate());
        }

        res = res & 0xFFFF;
        cache.put(wireOrLiteral, res);
        return res;

    }


    @Override
    public String part2(List<String> input) {

        if (part1 == 0) {
            part1(input);
        }

        cache.clear();

        program.put("b", new Instruction(Gate.ASSIGN, String.valueOf(part1), null, "b"));

        return String.valueOf(eval("a"));
   }
}
