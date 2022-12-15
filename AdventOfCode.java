// Main Advent of Code file
// Run code for all days, and time it

import java.util.*;
import java.time.*;

// Import all days
import Days.*;

public class AdventOfCode {
    public static void main(String[] args) {
        // Create a list of all the days
        List<Day> days = new ArrayList<>();
        days.add(new Days.Day1());
        days.add(new Days.Day2());
        days.add(new Days.Day3());
        days.add(new Days.Day4());
        days.add(new Days.Day5());
        days.add(new Days.Day6());
        days.add(new Days.Day7());
        days.add(new Days.Day8());
        days.add(new Days.Day9());
        days.add(new Days.Day10());
        days.add(new Days.Day11());
        days.add(new Days.Day12());
        days.add(new Days.Day13());
        days.add(new Days.Day14());

        int dayNumber = 0;

        Duration time_total = Duration.ZERO;

        // Run all the days
        for (Day day : days) {
            dayNumber++;

            System.out.print("Day " + dayNumber + "\n");

            Duration time_part_1 = runPartOfDay(day, 1);
            Duration time_part_2 = runPartOfDay(day, 2);

            time_total = time_total.plus(time_part_1).plus(time_part_2);

            String time_part_1_string = durationToString(time_part_1);
            String time_part_2_string = durationToString(time_part_2);
            
            System.out.println("Part 1: " + time_part_1_string + ", " + "Part 2: " + time_part_2_string + "\n");
        }

        System.out.println("Total time: " + durationToString(time_total));

        // Check if within 1 second
        if (time_total.compareTo(Duration.ofSeconds(1)) < 0) {
            System.out.println("Total time is less than 1 second!");
        }
    }

    private static Duration runPartOfDay(Day day, int part) {
        // Get the current time
        Instant start = Instant.now();
        
        // Run the part X code
        System.out.print("> Part " + part + ": " + day.part(part) + "\n");
        
        // Get the current time again
        Instant end = Instant.now();
        
        // Calculate the difference
        return Duration.between(start, end);
    }

    private static String durationToString(Duration duration) {
        return duration.toString().replace("PT", "").replace("S", "s");
    }
}