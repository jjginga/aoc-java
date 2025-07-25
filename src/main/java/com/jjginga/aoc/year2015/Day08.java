package com.jjginga.aoc.year2015;


import com.jjginga.aoc.Solution;
import com.jjginga.aoc.utils.InputReader;
import com.jjginga.aoc.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

import static com.jjginga.aoc.utils.Constants.*;

public class Day08 implements Solution {
    private static final Logger LOGGER = LoggerFactory.getLogger(Day08.class);

    public static void main(String[] args) {
        try {
            List<String> input = InputReader.readLines(YEAR_2015, DAY_08);

            Day08 day08 = new Day08();
            String solutionPart1 = day08.part1(input);
            String solutionPart2 = day08.part2(input);

            Utils.logResults(DAY_08, solutionPart1, solutionPart2);
        } catch (IOException e) {
            LOGGER.error("Error reading input file for {}: {}", DAY_08, e.getMessage());
        } catch (Exception e) {
            LOGGER.error("An unexpected error occurred while processing {}: {}", DAY_08, e.getMessage(), e);
        }
    }

    /**
     * Holds the character count for one literal line:
     * - numCharsCode: the raw length including quotes and escapes (part1)
     * - numCharsMemory: the length after processing escape sequences and quotation marks (part1)
     * - numCharsEncoded: the length of the encoded string (part 2)
     */
    private record NumChars (int numCharsCode, int numCharsMemory, int numCharsEncoded) {

        /**
         * Combine two NumChars by summing their respective fields.
         */
        NumChars add(NumChars other) {
            return new NumChars(this.numCharsCode + other.numCharsCode(),
                    this.numCharsMemory+other.numCharsMemory(),
                    this.numCharsEncoded+other.numCharsEncoded());
        }
    }

    @Override
    public String part1(List<String> input) {
        // Map each input line to its (codeLen, memoryLen) pair,
        // then sum them into one NumChars aggregate.
        var numChars = input.stream()
                .map(this::getNumChars)
                .reduce(new NumChars(0,0, 0), NumChars::add);

        // Calculate the difference
        return String.valueOf(numChars.numCharsCode() - numChars.numCharsMemory());
    }

    /**
     * Given  one quoted string literal s, compute
     * - numCharsCode: the total length including surrounding quotes and escape characters
     * - numCharsMemory: the length without surrounding quotes or composite characters are interpreted has one
     * - numCharsEncoded: the length of the encoded string where (" -> "\", \ -> \\)
     * we scan from index = 1 to length - 1 to skip the bounding quotes,
     * handling each escape in a single O(n) pass.
     */
    private NumChars getNumChars(String s) {
        int memLen = 0; // counter for characters that would be in memory
        int codeLen;    // counter for total characters
        int encodedLen = 3; //beginning we have " -> "\"

        // iterate from just after the opening quote
        // up until just before the closing quote
        for( codeLen = 1; codeLen < s.length() -1;){
            var c = s.charAt(codeLen);
            memLen++; //every iteration adds exactly one in-memory character

            if(c == '\\'){
                //we found a backlash, so this begin an escape sequence
                var n = s.charAt(codeLen+1);
                encodedLen+=2; // because \ -> \\

                // '\\' and '\"' each represent one memory character and two code chars
                if(n == '\\' || n == '"'){
                    codeLen+=2;
                    encodedLen+=2; // because \ -> \\ and " -> \"
                    continue;
                }

                // '\xHH' represents one memory character and four code chars
                if(n == 'x'){
                    codeLen+=4;
                    encodedLen+=3; //we have already counted the \\
                    continue;
                }

                // every other escape is invalid
                throw new InvalidEscapeSequenceException ("Unrecognized escape sequence: \\"+n);
            }

            // normal (non-escape) character: move to next code char
            codeLen++;
            encodedLen++;
        }
        codeLen++; //cound closing quote
        encodedLen+=3; //closing quote
        //return (numCharsCode, numCharsMemory, numCharEncoded)
        return new NumChars(codeLen, memLen, encodedLen);
    }

    @Override
    public String part2(List<String> input) {
        // Map each input line to its (codeLen, memoryLen, encodedLen) pair,
        // then sum them into one NumChars aggregate.
        var numChars = input.stream()
                .map(this::getNumChars)
                .reduce(new NumChars(0,0, 0), NumChars::add);

        // Calculate the difference
        return String.valueOf(numChars.numCharsEncoded() - numChars.numCharsCode());
   }
}

/**
 * Throw when encountering an escape not in '\\', '\"', '\xHH'
 */
class InvalidEscapeSequenceException extends IllegalArgumentException {
    public InvalidEscapeSequenceException(String message) { super(message); }
}