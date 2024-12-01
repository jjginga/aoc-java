# Advent of Code Java Solutions

Welcome to my repository of solutions for [Advent of Code](https://adventofcode.com/) challenges, implemented in Java!
This project organizes solutions by year and day, offering a clean and modular template that makes it easy to work with
various puzzles.

## Table of Contents

- [Overview](#overview)
- [Project Structure](#project-structure)
- [Getting Started](#getting-started)
- [Running Solutions](#running-solutions)
- [License](#license)

## Overview

This repository contains Java solutions for Advent of Code, an annual series of programming puzzles in December. Each
solution is organized by year and day to allow easy navigation. This project template is designed to be extensible and
efficient, with utility methods to handle input and other common tasks.

### Features

- Modular structure with separate packages for each year and day.
- Common utilities for input handling, parsing, and other helper functions.
- Interface-driven approach to manage puzzle solutions.

## Project Structure

```plaintext
advent-of-code-java/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/bestemic/aoc/
│   │   │       ├── year2023/            # Solutions for 2023
│   │   │       │   ├── Day01.java       # Solution for Day 1 of 2023
│   │   │       │   └── Day02.java       # Solution for Day 2 of 2023
│   │   │       ├── Solution.java        # Interface for solutions
│   │   │       └── utils/               # Utility package
│   │   └── resources/                   # Input files by year and day
│   └── test/
│       ├── java/
│       │   └── com/bestemic/aoc/
│       │       └── year2023/            # Tests for 2023 solutions
│       │           └── Day01Test.java   # Unit tests for Day 1 of 2023
│       └── resources/                   # Test input files by year and day
├── README.md
└── pom.xml
```

- **Solution.java**: Interface defining `part1` and `part2` methods for consistency across all solutions.
- **Utils**: Contains utilities for input handling and parsing.
- **Resources**: Stores input files, organized by year and day.
- **Test Resources**: Stores separate test input files based on sample data from the Advent of Code problems.

### Notes on Testing

Tests are organized by year, with one test file per day. Each test file can include unit tests for both `part1`
and `part2` methods, validating results against sample data from the Advent of Code problem descriptions.

## Getting Started

### Prerequisites

- Java 21
- [Maven](https://maven.apache.org/) for dependency management

### Setup

1. **Clone the repository**:
   ```bash
   git clone https://github.com/bestemic/advent-of-code-java.git
   cd advent-of-code-java
   ```

2. **Build the project**:
   ```bash
   mvn clean install
   ```

3. **Add input files**:

   Add input files for each day's challenge in the `src/main/resources/` directory, following the naming
   convention `yearYYYY/dayDD.txt` (e.g., `year2023/day01.txt`).

## Running Solutions

Each day’s solution can be run from its own implementation class, where each class contains a `main` method. This allows
you to execute the solution directly without needing a separate main application class. The `main` method reads the
input from the specified resources file and passes it to the appropriate solution methods.

### Example Code to Run a Solution

Here's an example code snippet to run the solution for Day 1 of 2023 from the `Day01` class:

```java
package com.bestemic.adventofcode.year2023;

import com.bestemic.adventofcode.utils.InputUtils;

import java.util.List;
import java.util.logging.Logger;

public class Day01 {
    private static final Logger LOGGER = LoggerFactory.getLogger(Day01.class);

    public static void main(String[] args) {
        List<String> input = InputUtils.readInput("year2023/day01.txt");

        Day01 day01 = new Day01();
        String solutionPart1 = day01.part1(input);
        String solutionPart2 = day01.part2(input);

        Utils.logResults(DAY_01, solutionPart1, solutionPart2);
    }

    public String part1(List<String> input) {
        // Implementation of part 1
    }

    public String part2(List<String> input) {
        // Implementation of part 2
    }
}
```

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.

---
