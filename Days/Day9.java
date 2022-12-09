package Days;
import java.util.*;

public class Day9 extends Day {

    public static void main(String[] args) {
        System.out.println(new Day9().part1());
        System.out.println(new Day9().part2());
    }

    public List<String> getInput(String location) {
        return super.getInput(location);
    }

    public String part1() {
        List<String> input = new Day9().getInput("Days/input_day9.txt");

        List<RopeKnot> knots = createRope(2);

        moveKnots(knots, input);

        int visitedPositions = knots.get(knots.size() - 1).getVisitedLength();

        return Integer.toString(visitedPositions);
    }

    public String part2() {
        List<String> input = new Day9().getInput("Days/input_day9.txt");

        List<RopeKnot> knots = createRope(10);

        moveKnots(knots, input);

        int visitedPositions = knots.get(knots.size() - 1).getVisitedLength();

        return Integer.toString(visitedPositions);
    }

    public static List<RopeKnot> createRope(int length) {
        List<RopeKnot> knots = new ArrayList<RopeKnot>();

        for (int i = 0; i < length; i++) {
            knots.add(new RopeKnot());

            if (i > 0) {
                knots.get(i).setPrevious(knots.get(i - 1));
                knots.get(i - 1).setNext(knots.get(i));
            }
        }

        return knots;
    }

    public void moveKnots(List<RopeKnot> knots, List<String> input) {
        for (String line : input) {
            MathVector moveDirection = getMoveVector(line.charAt(0));
            int moveDistance = Integer.parseInt(line.substring(2));
            knots.get(0).moveKnotMultipleTimes(moveDirection, moveDistance);
        }
    }

    public MathVector getMoveVector(char direction) {
        switch (direction) {
            case 'U':
                return new MathVector(new int[] {0, 1});
            case 'R':
                return new MathVector(new int[] {1, 0});
            case 'D':
                return new MathVector(new int[] {0, -1});
            case 'L':
                return new MathVector(new int[] {-1, 0});
            default:
                throw new IllegalArgumentException();
        }
    }
}

class RopeKnot {
    private MathVector pos;
    private MathVectorSet visited;

    private RopeKnot previous;
    private RopeKnot next;

    public RopeKnot() {
        this.pos = new MathVector(new int[] {0, 0});
        this.visited = new MathVectorSet();
        this.visited.add(this.pos);
        this.previous = null;
        this.next = null;
    }

    public void moveKnot(MathVector move) {
        if (this.previous != null) {
            if (this.getPos().distanceTo(this.previous.getPos()) <= 2) return;

            move = this.getPos().directionNormalized(this.previous.getPos());
        }

        this.pos = this.pos.add(move);

        if (this.previous != null && this.next == null) {
            this.visited.add(this.pos);
        }

        if (this.next != null) {
            this.next.moveKnot(new MathVector(new int[] {0, 0}));
        }
    }

    public void moveKnotMultipleTimes(MathVector move, int times) {
        for (int i = 0; i < times; i++) {
            this.moveKnot(move);
        }
    }

    public MathVector getPos() {
        return this.pos;
    }

    public int getVisitedLength() {
        return this.visited.size();
    }

    public void setPrevious(RopeKnot previous) {
        this.previous = previous;
    }

    public void setNext(RopeKnot next) {
        this.next = next;
    }
}

class MathVector {
    private int[] coordinates;

    public MathVector(int[] coordinates) {
        this.coordinates = new int[coordinates.length];

        // Deep copy
        for (int i = 0; i < coordinates.length; i++) {
            this.coordinates[i] = coordinates[i];
        }
    }

    public int getCoordinate(int index) {
        if (index < 0 || index >= this.coordinates.length) {
            throw new IndexOutOfBoundsException();
        }

        return this.coordinates[index];
    }

    public int getDimension() {
        return this.coordinates.length;
    }

    public MathVector add(MathVector v) {
        MathVector result = new MathVector(this.coordinates);

        if (result.getDimension() != v.getDimension()) {
            throw new IllegalArgumentException();
        }

        for (int i = 0; i < this.coordinates.length; i++) {
            result.coordinates[i] += v.getCoordinate(i);
        }

        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (!(o instanceof MathVector)) {
            return false;
        }

        MathVector v = (MathVector) o;

        if (this.coordinates.length != v.getDimension()) {
            return false;
        }
        
        for (int i = 0; i < this.coordinates.length; i++) {
            if (this.coordinates[i] != v.getCoordinate(i)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public String toString() {
        String result = "(";

        for (int i = 0; i < this.coordinates.length; i++) {
            result += this.coordinates[i];

            if (i < this.coordinates.length - 1) {
                result += ", ";
            }
        }

        result += ")";

        return result;
    }

    public int distanceTo(MathVector other) {
        if (this.getDimension() != other.getDimension()) {
            throw new IllegalArgumentException();
        }

        int result = 0;

        for (int i = 0; i < this.coordinates.length; i++) {
            result += Math.pow((this.coordinates[i] - other.getCoordinate(i)), 2);
        }

        return result;
    }

    public MathVector directionNormalized(MathVector other) {
        MathVector result = new MathVector(new int[] {0, 0});

        if (result.getDimension() != other.getDimension()) {
            throw new IllegalArgumentException();
        }
        
        for (int i = 0; i < this.coordinates.length; i++) {
            if (this.coordinates[i] > other.getCoordinate(i)) {
                result.coordinates[i] = -1;
            } else if (this.coordinates[i] < other.getCoordinate(i)) {
                result.coordinates[i] = 1;
            }
        }

        return result;
    }
}

class MathVectorSet {
    private List<MathVector> set;

    public MathVectorSet() {
        this.set = new ArrayList<>();
    }

    public void add(MathVector v) {
        if (!this.contains(v)) {
            this.set.add(v);
        }
    }

    public boolean contains(MathVector v) {
        for (MathVector vector : this.set) {
            if (vector.equals(v)) {
                return true;
            }
        }

        return false;
    }

    public int size() {
        return this.set.size();
    }
}