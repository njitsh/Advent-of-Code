package Days;
import java.util.*;

public class Day13 extends Day {
    public static boolean printOutput = false;

    public static void main(String[] args) {
        printOutput = true;
        System.out.println(new Day13().part1());
        System.out.println(new Day13().part2());
    }

    public List<String> getInput(String location) {
        return super.getInput(location);
    }

    public String part1() {
        List<String> input = getInput("Days/input_day13.txt");

        int sumOfRightIndices = 0;
        int index = 1;

        for (int i = 0; i < input.size(); i += 3) {
            String packet_1 = input.get(i);
            String packet_2 = input.get(i + 1);

            NumberList packet_1_parsed = packetParser(packet_1);
            NumberList packet_2_parsed = packetParser(packet_2);

            boolean inRightOrder = packet_1_parsed.checkOrder(packet_2_parsed);
            
            sumOfRightIndices += inRightOrder ? index : 0;

            if (printOutput) System.out.println("In right order: " + inRightOrder + "\n");
            if (printOutput) System.out.println("Sum of right indices: " + sumOfRightIndices + "\n");
            
            index++;
        }

        return Integer.toString(sumOfRightIndices);
    }

    public String part2() {
        List<String> input = getInput("Days/input_day13.txt");

        // Remove empty lines
        input.removeIf(String::isEmpty);

        // Add new input [[2]] and [[6]]
        input.add("[[2]]");
        input.add("[[6]]");

        List<NumberList> packets = new ArrayList<NumberList>();

        // Convert input to packets and add to list
        for (String packet : input) {
            packets.add(packetParser(packet));
        }

        // Sort packets
        packets.sort((a, b) -> a.checkOrderInt(b));

        // Find [[2]] and [[6]]
        int index_2 = 0;
        int index_6 = 0;

        for (NumberList packet : packets) {
            if (packet.toString().equals("[[2]]")) index_2 = packets.indexOf(packet) + 1;
            if (packet.toString().equals("[[6]]")) index_6 = packets.indexOf(packet) + 1;
        }

        int result = index_2 * index_6;

        return Integer.toString(result);
    }

    private NumberList packetParser(String packet) {
        NumberList numberList = new NumberList();
        String packetContents = packet.substring(1, packet.length() - 1);

        if (packetContents.length() == 0) {
            return numberList;
        }

        String[] packetContentsSplit = packetSplitter(packetContents);

        for (String packetContent : packetContentsSplit) {
            if (packetContent.charAt(0) == '[') {
                numberList.addChild(packetParser(packetContent));
            } else {
                numberList.addChild(new NumberList(Integer.parseInt(packetContent)));
            }
        }

        return numberList;
    }

    private String[] packetSplitter(String packet) {
        int depth = 0;
        int subPacketStart = 0;

        List<String> subPackets = new ArrayList<>();

        for (int i = 0; i < packet.length(); i++) {
            if (i == packet.length() - 1) {
                subPackets.add(packet.substring(subPacketStart, i + 1));
            } else if (packet.charAt(i) == '[') {
                depth++;
            } else if (packet.charAt(i) == ']') {
                depth--;
            } else if (packet.charAt(i) == ',' && depth == 0) {
                subPackets.add(packet.substring(subPacketStart, i));
                subPacketStart = i + 1;
            }
        }

        String[] splitPackets = new String[subPackets.size()];

        for (int i = 0; i < subPackets.size(); i++) {
            splitPackets[i] = subPackets.get(i);
        }

        return splitPackets;
    }
}

class NumberList {
    private List<NumberList> numberList;
    private int number;

    public NumberList() {
        this.number = -1;
        this.numberList = new ArrayList<>();
    }

    public NumberList(int number) {
        this.number = number;
        this.numberList = new ArrayList<>();
    }

    public NumberList(List<NumberList> numberList) {
        this.numberList = numberList;
        this.number = -1;
    }

    // DeepCopy
    public NumberList(NumberList numberList) {
        this.number = numberList.number;
        this.numberList = new ArrayList<>();
        for (NumberList numberListChild : numberList.numberList) {
            this.numberList.add(new NumberList(numberListChild));
        }
    }

    private boolean isNumber() {
        return number != -1;
    }

    public void addChild(NumberList numberList) {
        this.numberList.add(numberList);
    }

    public String toString() {
        if (isNumber()) {
            return Integer.toString(number);
        } else {
            String output = "[";
            if (numberList != null) {
                for (int i = 0; i < numberList.size(); i++) {
                    output += numberList.get(i).toString();
                    if (i != numberList.size() - 1) {
                        output += ", ";
                    }
                }
            }
            output += "]";
            return output;
        }
    }

    public void convertToNumberList() {
        this.addChild(new NumberList(number));
        this.number = -1;
    }

    // True if this is smaller than second
    public boolean checkOrder(NumberList second) {
        Stack <NumberList[]> evaluateStack = new Stack<>();

        evaluateStack.add(new NumberList[] {this, second});

        while (!evaluateStack.isEmpty()) {
            NumberList[] current = evaluateStack.pop();

            if (current[0] == null) {
                if (Day13.printOutput) System.out.println("Left side ran out of items, so inputs are in the right order");
                return true;
            } else if (current[1] == null) {
                if (Day13.printOutput) System.out.println("Right side ran out of items, so inputs are not in the right order");
                return false;
            }

            NumberList left = new NumberList(current[0]);
            NumberList right = new NumberList(current[1]);

            if (Day13.printOutput) System.out.println("Compare " + left + " vs " + right);

            // If both are numbers
            if (left.isNumber() && right.isNumber()) {
                if (strictlySmaller(left.number, right.number)) return true;
                if (strictlyLarger(left.number, right.number)) return false;
            }

            // If only one is a number
            // Convert to list
            if (left.isNumber() && !right.isNumber()) {
                left.convertToNumberList();
            } else if (!left.isNumber() && right.isNumber()) {
                right.convertToNumberList();
            }

            // Add all children to queue
            for (int i = Math.max(left.numberList.size(), right.numberList.size()) - 1; i >= 0; i--) {
                NumberList leftChild = i < left.numberList.size() ? left.numberList.get(i) : null;
                NumberList rightChild = i < right.numberList.size() ? right.numberList.get(i) : null;
                evaluateStack.add(new NumberList[] {leftChild, rightChild});
            }
        }

        return true;
    }

    public int checkOrderInt(NumberList second) {
        return checkOrder(second) ? -1 : 1;
    }

    public boolean strictlySmaller(int numberOne, int numberTwo) {
        return numberOne < numberTwo;
    }

    public boolean strictlyLarger(int numberOne, int numberTwo) {
        return numberOne > numberTwo;
    }
}