package Days;
import java.util.*;

public class Day10 extends Day {
    public static void main(String[] args) {
        System.out.println(new Day10().part1());
        System.out.println(new Day10().part2());
    }

    public List<String> getInput(String location) {
        return super.getInput(location);
    }

    public String part1() {
        List<String> input = getInput("Days/input_day10.txt");

        int x = 1;
        int cycles = 0;
        int i = 0;

        String instruction = input.get(i);

        int signalStrength = 0;

        while (i < input.size()) {
            if (instruction.substring(0,4).equals("addx")) {
                int newX = x + Integer.parseInt(instruction.substring(5));
                instruction = "setx " + newX;
            } else if (instruction.substring(0,4).equals("setx")) {
                x = Integer.parseInt(instruction.substring(5));
                instruction = "noop";
            }

            cycles++;

            if ((cycles - 20) % 40 == 0) {
                signalStrength += cycles * x;
            }

            if (!instruction.substring(0,4).equals("setx")) {
                i++;
                if (i < input.size()) instruction = input.get(i);
            }
        }

        return Integer.toString(signalStrength);
    }

    public String part2() {
        List<String> input = getInput("Days/input_day10.txt");

        int x = 1;
        int cycles = 0;
        int i = 0;

        String instruction = input.get(i);

        String crt = "\n";

        while (i < input.size()) {
            if (instruction.substring(0,4).equals("addx")) {
                int newX = x + Integer.parseInt(instruction.substring(5));
                instruction = "setx " + newX;
            } else if (instruction.substring(0,4).equals("setx")) {
                x = Integer.parseInt(instruction.substring(5));
                instruction = "noop";
            }

            cycles++;

            if (!instruction.substring(0,4).equals("setx")) {
                i++;
                if (i < input.size()) instruction = input.get(i);
            }

            if (Math.abs((cycles % 40) - x) <= 1) crt += "X";
            else crt += ".";

            if (cycles % 40 == 0) {
                crt += "\n";
            }
        }

        return crt;
    }
}
