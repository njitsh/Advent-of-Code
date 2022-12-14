package Days;
import java.util.*;

public class Day8 extends Day {
    private static boolean forestPrint = false;

    public static void main(String[] args) {
        forestPrint = true;
        System.out.println(new Day8().part1());
        System.out.println(new Day8().part2());
    }

    public List<String> getInput(String location) {
        return super.getInput(location);
    }

    public String part1() {
        List<String> input = new DayX().getInput("2022/Days/input_day8.txt");

        // Convert the input to a matrix
        int[][] forest = convertInputToMatrix(input);

        boolean[][] visible = new boolean[forest.length][forest[0].length];

        visible = checkVisibleTrees(forest, visible);

        int visibleTrees = countVisibleTrees(visible);

        return Integer.toString(visibleTrees);
    }

    public String part2() {
        List<String> input = new DayX().getInput("2022/Days/input_day8.txt");

        // Convert the input to a matrix
        int[][] forest = convertInputToMatrix(input);

        int maxScenicScore = findScenicScore(forest);

        return Integer.toString(maxScenicScore);
    }

    private static int[][] convertInputToMatrix(List<String> input) {
        int[][] matrix = new int[input.size()][input.get(0).length()];

        for (int i = 0; i < input.size(); i++) {
            for (int j = 0; j < input.get(i).length(); j++) {
                matrix[i][j] = input.get(i).charAt(j);
            }
        }

        return matrix;
    }

    private static boolean[][] checkVisibleTrees(int[][] forest, boolean[][] visible) {
        // Left to right
        int[] treeLine = new int[forest.length];
        for (int i = 0; i < forest.length; i++) {
            for (int j = 0; j < forest[i].length; j++) {
                if (forest[i][j] > treeLine[i]) {
                    treeLine[i] = forest[i][j];
                    visible[i][j] = true;
                }
            }
            printVisibleForest(visible);
        }

        // Top to bottom
        treeLine = new int[forest[0].length];
        for (int i = 0; i < forest[0].length; i++) {
            for (int j = 0; j < forest.length; j++) {
                if (forest[j][i] > treeLine[i]) {
                    treeLine[i] = forest[j][i];
                    visible[j][i] = true;
                }
            }
            printVisibleForest(visible);
        }

        // Right to left
        treeLine = new int[forest.length];
        for (int i = 0; i < forest.length; i++) {
            for (int j = forest[i].length - 1; j >= 0; j--) {
                if (forest[i][j] > treeLine[i]) {
                    treeLine[i] = forest[i][j];
                    visible[i][j] = true;
                }
            }
            printVisibleForest(visible);
        }

        // Bottom to top
        treeLine = new int[forest[0].length];
        for (int i = 0; i < forest[0].length; i++) {
            for (int j = forest.length - 1; j >= 0; j--) {
                if (forest[j][i] > treeLine[i]) {
                    treeLine[i] = forest[j][i];
                    visible[j][i] = true;
                }
            }
            printVisibleForest(visible);
        }

        return visible;
    }

    private static void printVisibleForest(boolean[][] visible) {
        if (!forestPrint) return;

        // Clear terminal screen
        System.out.print("\033[H\033[2J");

        for (int i = 0; i < visible.length; i++) {
            for (int j = 0; j < visible[i].length; j++) {
                System.out.print(visible[i][j] ? "#" : ".");
            }
            System.out.println();
        }
    }

    private static int countVisibleTrees(boolean[][] visible) {
        int count = 0;

        for (int i = 0; i < visible.length; i++) {
            for (int j = 0; j < visible[i].length; j++) {
                if (visible[i][j]) count++;
            }
        }

        return count;
    }

    private static int findScenicScore(int[][] forest) {
        int maxScore = 0;

        for (int i = 0; i < forest.length; i++) {
            for (int j = 0; j < forest[i].length; j++) {
                int upDistance = 0;
                int rightDistance = 0;
                int downDistance = 0;
                int leftDistance = 0;

                int highestTree = 0;
                // Up
                while (highestTree < forest[i][j] && i - upDistance > 0) {
                    upDistance++;
                    if (forest[i - upDistance][j] > highestTree) {
                        highestTree = forest[i - upDistance][j];
                    }
                }
                
                highestTree = 0;
                // Right
                while (highestTree < forest[i][j] && j + rightDistance < forest[i].length - 1) {
                    rightDistance++;
                    if (forest[i][j + rightDistance] > highestTree) {
                        highestTree = forest[i][j + rightDistance];
                    }
                }

                highestTree = 0;
                // Down
                while (highestTree < forest[i][j] && i + downDistance < forest.length - 1) {
                    downDistance++;
                    if (forest[i + downDistance][j] > highestTree) {
                        highestTree = forest[i + downDistance][j];
                    }
                }

                highestTree = 0;
                // Left
                while (highestTree < forest[i][j] && j - leftDistance > 0) {
                    leftDistance++;
                    if (forest[i][j - leftDistance] > highestTree) {
                        highestTree = forest[i][j - leftDistance];
                    }
                }

                int score = calculateScenicScore(upDistance, rightDistance, downDistance, leftDistance);
                if (score > maxScore) {
                    maxScore = score;
                }
            }
        }

        return maxScore;
    }

    private static int calculateScenicScore(int up, int right, int down, int left) {
        return up * right * down * left;
    }
}
