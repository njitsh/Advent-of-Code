package Days;
import java.util.*;

public class Day7 extends Day {
    public static void main(String[] args) {
        System.out.println(new Day7().part1());
        System.out.println(new Day7().part2());
    }

    public List<String> getInput(String location) {
        return super.getInput(location);
    }

    public String part1() {
        List<String> input = new Day7().getInput("Days/input_day7.txt");

        // Create a map of directories
        Directory rootDir = createDirectory(input);

        // Find the directory with a total size of at most 100000
        int totalSize = getDirectoriesSizeByMaxSize(rootDir, 100000);

        return Integer.toString(totalSize);
    }

    public String part2() {
        List<String> input = new Day7().getInput("Days/input_day7.txt");

        // Create a map of directories
        Directory rootDir = createDirectory(input);

        int sizeToClearUp = 30000000 - (70000000 - rootDir.getSize());

        // Find the directory with a total size of at most 100000
        int directorySize = getDirectorySizeByMinSize(rootDir, sizeToClearUp);

        return Integer.toString(directorySize);
    }

    private static Directory createDirectory(List<String> input) {
        Directory rootDir = new Directory("/", null);
        Directory activeDir = rootDir;

        for (int i = 1; i < input.size();) {
            String command = input.get(i);

            // Find the next command
            int nextCommand = i + 1 + findNextCommand(input.subList(i + 1, input.size()));
            activeDir = executeCommand(activeDir, command, input.subList(i + 1, nextCommand));

            i = nextCommand;
        }

        return rootDir;
    }

    private static int findNextCommand(List<String> input) {
        for (int i = 0; i < input.size(); i++) {
            if (input.get(i).startsWith("$")) {
                return i;
            }
        }
        return input.size();
    }

    private static Directory executeCommand(Directory parent, String command, List<String> output) {
        switch (command.substring(0, 4)) {
            case "$ cd":
                String remainingCommand = command.substring(5);

                // Change directory
                if (remainingCommand.equals("/")) {
                    // Go to root directory
                    parent = parent.getRoot();
                } else if (remainingCommand.equals("..")) {
                    // Go to parent directory
                    parent = parent.getParent();
                } else {
                    // Go to child directory
                    String dirName = remainingCommand;
                    parent = parent.getDirectory(dirName);
                }

                break;
            case "$ ls":
                // Add all directories and files to the parent directory
                // Until next command
                for (String line : output) {
                    if (line.startsWith("dir")) {
                        // Add directory
                        String dirName = line.substring(4);
                        Directory dir = new Directory(dirName, parent);
                        parent.addDirectory(dir);
                    } else {
                        // Add file
                        String[] fileData = line.split(" ");
                        int fileSize = Integer.parseInt(fileData[0]);
                        String fileName = fileData[1];
                        DeviceFile file = new DeviceFile(fileName, fileSize);
                        parent.addFile(file);
                    }
                }

                break;
            default:
                throw new IllegalArgumentException("Invalid command");
        }

        return parent;
    }

    public int getDirectoriesSizeByMaxSize(Directory rootDir, int maxSize) {
        int size = 0;
        
        // Queue of directories to check
        Queue<Directory> queue = new LinkedList<>();

        // Add root directory to queue
        queue.add(rootDir);

        while (!queue.isEmpty()) {
            Directory dir = queue.remove();

            // Check if the directory is small enough
            int dirSize = dir.getSize();
            if (dirSize <= maxSize) {
                size += dirSize;
            }

            // Add all child directories to the queue
            for (Directory childDir : dir.getChildDirectories()) {
                queue.add(childDir);
            }
        }

        return size;
    }

    public int getDirectorySizeByMinSize(Directory rootDir, int minSize) {
        int smallestSize = rootDir.getSize();
        
        // Queue of directories to check
        Queue<Directory> queue = new LinkedList<>();

        // Add root directory to queue
        queue.add(rootDir);

        while (!queue.isEmpty()) {
            Directory dir = queue.remove();

            int dirSize = dir.getSize();

            if (dirSize < minSize) {
                continue;
            }

            // Check if the directory is small enough
            if (dirSize < smallestSize) {
                smallestSize = dirSize;
            }

            // Add all child directories to the queue
            for (Directory childDir : dir.getChildDirectories()) {
                queue.add(childDir);
            }
        }

        return smallestSize;
    }
}

class Directory {
    private String name;
    private Directory parent;

    private List<Directory> directories;
    private List<DeviceFile> files;

    public Directory(String name, Directory parent) {
        this.name = name;
        this.parent = parent;
        this.directories = new ArrayList<>();
        this.files = new ArrayList<>();
    }

    public void addFile(DeviceFile file) {
        this.files.add(file);
    }

    public void addDirectory(Directory dir) {
        this.directories.add(dir);
    }

    public String getName() {
        return this.name;
    }

    public int getSize() {
        int size = 0;

        for (DeviceFile file : this.files) {
            size += file.getSize();
        }

        for (Directory dir : this.directories) {
            size += dir.getSize();
        }

        return size;
    }

    public Directory getRoot() {
        Directory root = this;
        while (root.getParent() != null) {
            root = root.getParent();
        }
        return root;
    }

    public Directory getParent() {
        return this.parent;
    }

    public Directory getDirectory(String name) {
        for (Directory dir : this.directories) {
            if (dir.getName().equals(name)) {
                return dir;
            }
        }
        return null;
    }

    public List<Directory> getChildDirectories() {
        return this.directories;
    }
}

class DeviceFile {
    private String name;
    private int size;

    public DeviceFile(String name, int size) {
        this.name = name;
        this.size = size;
    }

    public String getName() {
        return this.name;
    }

    public int getSize() {
        return this.size;
    }
}
