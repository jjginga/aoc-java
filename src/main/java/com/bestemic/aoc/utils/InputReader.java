package com.bestemic.aoc.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class InputReader {

    private InputReader() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated.");
    }

    public static List<String> readLines(String year, String day) throws IOException {
        String filePath = String.format("src/main/resources/%s/%s.txt", year, day);
        return Files.readAllLines(Paths.get(filePath));
    }

    public static List<String> readTestLines(String year, String day) throws IOException {
        String filePath = String.format("src/test/resources/%s/%s.txt", year, day);
        return Files.readAllLines(Paths.get(filePath));
    }
}