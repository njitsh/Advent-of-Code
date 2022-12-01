package Days;

public class Day {
    public String part(int part) {
        if (part == 1) {
            return part_1();
        } else if (part == 2) {
            return part_2();
        } else {
            return "Invalid part number";
        }
    }

    public String part_1() {
        return "Day X, Part 1";
    }

    public String part_2() {
        return "Day X, Part 2";
    }
}
