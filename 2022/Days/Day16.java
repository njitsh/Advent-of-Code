package Days;
import java.util.*;

public class Day16 extends Day {
    private static boolean printOutput = false;
    public static void main(String[] args) {
        printOutput = true;
        System.out.println(new Day16().part1());
        System.out.println(new Day16().part2());
    }

    public List<String> getInput(String location) {
        return super.getInput(location);
    }

    public String part1() {
        List<String> input = getInput("2022/Days/input_day16.txt");

        // List of valves
        List<Valve> valves = parseInput(input);

        // Get AA valve
        Valve startValve = getFirstValve(valves);

        // State space
        // Keys: current valve, minutesLeft, turnedOnValves
        int[][][] stateSpace = new int[valves.size()][31][valves.size() * valves.size()];
        // Initialize state space
        for (int i = 0; i < valves.size(); i++) {
            for (int j = 0; j < 31; j++) {
                for (int k = 0; k < valves.size(); k++) {
                    stateSpace[i][j][k] = -1;
                }
            }
        }

        if (printOutput) {
            // Print neighbours of valves
            for (Valve valve : valves) {
                valve.printNeighbours();
            }
        }

        List<Valve> valvesWithFlow = new ArrayList<Valve>();
        List<Valve> valvesWithoutFlow = new ArrayList<Valve>();

        // Remove valves with 0 flow rate and replace it in neighbours
        for (Valve valve : valves) {
            if (valve.getFlowRate() == 0) {
                valve.replace();
                if (!valve.getName().equals("AA")) valvesWithoutFlow.add(valve);
                else {
                    // Remove neighbours with 0 flow rate from "AA"
                    Set<Valve> neighboursToRemove = new HashSet<Valve>();
                    for (Valve neighbour : valve.getNeighbours().keySet()) {
                        if (neighbour.getFlowRate() == 0) neighboursToRemove.add(neighbour);
                    }
                    valve.removeNeighbours(neighboursToRemove);
                }
            } else {
                valvesWithFlow.add(valve);
            }
        }

        // Remove valves with 0 flow rate
        valves.removeAll(valvesWithoutFlow);

        // Calculate shortest distance between every two valves
        for (Valve valve : valves) {
            valve.calculateShortestDistance(valvesWithFlow);
        }

        if (printOutput) System.out.println("After removing 0 flow rate valves");

        if (printOutput) {
            // Print neighbours of valves
            for (Valve valve : valves) {
                if (valve.getFlowRate() == 0 && !valve.getName().equals("AA")) continue;
                valve.printNeighbours();
            }
        }

        // Remove "AA" from neighbours of valves
        for (Valve valve : valves) {
            valve.removeNeighbour(startValve);
        }

        int releasedPressure = releasePressure(startValve, 30, valves, new ArrayList<Valve>(), stateSpace, null);
        
        return Integer.toString(releasedPressure);
    }

    public String part2() {
        return "Day 16, Part 2";
    }

    private static List<Valve> parseInput(List<String> input) {
        Map<String, Valve> valves = new HashMap<String, Valve>();

        for (String line : input) {
            String[] parts = line.split(" ");

            String name = parts[1];
            int flowRate = Integer.parseInt(parts[4].substring(5, parts[4].length() - 1));
            List<String> neighbourNames = new ArrayList<>();

            for (int i = 9; i < parts.length; i++) {
                String neighbourName = parts[i];
                if (i < parts.length - 1) neighbourName = neighbourName.substring(0, neighbourName.length() - 1);

                neighbourNames.add(neighbourName);
            }

            Valve valve = new Valve(name, flowRate, neighbourNames);

            valves.put(name, valve);
        }

        for (Valve valve : valves.values()) {
            HashMap<Valve, Integer> neighbours = new HashMap<>();
            
            for (String neighbourName : valve.getNeighbourNames()) {
                Valve neighbour = valves.get(neighbourName);
                neighbours.put(neighbour, 1);
            }

            valve.setNeighbours(neighbours);
        }

        // Convert dictionary to list based on values
        return new ArrayList<Valve>(valves.values());
    }

    private static Valve getFirstValve(List<Valve> valves) {
        for (Valve valve : valves) {
            if (valve.getName().equals("AA")) return valve;
        }

        return null;
    }

    public static int openValvesEncoded(List<Valve> valves) {
        int encoded = 0;

        for (Valve valve : valves) {
            if (valve.valveIsOpen()) encoded += Math.pow(2, valve.getIndex());
        }

        return encoded;
    }

    private static int releasePressure(Valve valve, int minutesLeft, List<Valve> valves, List<Valve> openValves, int[][][] stateSpace, Valve previous) {
        int releasedPressure = 0;
        
        if (minutesLeft < 1) {
            return 0;
        }

        // if (previous != null && stateSpace[previous.getIndex()][minutesLeft][openValvesEncoded(valves)] != -1) {
        //     return stateSpace[previous.getIndex()][minutesLeft][openValvesEncoded(valves)];
        // }

        for (Valve neighbour : valve.getNeighbours().keySet()) {
            int cost = valve.getNeighbours().get(neighbour) + 1;
            int timeLeft = minutesLeft - cost;
            if (neighbour.valveIsOpen() || timeLeft < 1) continue;

            // // Move to neighbour without opening valve
            // int output = releasePressure(neighbour, minutesLeft - valve.getNeighbours().get(neighbour), valves, openValves, stateSpace);
            // if (output > releasedPressure) {
            //     releasedPressure = output;
            // }

            // if (neighbour.valveIsOpen()) continue;

            // Move to neighbour and open valve
            neighbour.openValve();
            openValves.add(neighbour);
            int output = releasePressure(neighbour, timeLeft, valves, openValves, stateSpace, valve);
            output += neighbour.getFlowRate() * (timeLeft + 1);
            if (output > releasedPressure) {
                releasedPressure = output;
            }
            openValves.remove(neighbour);
            neighbour.closeValve();
        }

        // stateSpace[valve.getIndex()][minutesLeft][openValvesEncoded(valves)] = releasedPressure;

        return releasedPressure;
    }
}

class Valve {
    private String name;
    private int flowRate;
    private HashMap<Valve, Integer> neighbours;
    private List<String> neighbourNames;
    private boolean valveIsOpen = false;
    private int index;

    public Valve(String name, int flowRate, List<String> neighbours) {
        this.name = name;
        this.flowRate = flowRate;
        this.neighbourNames = neighbours;
    }

    public String getName() {
        return name;
    }

    public int getFlowRate() {
        return flowRate;
    }

    public int getIndex() {
        return index;
    }
    
    public HashMap<Valve, Integer> getNeighbours() {
        return neighbours;
    }

    public List<String> getNeighbourNames() {
        return neighbourNames;
    }

    public void printNeighbours() {
        String neighboursOutput = "";

        for (Valve neighbour : this.neighbours.keySet()) {
            neighboursOutput += neighbour.getName() + " " + this.neighbours.get(neighbour) + " ";
        }

        for (Valve neighbour : neighbours.keySet()) {
            System.out.println(name + " " + getFlowRate() + " -> " + neighbour.getName() + " Distance: " + neighbours.get(neighbour) + " Neighbours: " + neighboursOutput);
        }
    }
    
    public void setNeighbours(HashMap<Valve, Integer> neighbours) {
        this.neighbours = neighbours;
    }

    public void addNeighbour(Valve neighbour, int distance) {
        if (this != neighbour && (!this.neighbours.containsKey(neighbour) || this.neighbours.get(neighbour) > distance)) {
            this.neighbours.put(neighbour, distance);
        }
    }

    public void addNeighbours(Set<Valve> newNeighbours, Valve previousValve) {
        // If neighbour is not already in neighbours with a lower distance, add it
        for (Valve neighbour : newNeighbours) {
            int newDistance = previousValve.neighbours.get(this) + previousValve.neighbours.get(neighbour);
            if (this != neighbour && (!this.neighbours.containsKey(neighbour) || this.neighbours.get(neighbour) > newDistance)) {
                this.neighbours.put(neighbour, newDistance);
            }
        }
    }

    public void removeNeighbour(Valve neighbour) {
        this.neighbours.remove(neighbour);
    }

    public void removeNeighbours(Set<Valve> neighbours) {
        for (Valve neighbour : neighbours) {
            this.neighbours.remove(neighbour);
        }
    }

    public void openValve() {
        this.valveIsOpen = true;
    }

    public void closeValve() {
        this.valveIsOpen = false;
    }

    public boolean valveIsOpen() {
        return valveIsOpen;
    }

    public void replace() {
        for (Valve neighbour : neighbours.keySet()) {
            neighbour.addNeighbours(neighbours.keySet(), this);
            neighbour.removeNeighbour(this);
        }
    }

    public void calculateShortestDistance(List<Valve> valves) {
        // Calculate shortest distance to all other valves using BFS and add to neighbours

        // Create distance map
        HashMap<Valve, Integer> distance = new HashMap<Valve, Integer>();

        // Set all distances to infinity
        for (Valve valve : valves) {
            distance.put(valve, Integer.MAX_VALUE);
        }

        // Add all neighbours to distance map
        for (Valve neighbour : neighbours.keySet()) {
            distance.put(neighbour, neighbours.get(neighbour));
        }

        // Add self to distance map
        distance.put(this, 0);

        // Create priority queue for Dijkstra's algorithm
        PriorityQueue<Valve> queue = new PriorityQueue<Valve>(new Comparator<Valve>() {
            @Override
            public int compare(Valve v1, Valve v2) {
                return distance.get(v1) - distance.get(v2);
            }
        });

        // Add all neighbours to queue
        for (Valve neighbour : neighbours.keySet()) {
            queue.add(neighbour);
        }
        
        // Create visited set
        Set<Valve> visited = new HashSet<Valve>();

        // Add all neighbours to visited
        for (Valve neighbour : neighbours.keySet()) {
            visited.add(neighbour);
        }

        // Add self to visited
        visited.add(this);

        // While queue is not empty
        while (!queue.isEmpty()) {
            // Get next valve
            Valve valve = queue.poll();

            // Set distance to valve
            valve.addNeighbour(this, distance.get(valve));

            // For each neighbour of valve
            for (Valve neighbour : valve.getNeighbours().keySet()) {
                // If neighbour is not in visited
                if (!visited.contains(neighbour)) {
                    // Add neighbour to visited
                    visited.add(neighbour);

                    // Add neighbour to queue
                    queue.add(neighbour);

                    // Add neighbour to distance map
                    distance.put(neighbour, distance.get(valve) + valve.getNeighbours().get(neighbour));
                }
            }
        }

        // Remove self from distance map
        distance.remove(this);

        // Set new distances for this valve
        this.setNeighbours(distance);
    }
}

class bestOutput {
    private int pressure;
    private List<Valve> valves;

    public bestOutput(int pressure, List<Valve> valves) {
        // Make deep copy of valves
        this.pressure = pressure;
        this.valves = new ArrayList<Valve>();
    }

    public int getPressure() {
        return pressure;
    }

    public void setPressure(int pressure) {
        this.pressure = pressure;
    }

    public List<Valve> getValves() {
        return valves;
    }

    public List<Valve> getOpenValves() {
        List<Valve> openValves = new ArrayList<Valve>();

        for (Valve valve : valves) {
            if (valve.valveIsOpen()) openValves.add(valve);
        }

        return openValves;
    }
}
