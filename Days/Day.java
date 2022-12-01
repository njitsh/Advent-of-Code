package Days;
import java.io.*;
import java.util.*;

public class Day {
    public String part(int part) {
        if (part == 1) {
            return part1();
        } else if (part == 2) {
            return part2();
        } else {
            return "Invalid part number";
        }
    }

    // Get input from file at location and return as String
    public List<String> getInput(String location) {
        List<String> input = new ArrayList<>();

        try {
            File file = new File(location);
            Scanner sc = new Scanner(file);
            while (sc.hasNextLine()) {
                input.add(sc.nextLine());
            }
            sc.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException("File not found");
        }

        return input;
    }

    public static String part1() {
        return "Day X, Part 1";
    }

    public static String part2() {
        return "Day X, Part 2";
    }
}
