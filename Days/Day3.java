package Days;
import java.util.*;

public class Day3 extends Day {
    public static void main(String[] args) {
        System.out.println(part1());
        System.out.println(part2());
    }

    public List<String> getInput(String location) {
        return super.getInput(location);
    }

    public static String part1() {
        List<String> input = new Day3().getInput("Days/input_day3.txt");

        int priorityScore = 0;

        for (String line : input) {
            String half_1 = line.substring(0, line.length() / 2);
            String half_2 = line.substring(line.length() / 2);

            char overlap = findCommonCharacter(half_1, half_2);
            priorityScore += getCharPriority(overlap);
        }

        return Integer.toString(priorityScore);
    }

    public static String part2() {
        List<String> input = new Day3().getInput("Days/input_day3.txt");

        int priorityScore = 0;
        
        for (int i = 0; i < input.size(); i += 3) {
            String line_1 = input.get(i);
            String line_2 = input.get(i + 1);
            String line_3 = input.get(i + 2);

            char overlap = findBadgeCharacter(line_1, line_2, line_3);
            priorityScore += getCharPriority(overlap);
        }

        return Integer.toString(priorityScore);
    }

    private static char findCommonCharacter(String half_1, String half_2) {
        for (int i = 0; i < half_1.length(); i++) {
            for (int j = 0; j < half_2.length(); j++) {
                if (half_1.charAt(i) == half_2.charAt(j)) {
                    return half_1.charAt(i);
                }
            }
        }
        
        // No item found
        throw new IllegalArgumentException("No common character found");
    }

    private static int getCharPriority(char character) {
        if (character >= 'A' && character <= 'Z') {
            return character - 'A' + 27;
        } else if (character >= 'a' && character <= 'z') {
            return character - 'a' + 1;
        } else {
            throw new IllegalArgumentException("Invalid character");
        }
    }

    private static char findBadgeCharacter(String elf_1, String elf_2, String elf_3) {
        for (int i = 0; i < elf_1.length(); i++) {
            for (int j = 0; j < elf_2.length(); j++) {
                if (elf_1.charAt(i) == elf_2.charAt(j)) {
                    for (int k = 0; k < elf_3.length(); k++) {
                        if (elf_1.charAt(i) == elf_3.charAt(k)) {
                            return elf_1.charAt(i);
                        }
                    }
                }
            }
        }
        
        // No item found
        throw new IllegalArgumentException("No common character found");
    }
}
