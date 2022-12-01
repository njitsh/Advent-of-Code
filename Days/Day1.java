package Days;
import java.util.*;

public class Day1 extends Day {
    public static void main(String[] args) {
        System.out.println(part1());
        System.out.println(part2());
    }

    public List<String> getInput(String location) {
        return super.getInput(location);
    }

    public static String part1() {
        List<String> input = new Day1().getInput("Days/input_day1.txt");
        int maxCalories = 0;
        int currentCalories = 0;

        for (String line : input) {
            if (line.equals("")) {
                maxCalories = Math.max(maxCalories, currentCalories);
                currentCalories = 0;
            } else {
                currentCalories += Integer.parseInt(line);
            }
            maxCalories = Math.max(maxCalories, currentCalories);
            currentCalories = 0;
        }

        return Integer.toString(maxCalories);
    }

    public static String part2() {
        List<String> input = new Day1().getInput("Days/input_day1.txt");
        int[] topCalories = new int[3];
        
        int currentCalories = 0;

        for (String line : input) {
            if (line.equals("")) {
                topCalories = setMax(topCalories, currentCalories);
                currentCalories = 0;
            } else {
                currentCalories += Integer.parseInt(line);
            }
        }
        topCalories = setMax(topCalories, currentCalories);
        currentCalories = 0;

        return Integer.toString(topCalories[0] + topCalories[1] + topCalories[2]);
    }

    private static int[] setMax(int[] topCalories, int currentCalories) {
        for (int i = 0; i < topCalories.length; i++) {
            if (topCalories[i] < currentCalories) {
                for (int j = topCalories.length - 2; i <= j; j--) {
                    topCalories[j + 1] = topCalories[j];
                }
                topCalories[i] = currentCalories;

                return topCalories;
            }
        }
        return topCalories;
    }
}
