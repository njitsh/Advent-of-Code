package Days;
import java.util.*;

public class Day4 extends Day {
    public static void main(String[] args) {
        System.out.println(new Day4().part1());
        System.out.println(new Day4().part2());
    }

    public List<String> getInput(String location) {
        return super.getInput(location);
    }

    public String part1() {
        List <String> input = getInput("2022/Days/input_day4.txt");

        int fullOverlapPairs = 0;

        for (String line : input) {
            String[] splitLine = line.split(",");
            String[] elf1 = splitLine[0].split("-");
            String[] elf2 = splitLine[1].split("-");

            if (checkIfFullyContains(elf1, elf2)) {
                fullOverlapPairs++;
            }
        }
        
        return Integer.toString(fullOverlapPairs);
    }

    public String part2() {
        List <String> input = getInput("2022/Days/input_day4.txt");

        int overlapPairs = 0;

        for (String line : input) {
            String[] splitLine = line.split(",");
            String[] elf1 = splitLine[0].split("-");
            String[] elf2 = splitLine[1].split("-");

            if (pairOverlaps(elf1, elf2)) {
                overlapPairs++;
            }
        }

        return Integer.toString(overlapPairs);
    }

    private static Boolean checkIfFullyContains(String[] elf1, String[] elf2) {
        int elf1_start = Integer.parseInt(elf1[0]);
        int elf1_end = Integer.parseInt(elf1[1]);
        int elf2_start = Integer.parseInt(elf2[0]);
        int elf2_end = Integer.parseInt(elf2[1]);

        if (elf1_start <= elf2_start && elf1_end >= elf2_end) return true;
        if (elf2_start <= elf1_start && elf2_end >= elf1_end) return true;
        return false;
    }

    private static Boolean pairOverlaps(String[] elf1, String[] elf2) {
        int elf1_start = Integer.parseInt(elf1[0]);
        int elf1_end = Integer.parseInt(elf1[1]);
        int elf2_start = Integer.parseInt(elf2[0]);
        int elf2_end = Integer.parseInt(elf2[1]);
        
        if (elf1_end < elf2_start || elf2_end < elf1_start) return false;
        return true;
    }
}
