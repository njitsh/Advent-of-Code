package Days;
import java.util.*;

public class Day6 extends Day {
    public static void main(String[] args) {
        System.out.println(new Day6().part1());
        System.out.println(new Day6().part2());
    }

    public List<String> getInput(String location) {
        return super.getInput(location);
    }

    public String part1() {
        List<String> input = new Day6().getInput("Days/input_day6.txt");

        // Input is only one line
        String input_line = input.get(0);

        int startOfPacketMarker = findPacketMarker(input_line, 4);

        return Integer.toString(startOfPacketMarker);
    }

    public String part2() {
        List<String> input = new Day6().getInput("Days/input_day6.txt");

        // Input is only one line
        String input_line = input.get(0);

        int startOfPacketMarker = findPacketMarker(input_line, 14);

        return Integer.toString(startOfPacketMarker);
    }

    private static int findPacketMarker(String input, int markerSize) {
        // Create queue containing the last 4 characters
        Queue<Character> queue = new LinkedList<>();
        
        for (int i = 0; i < input.length(); i++) {
            // Add the current character to the queue
            queue.add(input.charAt(i));
            
            if (i >= markerSize) {
                // Remove the first character from the queue
                queue.remove();
            }

            // Check if the queue contains duplicate characters
            if (queue.size() == markerSize && !checkDuplicateChars(queue)) {
                return i + 1;
            }
        }
        throw new IllegalArgumentException("No packet marker found");
    }

    private static boolean checkDuplicateChars(Queue<Character> queue) {
        // Create a set to store the characters
        Set<Character> set = new HashSet<>();
        
        // Loop through the queue
        for (Object character : queue) {
            // If the character is already in the set, return true
            if (set.contains(character)) {
                return true;
            }
            
            // Add the character to the set
            set.add((Character) character);
        }
        
        // No duplicate characters found
        return false;
    }
}
