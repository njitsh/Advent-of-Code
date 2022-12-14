package Days;
import java.util.*;

public class Day5 extends Day {
    private static boolean stackPrint = false;

    public static void main(String[] args) {
        stackPrint = true;
        System.out.println(new Day5().part1());
        System.out.println(new Day5().part2());
    }

    public List<String> getInput(String location) {
        return super.getInput(location);
    }

    public String part1() {
        return crateMover(false);
    }

    public String part2() {
        return crateMover(true);
    }

    private static String crateMover(boolean moveTogether) {
        List<String> input = new Day5().getInput("2022/Days/input_day5.txt");

        // Find input split line
        int splitLine = findSplitLine(input);

        // Create stacks
        List<Stack<Character>> stacks = createStacks(input.subList(0, splitLine), splitLine);

        // Print stacks
        printStacks(stacks);

        // Move stacks
        stacks = executeDirections(stacks, input.subList(splitLine + 1, input.size()), moveTogether);

        // Get top crates
        String topCrates = getTopCrates(stacks);

        return topCrates;
    }

    private static int findSplitLine(List<String> input) {
        int splitLine = 0;

        // Find split line
        for (String line : input) {
            if (line.equals("")) {
                break;
            }
            splitLine++;
        }

        return splitLine;
    }

    private static List<Stack<Character>> createStacks(List<String> input, int splitLine) {
        // Find number of stacks
        final int numberOfStacks = input.get(splitLine - 1).split("   ").length;
        List<Stack<Character>> stacks = new ArrayList<Stack<Character>>();

        for (int i = 0; i < numberOfStacks; i++) {
            stacks.add(new Stack<Character>());
        }

        // Fill stacks
        for (int i = splitLine - 2; i >= 0; i--) {
            String line = input.get(i);
            
            for (int j = 1; j < line.length(); j += 4) {
                if (line.charAt(j) != ' ') {
                    stacks.get((j - 1) / 4).push(line.charAt(j));
                }
            }
        }

        return stacks;
    }

    private static void printStacks(List<Stack<Character>> stacks) {
        if (!stackPrint) return;

        // Clear terminal screen
        System.out.print("\033[H\033[2J");

        // Find longest stack
        int stackSize = 0;

        for (Stack<Character> stack : stacks) {
            if (stack.size() > stackSize) {
                stackSize = stack.size();
            }
        }

        // System.out.flush();
        for (int i = stackSize - 1; i >= 0; i--) {
            for (int j = 0; j < stacks.size(); j++) {
                if (stacks.get(j).size() > i) {
                    System.out.print(" " + stacks.get(j).get(i) + " ");
                } else {
                    System.out.print("   ");
                }
            }
            System.out.println();
        }

        // Print stack numbers
        for (int i = 0; i < stacks.size(); i++) {
            System.out.print(" " + i + " ");
        }
        System.out.println();

        // Wait for 10ms
        try {
            Thread.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static List<Stack<Character>> moveCratesSeparately(List<Stack<Character>> stacks, int amount, int stackFrom, int stackTo) {
        for (int i = 0; i < amount; i++) {
            // Return if stack is empty
            if (stacks.get(stackFrom).isEmpty()) throw new IllegalArgumentException("Stack " + stackFrom + " is empty");

            Character crate = stacks.get(stackFrom).pop();
            stacks.get(stackTo).push(crate);
            printStacks(stacks);
        }

        return stacks;
    }

    private static List<Stack<Character>> moveCratesTogether(List<Stack<Character>> stacks, int amount, int stackFrom, int stackTo) {
        // Return if stack is empty
        if (stacks.get(stackFrom).isEmpty()) throw new IllegalArgumentException("Stack " + stackFrom + " is empty");

        Stack<Character> temporaryStack = new Stack<Character>();

        // Remove crates from stack
        for (int i = 0; i < amount; i++) {
            char crate = stacks.get(stackFrom).pop();
            temporaryStack.push(crate);
        }

        // Add crates to stack
        for (int i = 0; i < amount; i++) {
            char crate = (char) temporaryStack.pop();
            stacks.get(stackTo).push(crate);
        }

        return stacks;
    }

    private static List<Stack<Character>> executeDirections(List<Stack<Character>> stacks, List<String> directions, boolean moveTogether) {
        // Read directions
        for (String direction : directions) {
            String[] parameters = direction.split(" ");
            int amount = Integer.parseInt(parameters[1]);
            int stackFrom = Integer.parseInt(parameters[3]) - 1;
            int stackTo = Integer.parseInt(parameters[5]) - 1;

            if (moveTogether) {
                stacks = moveCratesTogether(stacks, amount, stackFrom, stackTo);
            } else {
                stacks = moveCratesSeparately(stacks, amount, stackFrom, stackTo);
            }
        }

        return stacks;
    }

    private static String getTopCrates(List<Stack<Character>> stacks) {
        String topCrates = "";

        for (Stack<Character> stack : stacks) {
            if (!stack.isEmpty()) {
                topCrates += stack.peek();
            }
        }

        return topCrates;
    }
}
