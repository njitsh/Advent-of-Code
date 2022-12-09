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

        int dayNumber = 0;

        // Run all the days
        for (Day day : days) {
            dayNumber++;

            System.out.print("Day " + dayNumber + "\n");

            String time_part_1 = runPartOfDay(day, 1).toString().replace("PT", "");
            String time_part_2 = runPartOfDay(day, 2).toString().replace("PT", "");
            
            System.out.println("Part 1: " + time_part_1 + "s, " + "Part 2: " + time_part_2 + "s\n");
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
}