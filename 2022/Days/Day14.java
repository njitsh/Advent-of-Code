package Days;
import java.util.*;

public class Day14 extends Day {
    public static boolean printOutput = false;
    public static boolean quickCalculation = true;

    public static void main(String[] args) {
        printOutput = true;
        quickCalculation = false;
        System.out.println(new Day14().part1());
        System.out.println(new Day14().part2());
    }

    public List<String> getInput(String location) {
        return super.getInput(location);
    }

    public String part1() {
        List<String> input = getInput("2022/Days/input_day14.txt");

        // Parse input
        List<List<int[]>> parsedInput = parseInput(input);

        // Get min and max values
        List<int[]> minMax = parsedInput.remove(parsedInput.size() - 1);
        int minX = minMax.get(0)[0];
        int minY = minMax.get(0)[1];
        int maxX = minMax.get(1)[0];
        int maxY = minMax.get(1)[1];

        
        int[] sandInput = new int[] {convertToX(500, minX), 0};

        boolean floor = false;

        // Construct rock
        char[][] map = rockFormation(parsedInput, minX, minY, maxX, maxY, floor);

        // Simulate sand flow
        int restingSand = sandFlow(map, sandInput);

        return Integer.toString(restingSand);
    }

    public String part2() {
        List<String> input = getInput("2022/Days/input_day14.txt");

        // Parse input
        List<List<int[]>> parsedInput = parseInput(input);

        // Get min and max values
        List<int[]> minMax = parsedInput.remove(parsedInput.size() - 1);
        int minX = minMax.get(0)[0];
        int minY = minMax.get(0)[1];
        int maxX = minMax.get(1)[0];
        int maxY = minMax.get(1)[1] + 2;

        boolean floor = true;

        int height = maxY - minY + 2;
        int width = maxX - minX + 1;

        // Widen map
        minX = minX - height + width / 2;
        maxX = maxX + height - width / 2;
        
        int[] sandInput = new int[] {convertToX(500, minX), 0};

        // Construct rock
        char[][] map = rockFormation(parsedInput, minX, minY, maxX, maxY, floor);

        // Simulate sand flow
        int restingSand = sandFlow(map, sandInput);

        return Integer.toString(restingSand);
    }

    private List<List<int[]>> parseInput(List<String> input) {
        List<List<int[]>> parsedInput = new ArrayList<>();

        int minX = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int minY = Integer.MAX_VALUE;
        int maxY = Integer.MIN_VALUE;

        for (String line : input) {
            String[] splitLine = line.split(" -> ");

            List<int[]> parsedLine = new ArrayList<>();
            
            for (String s : splitLine) {
                String[] splitS = s.split(",");
                int x = Integer.parseInt(splitS[0]);
                int y = Integer.parseInt(splitS[1]);

                minX = Math.min(minX, x);
                maxX = Math.max(maxX, x);
                minY = 0;
                maxY = Math.max(maxY, y);

                int[] parsedS = new int[] {x, y};
                
                parsedLine.add(parsedS);
            }

            parsedInput.add(parsedLine);
        }

        // Add min and max as last line
        List<int[]> minMax = new ArrayList<>();
        minMax.add(new int[] {minX, minY});
        minMax.add(new int[] {maxX, maxY});

        parsedInput.add(minMax);

        return parsedInput;
    }

    private char[][] rockFormation(List<List<int[]>> input, int minX, int minY, int maxX, int maxY, boolean floor) {
        char[][] map = new char[maxY - minY + 1][maxX - minX + 1];

        for (List<int[]> line : input) {
            for (int i = 0; i < line.size() - 1; i++) {
                int[] point1 = line.get(i);
                int[] point2 = line.get(i + 1);

                int x1 = convertToX(point1[0], minX);
                int y1 = point1[1];
                int x2 = convertToX(point2[0], minX);
                int y2 = point2[1];

                if (x1 == x2) {
                    int toY = Math.max(y1, y2);
                    for (int y = Math.min(y1, y2); y <= toY; y++) {
                        map[y][x1] = '#';
                    }
                } else if (y1 == y2) {
                    int toX = Math.max(x1, x2);
                    for (int x = Math.min(x1, x2); x <= toX; x++) {
                        map[y1][x] = '#';
                    }
                }
            }
        }

        // For all other places, set to .
        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[0].length; x++) {
                if (map[y][x] != '#') {
                    map[y][x] = '.';
                }
            }
        }

        // Set floor to rocks
        if (floor) {
            for (int x = 0; x < map[0].length; x++) {
                map[map.length - 1][x] = '#';
            }
        }

        return map;
    }

    private int convertToX(int x, int minX) {
        return x - minX;
    }

    private void printMap(char[][] map, int[] sandInput, int[] sand) {
        String output = "\033[H\033[2J";

        int start = Math.max(0, sand[1] - 20);
        int linesLeft = 40 - (sand[1] - start);
        int end = Math.min(map.length, sand[1] + linesLeft);

        int width = Math.min(100, map[0].length);
        int startX = Math.min(Math.max(0, sandInput[0] - width / 2), sand[0]);
        int endX = Math.max(Math.min(map[0].length, sandInput[0] + width / 2), sand[0]);

        for (int y = start; y < end; y++) {
            for (int x = startX; x < endX; x++) {
                // Print sand input blue
                // Print active sand red
                // Print resting sand yellow
                // Print the air grey
                if (x == sand[0] && y == sand[1]) output += "\u001B[31m" + map[y][x] + "\u001B[0m";
                else if (x == sandInput[0] && y == sandInput[1]) output += "\u001B[34m+\u001B[0m";
                else if (map[y][x] == 'o') output += "\u001B[33m" + map[y][x] + "\u001B[0m";
                else output += "\u001B[30m" + map[y][x] + "\u001B[0m";
            }
            output += "\n";
        }

        System.out.println(output);

        // Sleep 20 ms
        try {
            Thread.sleep(20);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private int sandFlow(char[][] map, int[] sandInput) {
        int restingSand = 0;
        boolean newSand = true;
        int simulatons = 0;
        int[] sand;
        Stack<int[]> path = new Stack<int[]>();
        path.add(new int[] {sandInput[0], sandInput[1]});

        while (newSand) {
            if (quickCalculation) sand = path.pop();
            else sand = new int[] {sandInput[0], sandInput[1]};
            map[sand[1]][sand[0]] = 'o';
            restingSand += 1;
            boolean updateSand = true;

            while (updateSand) {
                simulatons += 1;
                // Check if sand can fall down
                int[][] fallDirections = new int[][] {
                    new int[] {sand[0], sand[1] + 1},
                    new int[] {sand[0] - 1, sand[1] + 1},
                    new int[] {sand[0] + 1, sand[1] + 1}
                };

                updateSand = false;

                for (int[] fallDirection : fallDirections) {
                    if (canFall(map, fallDirection)) {
                        // Fall in direction
                        map[sand[1]][sand[0]] = '.';
                        if (!inMap(map, fallDirection)) {
                            newSand = false;
                            updateSand = false;
                            restingSand -= 1;
                        } else {
                            if (quickCalculation) path.add(new int[] {sand[0], sand[1]});
                            sand = fallDirection;
                            map[sand[1]][sand[0]] = 'o';
                            updateSand = true;
                        }

                        break;
                    }
                }

                // Check if sand blocks the input
                if (sand[0] == sandInput[0] && sand[1] == sandInput[1]) {
                    newSand = false;
                }

                if (printOutput && simulatons % 1000 == 0) printMap(map, sandInput, sand);
            }
        }

        return restingSand;
    }

    private boolean inMap(char[][] map, int[] point) {
        return point[0] >= 0 && point[0] < map[0].length && point[1] >= 0 && point[1] < map.length;
    }

    private boolean canFall(char[][] map, int[] point) {
        return !inMap(map, point) || map[point[1]][point[0]] == '.';
    }
}
