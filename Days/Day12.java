package Days;
import java.util.*;

public class Day12 extends Day {
    private static int[] S = new int[2];
    private static int[] E = new int[2];

    private static boolean printOutput = false;
    private static boolean recursive = false;

    private static int numberOfCalculations = 0;

    public static void main(String[] args) {
        printOutput = true;
        System.out.println(new Day12().part1());
        System.out.println(new Day12().part2());
    }

    public List<String> getInput(String location) {
        return super.getInput(location);
    }

    public String part1() {
        List<String> input = getInput("Days/input_day12.txt");

        char[][] grid = convertStringToGrid(input);
        int[][] distances = setStartingDistances(grid, S);
        int[][][] previous = new int[grid.length][grid[0].length][2];

        Dijkstra dijkstra = new Dijkstra(distances, previous, new Path(new ArrayList<>(), "S"));

        if (recursive) dijkstra = dijkstraRecursive(grid, dijkstra, S, false);
        else dijkstra = dijkstra(grid, dijkstra, S, false);

        distances = dijkstra.distances;
        previous = dijkstra.previous;
        Path path = getPath(previous, S, E, grid);

        if (printOutput) printDistances(distances, grid, null, path);
        int distanceToE = distances[E[0]][E[1]];

        if (printOutput) System.out.println("Distance to E: " + distanceToE);
        if (printOutput) System.out.println("Path to E: " + path);
        if (printOutput) System.out.println("Calculations: " + numberOfCalculations);

        return Integer.toString(distanceToE);
    }

    public String part2() {
        List<String> input = getInput("Days/input_day12.txt");

        char[][] grid = convertStringToGrid(input);
        int[][] distances = setStartingDistances(grid, E);
        int[][][] previous = new int[grid.length][grid[0].length][2];

        Dijkstra dijkstra = new Dijkstra(distances, previous, new Path(new ArrayList<>(), "E"));

        if (recursive) dijkstra = dijkstraRecursive(grid, dijkstra, E, true);
        else dijkstra = dijkstra(grid, dijkstra, E, true);

        // Get closest point a
        char pointChar = 'a';
        int[] point = getClosestPoint(grid, distances, pointChar);

        distances = dijkstra.distances;
        previous = dijkstra.previous;
        Path path = getPath(previous, E, point, grid);

        if (printOutput) printDistances(distances, grid, null, path);
        int distanceToPoint = distances[point[0]][point[1]];

        if (printOutput) System.out.println("Distance to point " + pointChar + ": " + distanceToPoint);
        if (printOutput) System.out.println("Path to E: " + path);
        if (printOutput) System.out.println("Calculations: " + numberOfCalculations);

        return Integer.toString(distanceToPoint);
    }

    private static char[][] convertStringToGrid(List<String> input) {
        char[][] grid = new char[input.size()][input.get(0).length()];
        
        for (int i = 0; i < input.size(); i++) {
            for (int j = 0; j < input.get(i).length(); j++) {
                grid[i][j] = input.get(i).charAt(j);

                if (grid[i][j] == 'S') {
                    S[0] = i;
                    S[1] = j;
                    grid[i][j] = 'a';
                } else if (grid[i][j] == 'E') {
                    E[0] = i;
                    E[1] = j;
                    grid[i][j] = 'z';
                }
            }
        }

        return grid;
    }

    private static int[][] setStartingDistances(char[][] grid, int[] pos) {
        int[][] distances = new int[grid.length][grid[0].length];

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                distances[i][j] = Integer.MAX_VALUE;
            }
        }

        distances[pos[0]][pos[1]] = 0;

        return distances;
    }

    private static Dijkstra dijkstraRecursive(char[][] grid, Dijkstra dijkstra, int[] pos, boolean inverseDirection) {
        int[][] nextMove = new int[][] {{pos[0] - 1, pos[1]}, {pos[0], pos[1] + 1}, {pos[0] + 1, pos[1]}, {pos[0], pos[1] - 1}};

        for (int[] move : nextMove) {
            if (canMove(grid, pos, move, inverseDirection) && dijkstra.distances[move[0]][move[1]] > dijkstra.distances[pos[0]][pos[1]] + 1) {
                dijkstra.distances[move[0]][move[1]] = dijkstra.distances[pos[0]][pos[1]] + 1;

                // Previous point
                dijkstra.previous[move[0]][move[1]] = pos;

                dijkstra.path.addToPath(move, Character.toString(grid[move[0]][move[1]]));
                if (printOutput && numberOfCalculations % 10000 == 0) {
                    printDistances(dijkstra.distances, grid, move, dijkstra.path);
                }
                numberOfCalculations++;
                dijkstra = dijkstraRecursive(grid, dijkstra, move, inverseDirection);
                dijkstra.path.removeFromPath();
            }
        }

        return dijkstra;
    }

    // If A* is used, this can be added to the priority queue
    private static int distanceBetweenPoints(int[] a, int[] b) {
        return (int) Math.round(Math.pow(a[0] - b[0], 2) + Math.pow(a[1] - b[1], 2));
    }

    private static Dijkstra dijkstra(char[][] grid, Dijkstra dijkstra, int[] startPos, boolean inverseDirection) {
        // Priority queue of points to visit
        PriorityQueue<int[]> pq = new PriorityQueue<int[]>(new Comparator<int[]>() {
            @Override
            public int compare(int[] a, int[] b) {
                return dijkstra.distances[a[0]][a[1]] - dijkstra.distances[b[0]][b[1]];
            }
        });
        
        // Add starting point to priority queue
        pq.add(startPos);

        while (!pq.isEmpty()) {
            int[] pos = pq.poll();
            int[][] neighbours = new int[][] {{pos[0] - 1, pos[1]}, {pos[0], pos[1] + 1}, {pos[0] + 1, pos[1]}, {pos[0], pos[1] - 1}};
            
            for (int[] neighbour : neighbours) {
                if (canMove(grid, pos, neighbour, inverseDirection) && dijkstra.distances[neighbour[0]][neighbour[1]] > dijkstra.distances[pos[0]][pos[1]] + 1) {
                    dijkstra.distances[neighbour[0]][neighbour[1]] = dijkstra.distances[pos[0]][pos[1]] + 1;

                    // Previous point
                    dijkstra.previous[neighbour[0]][neighbour[1]] = pos;

                    // Add neighbour to priority queue
                    pq.add(neighbour);

                    if (printOutput && numberOfCalculations % 10 == 0) {
                        printDistances(dijkstra.distances, grid, neighbour, null);
                    }
                    numberOfCalculations++;
                    //if (printOutput) printDistances(dijkstra.distances, grid, neighbour, null);
                }
            }
        }

        return dijkstra;
    }

    private static boolean canMove(char[][] grid, int[] from, int[] to, boolean inverseDirection) {
        return !inverseDirection && (to[0] >= 0 && to[0] < grid.length) && (to[1] >= 0 && to[1] < grid[0].length)
                && grid[to[0]][to[1]] - grid[from[0]][from[1]] <= 1 ||
                inverseDirection && (to[0] >= 0 && to[0] < grid.length) && (to[1] >= 0 && to[1] < grid[0].length)
                && grid[from[0]][from[1]] - grid[to[0]][to[1]] <= 1;
    }

    private Path getPath(int[][][] previous, int[] startPos, int[] endPos, char[][] grid) {
        String path = "";
        int[] current = new int[] {endPos[0], endPos[1]};

        Stack<int[]> pointPath = new Stack<int[]>();

        while (current[0] != startPos[0] || current[1] != startPos[1]) {
            pointPath.push(current);
            path = grid[current[0]][current[1]] + path;
            current = new int[] {previous[current[0]][current[1]][0], previous[current[0]][current[1]][1]};
        }

        List<int[]> pathList = new ArrayList<int[]>();
        while (!pointPath.isEmpty()) {
            pathList.add(pointPath.pop());
        }

        return new Path(pathList, path);
    }

    private int[] getClosestPoint(char[][] grid, int[][] distances, char c) {
        int[] closestPoint = null;
        
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j] == c && (closestPoint == null || distances[i][j] < distances[closestPoint[0]][closestPoint[1]])) {
                    closestPoint = new int[] {i, j};
                }
            }
        }

        return closestPoint;
    }

    private static void printDistances(int[][] distances, char[][] grid, int[] pos, Path path) {
        // Clear console
        System.out.print("\033[H\033[2J");

        String printGrid = "";
        for (int i = 0; i < distances.length; i++) {
            String line = "";
            for (int j = 0; j < distances[i].length; j++) {
                line += getChar(i, j, distances, pos, grid, true, path);
            }
            line += "\n";
            printGrid += line;
        }

        printGrid += "\n";

        if (pos != null) printGrid += "Current position: " + pos[0] + ", " + pos[1] + " Current distance: " + distances[pos[0]][pos[1]] + " Print: " + getChar(pos[0], pos[1], distances, pos, grid, false, null) + "\n";

        System.out.print(printGrid);

        // Wait for 0.02 second
        try {
            Thread.sleep(20);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static String getChar(int i, int j, int[][] distances, int[] pos, char[][] grid, boolean currentRed, Path path) {
        // if S or E, return S or E in RED
        if (S[0] == i && S[1] == j) {
            return "\033[0;31mS\033[0m";
        } else if (E[0] == i && E[1] == j) {
            return "\033[0;31mE\033[0m";
        } else if (distances[i][j] == Integer.MAX_VALUE) { // if current position has max value, print char
            return String.valueOf(grid[i][j]);
        } else if (path != null && path.contains(new int[] {i, j})) { // if current position is in path, print char in RED
            return "\033[0;31mX\033[0m";
        }

        // array of possible colours to use
        String[] colours = {"\033[0;34m", "\033[0;32m", "\033[0;33m", "\033[0;35m", "\033[0;36m", "\033[0;37m", "\033[0;30m", "\033[0;92m", "\033[0;93m", "\033[0;94m", "\033[0;95m", "\033[0;96m", "\033[0;97m", "\033[0;98m", "\033[0;99m",
                "\033[0;100m", "\033[0;101m", "\033[0;102m", "\033[0;103m", "\033[0;104m", "\033[0;105m", "\033[0;106m", "\033[0;107m",
                "\033[0;108m", "\033[0;109m", "\033[0;110m", "\033[0;111m", "\033[0;112m", "\033[0;113m", "\033[0;114m", "\033[0;115m"};
            
        // print distance with only last digit, different color based on first digit from colours array
        // if current position, print in red
        if (currentRed && pos != null && i == pos[0] && j == pos[1]) { // RED
            return "\033[0;31m" + distances[i][j] % 10 + "\033[0m";
        } else if (distances[i][j] < 10) {
            return colours[0] + distances[i][j] + "\033[0m";
        } else if (distances[i][j] < 100) {
            return colours[distances[i][j] / 10] + distances[i][j] % 10 + "\033[0m";
        }
        return colours[distances[i][j] / 10 % 10] + distances[i][j] % 100 % 10 + "\033[0m";
    }
}

class Dijkstra {
    public int[][] distances;
    public int[][][] previous;
    public Path path;

    Dijkstra(int[][] distances, int[][][] previous, Path path) {
        this.distances = distances;
        this.previous = previous;
        this.path = path;
    }
}

class Path {
    public List<int[]> path;
    public String pathString;
    
    Path(List<int[]> path, String pathString) {
        this.path = path;
        this.pathString = pathString;
    }

    public boolean contains(int[] pos) {
        for (int[] p : this.path) {
            if (p[0] == pos[0] && p[1] == pos[1]) return true;
        }
        return false;
    }

    public void addToPath(int[] pos, String character) {
        this.path.add(pos);
        this.pathString += character;
    }

    public void removeFromPath() {
        this.path.remove(this.path.size() - 1);
        this.pathString = this.pathString.substring(0, this.pathString.length() - 1);
    }

    public String toString() {
        return this.pathString;
    }
}
