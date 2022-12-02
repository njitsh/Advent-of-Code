package Days;
import java.util.*;

public class Day2 extends Day {
    // A is Rock 1
    // B is Paper 2
    // C is Scissors 3

    // A beats C
    // B beats A
    // C beats B

    public static void main(String[] args) {
        System.out.println(part1());
        System.out.println(part2());
    }

    public List<String> getInput(String location) {
        return super.getInput(location);
    }

    public static String part1() {
        List<String> input = new Day2().getInput("Days/input_day2.txt");
        int score = 0;

        for (String line : input) {
            score += calculateRoundScore(line.substring(0, 1), mapCharacters(line.substring(2, 3)));
            score += calculateLetterScore(mapCharacters(line.substring(2, 3)));
        }

        return Integer.toString(score);
    }

    // X means lose
    // Y means draw
    // Z means win
    public static String part2() {
        List<String> input = new Day2().getInput("Days/input_day2.txt");
        int score = 0;

        for (String line : input) {
            String response = findCharacter(line.substring(2, 3), line.substring(0, 1));
            score += calculateRoundScore(line.substring(0, 1), response);
            score += calculateLetterScore(response);
        }

        return Integer.toString(score);
    }

    private static int calculateRoundScore(String opponent, String player) {
        if (isWin(opponent, player)) {
            return 6;
        } else if (opponent.equals(player)) {
            return 3;
        }
        return 0;
    }

    private static int calculateLetterScore(String letter) {
        switch (letter) {
            case "A":
                return 1;
            case "B":
                return 2;
            case "C":
                return 3;
            default:
                throw new RuntimeException("Invalid letter");
        }
    }

    private static Boolean isWin(String opponent, String player) {
        if (opponent.equals("C") && player.equals("A")) {
            return true;
        } else if (opponent.equals("A") && player.equals("B")) {
            return true;
        } else if (opponent.equals("B") && player.equals("C")) {
            return true;
        }
        return false;
    } 

    private static String mapCharacters(String character) {
        switch (character) {
            case "X":
                return "A";
            case "Y":
                return "B";
            case "Z":
                return "C";
            default:
                return character;
        }
    }

    private static String findCharacter(String toDo, String opponent) {
        switch (toDo) {
            case "X": // Lose
                return findLosingCharacter(opponent);
            case "Y": // Draw
                return opponent;
            case "Z": // Win
                return findWinningCharacter(opponent);
            default:
                throw new RuntimeException("Invalid character");
        }
    }

    private static String findLosingCharacter (String opponent) {
        switch (opponent) {
            case "A":
                return "C";
            case "B":
                return "A";
            case "C":
                return "B";
            default:
                throw new RuntimeException("Invalid character");
        }
    }

    private static String findWinningCharacter(String opponent) {
        switch (opponent) {
            case "C":
                return "A";
            case "A":
                return "B";
            case "B":
                return "C";
            default:
                throw new RuntimeException("Invalid character");
        }
    }
}
