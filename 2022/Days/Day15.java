package Days;
import java.util.*;

public class Day15 extends Day {
    public static void main(String[] args) {
        System.out.println(new Day15().part1());
        System.out.println(new Day15().part2());
    }

    public List<String> getInput(String location) {
        return super.getInput(location);
    }

    public String part1() {
        List<String> input = getInput("2022/Days/input_day15.txt");

        List<Sensor> sensors = parseInput(input);

        int checkRow = 2000000;

        // Remove all sensors that do not overlap with checkRow
        List<Sensor> overlappingSensors = removeNonOverlappingSensors(sensors, checkRow);

        // Get minimum and maximum x and y coordinates
        int[] bounds = getBounds(sensors);

        // Count the number of spots covered by the sensors for checkRow
        int count = countCoveredSpots(overlappingSensors, bounds, checkRow);

        return Integer.toString(count);
    }

    public String part2() {
        List<String> input = getInput("2022/Days/input_day15.txt");

        List<Sensor> sensors = parseInput(input);

        // Get the diagonals
        List<int[]> diagonals = getDiagonals(sensors);

        // Set the minimum and maximum x and y coordinates
        int minBound = 0;
        int maxBound = 4000000;
        int[] bounds = new int[] {minBound, maxBound};

        // Find diagonal intersections
        List<int[]> intersections = findIntersections(diagonals, bounds);

        // Find the empty spot
        int[] emptySpot = findEmptySpot(sensors, intersections);
        
        long tuningFrequency = calculateTuningFrequency(emptySpot);

        return Long.toString(tuningFrequency);
    }

    private static List<Sensor> parseInput(List<String> input) {
        List<Sensor> sensors = new ArrayList<>();

        for (String line : input) {
            String[] split = line.split(" ");
            int xCoord = Integer.parseInt(split[2].substring(2, split[2].length() - 1));
            int yCoord = Integer.parseInt(split[3].substring(2, split[3].length() - 1));
            int xClosestBeacon = Integer.parseInt(split[8].substring(2, split[8].length() - 1));
            int yClosestBeacon = Integer.parseInt(split[9].substring(2, split[9].length()));

            sensors.add(new Sensor(new int[] {xCoord, yCoord}, new int[] {xClosestBeacon, yClosestBeacon}));
        }

        return sensors;
    }

    private static int[] getBounds(List<Sensor> sensors) {
        int minX = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;

        for (Sensor sensor : sensors) {
            int[] coords = new int[] {sensor.getX(), sensor.getClosestBeaconX()};
            
            for (int coord : coords) {
                if (coord < minX) {
                    minX = coord;
                }
                if (coord > maxX) {
                    maxX = coord;
                }
            }
        }

        return new int[] {minX, maxX};
    }

    private static List<Sensor> removeNonOverlappingSensors(List<Sensor> sensors, int checkRow) {
        List<Sensor> overlappingSensors = new ArrayList<>();

        for (Sensor sensor : sensors) {
            if (sensor.inRange(sensor.getX(), checkRow)) {
                overlappingSensors.add(sensor);
            }
        }

        return overlappingSensors;
    }

    private static int countCoveredSpots(List<Sensor> sensors, int[] bounds, int checkRow) {
        int count = 0;
        
        for (int i = bounds[0]; i <= bounds[1]; i++) {
            for (Sensor sensor : sensors) {
                if (sensor.inRange(i, checkRow)) {
                    // If not on top of a beacon
                    if (!sensor.onTopOfBeacon(i, checkRow)) {
                        int horizontalDistance = sensor.distanceToBeacon() - Math.abs(sensor.getY() - checkRow);
                        if (i < sensor.getX()) {
                            int stepSize = (sensor.getX() - i) + horizontalDistance;
                            i += stepSize;
                            count += stepSize + 1;
                        } else if (i > sensor.getX()) {
                            int stepSize = Math.max(horizontalDistance - (i - sensor.getX()) - 1, 1);
                            i += stepSize;
                            count += stepSize + 1;
                        } else {
                            count++;
                        }
                    }
                    break;
                }
            }
        }

        return count;
    }

    private static List<int[]> getDiagonals(List<Sensor> sensors) {
        List<int[]> diagonals = new ArrayList<>();

        for (Sensor sensor : sensors) {
            // diagonals
            // y = x + s.y - s.x + r + 1
            // y = x + s.y - s.x - r - 1
            // y = -x + s.y + s.x + r + 1
            // y = -x + s.y + s.x - r - 1

            int[] sensorDiagonals = new int[] {
                sensor.getY() - sensor.getX() + sensor.distanceToBeacon() + 1, // x
                sensor.getY() - sensor.getX() - sensor.distanceToBeacon() - 1, // x
                sensor.getY() + sensor.getX() + sensor.distanceToBeacon() + 1, // -x
                sensor.getY() + sensor.getX() - sensor.distanceToBeacon() - 1 // -x
            };

            diagonals.add(sensorDiagonals);
        }

        return diagonals;
    }

    private static List<int[]> findIntersections(List<int[]> diagonals, int[] bounds) {
        List<int[]> intersections = new ArrayList<>();

        for (int i = 0; i < diagonals.size(); i++) {
            for (int j = i + 1; j < diagonals.size(); j++) {
                int[] diagonals1 = diagonals.get(i);
                int[] diagonals2 = diagonals.get(j);
                
                for (int k = 0; k < 2; k++) {
                    for (int l = 2; l < 4; l++) {
                        int x = (diagonals2[l] - diagonals1[k]) / 2;
                        int y = (diagonals1[k] + diagonals2[l]) / 2;

                        if (x >= bounds[0] && x <= bounds[1] && y >= bounds[0] && y <= bounds[1]) {
                            intersections.add(new int[] {x, y});
                        }
                    }
                }
            }
        }

        return intersections;
    }

    private static int[] findEmptySpot(List<Sensor> sensors, List<int[]> intersections) {
        // Loop through all intersections
        for (int[] intersection : intersections) {
            boolean emptySpot = true;

            // Check if any sensor is in range of the intersection
            for (Sensor sensor : sensors) {
                if (sensor.inRange(intersection[0], intersection[1])) {
                    emptySpot = false;
                    break;
                }
            }

            if (emptySpot) return intersection;
        }
        
        throw new RuntimeException("No empty spot found");
    }

    private static long calculateTuningFrequency(int[] emptySpot) {
        return (long) emptySpot[0] * 4000000 + (long) emptySpot[1];
    }
}

class Sensor {
    private int[] coordinates;
    private int[] closestBeacon;
    private int distanceToBeacon;

    public Sensor(int[] coordinates, int[] closestBeacon) {
        this.coordinates = coordinates;
        this.closestBeacon = closestBeacon;
        this.distanceToBeacon = calculateDistanceToBeacon();
    }

    // Manhattan distance
    private int calculateDistanceToBeacon() {
        return Math.abs(coordinates[0] - closestBeacon[0]) + Math.abs(coordinates[1] - closestBeacon[1]);
    }

    public int distanceToBeacon() {
        return distanceToBeacon;
    }

    public boolean inRange(int x, int y) {
        return Math.abs(coordinates[0] - x) + Math.abs(coordinates[1] - y) <= distanceToBeacon;
    }

    public int getX() {
        return coordinates[0];
    }

    public int getY() {
        return coordinates[1];
    }

    public int getClosestBeaconX() {
        return closestBeacon[0];
    }

    public boolean onTopOfBeacon(int x, int y) {
        return x == closestBeacon[0] && y == closestBeacon[1];
    }
}
