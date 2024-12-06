package com.bestemic.aoc.utils;

import java.util.List;

public class Grid {
    private final char[][] fields;
    private final int rows;
    private final int cols;
    private Point currentPosition;
    private Direction direction = null;

    public Grid(List<String> input) {
        this.rows = input.size();
        this.cols = input.getFirst().length();
        this.fields = new char[rows][cols];

        for (int i = 0; i < rows; i++) {
            fields[i] = input.get(i).toCharArray();
        }
    }

    public void findStart(char start) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (fields[i][j] == start) {
                    currentPosition = new Point(i, j);
                    return;
                }
            }
        }
    }

    public void findDirection() {
        char current = fields[currentPosition.getX()][currentPosition.getY()];
        switch (current) {
            case '^':
                direction = Direction.UP;
                break;
            case 'v':
                direction = Direction.DOWN;
                break;
            case '<':
                direction = Direction.LEFT;
                break;
            case '>':
                direction = Direction.RIGHT;
                break;
            default:
                break;
        }
    }

    public void moveForward(char mark) {
        fields[currentPosition.getX()][currentPosition.getY()] = mark;
        currentPosition = getNextPosition();
    }

    private Point getNextPosition() {
        int x = currentPosition.getX();
        int y = currentPosition.getY();

        switch (direction) {
            case UP:
                x--;
                break;
            case DOWN:
                x++;
                break;
            case LEFT:
                y--;
                break;
            case RIGHT:
                y++;
                break;
            default:
                break;
        }

        return new Point(x, y);
    }

    public void turnLeft() {
        direction = Direction.values()[(direction.ordinal() + 3) % 4];
    }

    public void turnRight() {
        direction = Direction.values()[(direction.ordinal() + 1) % 4];
    }

    public int countOccurrences(char target) {
        int count = 0;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (fields[i][j] == target) {
                    count++;
                }
            }
        }

        return count;
    }

    public char getNextCharacter() {
        Point newPosition = getNextPosition();
        int x = newPosition.getX();
        int y = newPosition.getY();

        if (x >= 0 && x < rows && y >= 0 && y < cols) {
            return fields[x][y];
        } else {
            return '!';
        }
    }
}
