package Days;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day11 extends Day {
    private static boolean printOutput = false;

    public static void main(String[] args) {
        printOutput = true;
        System.out.println(new Day11().part1());
        System.out.println(new Day11().part2());
    }

    public List<String> getInput(String location) {
        return super.getInput(location);
    }

    public String part1() {
        List<String> input = getInput("2022/Days/input_day11.txt");

        List<Monkey> monkeys = createMonkeys(input, true);

        int rounds = 20;

        for (int i = 1; i <= rounds; i++) {
            for (Monkey monkey : monkeys) {
                monkey.throwItems();
            }
        }

        long[] mostInspections = monkeysMostInspections(monkeys);

        long monkey_business = calculateMonkeyBusiness(mostInspections[0], mostInspections[1]);

        return Long.toString(monkey_business);
    }

    public String part2() {
        List<String> input = getInput("2022/Days/input_day11.txt");

        List<Monkey> monkeys = createMonkeys(input, false);

        int rounds = 10000;

        for (int i = 1; i <= rounds; i++) {
            if (printOutput && i % 1000 == 0) {
                // Print monkey inspections
                for (Monkey monkey : monkeys) {
                    System.out.println(monkey.name + ": " + monkey.getInspections());
                }
                System.out.println();
            }
            for (Monkey monkey : monkeys) {
                monkey.throwItems();
            }
        }

        long[] mostInspections = monkeysMostInspections(monkeys);

        long monkey_business = calculateMonkeyBusiness(mostInspections[0], mostInspections[1]);

        return Long.toString(monkey_business);
    }

    private static List<Monkey> createMonkeys(List<String> input, boolean reliefDivide) {
        List<Monkey> monkeys = new ArrayList<Monkey>();

        int index = 0;

        while (index < input.size()) {
            int name = Integer.parseInt(findData(input.get(index), "Monkey (\\p{Digit}+):").get(0));

            String[] itemString = input.get(index + 1).substring(18).split(" ");
            List<Long> longItemList = new ArrayList<Long>();
            for (int i = 0; i < itemString.length; i++) {
                String item = itemString[i];

                if (i < itemString.length - 1) {
                    item = item.substring(0, item.length() - 1);
                }

                longItemList.add(Long.parseLong(item));
            }

            String[] functionSplit = input.get(index + 2).split(" ");
            String operator = functionSplit[functionSplit.length - 2];
            long number = 0;
            if (functionSplit[functionSplit.length - 1].equals("old")) {
                operator = "2";
            } else {
                number = Long.parseLong(functionSplit[functionSplit.length - 1]);
            }

            long divisibleBy = Long.parseLong(findData(input.get(index + 3), "Test: divisible by (\\p{Digit}+)").get(0));

            int monkeyTrue = Integer.parseInt(findData(input.get(index + 4), "    If true: throw to monkey (\\p{Digit}+)").get(0));
            int monkeyFalse = Integer.parseInt(findData(input.get(index + 5), "    If false: throw to monkey (\\p{Digit}+)").get(0));
        
            monkeys.add(new Monkey(name, longItemList, operator, number, divisibleBy, monkeyTrue, monkeyFalse, reliefDivide));

            index += 7;
        }

        for (Monkey monkey : monkeys) {
            monkey.setMonkeys(monkeys);
        }

        long productDivisibleBy = 1;
        for (Monkey monkey : monkeys) {
            productDivisibleBy *= monkey.divisibleBy;
        }

        for (Monkey monkey : monkeys) {
            monkey.setProductDivisibleBy(productDivisibleBy);
        }

        return monkeys;
    }

    private static long[] monkeysMostInspections(List<Monkey> monkeys) {
        long[] output = new long[2];

        long max_1 = 0;
        long max_2 = 0;

        for (Monkey monkey : monkeys) {
            if (monkey.getInspections() > max_1) {
                max_2 = max_1;
                max_1 = monkey.getInspections();
            } else if (monkey.getInspections() > max_2) {
                max_2 = monkey.getInspections();
            }
        }

        output[0] = max_1;
        output[1] = max_2;

        return output;
    }

    private static List<String> findData(String line, String regex) {
        List<String> output = new ArrayList<String>();

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(line);

        if (!matcher.find()) throw new IllegalArgumentException();

        for (int i = 1; i <= matcher.groupCount(); i++) {
            output.add(matcher.group(i));
        }
        return output;
    }

    private static long calculateMonkeyBusiness(long monkey_1, long monkey_2) {
        return monkey_1 * monkey_2;
    }
}

class Monkey {
    int name;
    Queue<Long> items = new LinkedList<Long>();
    String operation;
    long number;
    long divisibleBy;
    int monkeyTrue;
    int monkeyFalse;
    boolean reliefDivide;

    long inspections = 0;

    List<Monkey> monkeys;

    long productDivisibleBy;

    Monkey(int name, List<Long> items, String operation, long number, long divisibleBy, int monkeyTrue, int monkeyFalse, boolean reliefDivide) {
        this.name = name;
        
        for (long item : items) {
            this.items.add(item);
        }

        this.operation = operation;
        this.number = number;
        this.divisibleBy = divisibleBy;
        this.monkeyTrue = monkeyTrue;
        this.monkeyFalse = monkeyFalse;
        this.reliefDivide = reliefDivide;
    }

    public long getInspections() {
        return this.inspections;
    }

    public long getDivisibleBy() {
        return this.divisibleBy;
    }

    public void setProductDivisibleBy(long productDivisibleBy) {
        this.productDivisibleBy = productDivisibleBy;
    }

    public void setMonkeys(List<Monkey> monkeys) {
        this.monkeys = monkeys;
    }

    public void throwItems() {
        while (this.items.size() > 0) {
            throwItem();
        }
    }

    private void throwItem() {
        long item = this.items.poll();
        this.inspections++;

        if (operation.equals("+")) {
            item += this.number;
        } else if (operation.equals("*")) {
            item *= this.number;
        } else if (operation.equals("2")) {
            item *= item;
        }

        if (reliefDivide) {
            item /= 3;
        }

        item = item % this.productDivisibleBy;

        if (item % this.divisibleBy == 0) {
            // throw to monkeyTrue
            monkeys.get(monkeyTrue).catchItem(item);
        } else {
            // throw to monkeyFalse
            monkeys.get(monkeyFalse).catchItem(item);
        }
    }

    public void catchItem(long item) {
        this.items.add(item);
    }
}