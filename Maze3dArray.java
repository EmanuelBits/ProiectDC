import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Maze3dArray {
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_RESET = "\u001B[0m";

    private static Maze createMaze(Scanner scanner) throws IOException {
        if (!scanner.hasNextInt()) {
            return null; // End of file reached
        }
        
        int dimension = scanner.nextInt();
        long[][][] cube = new long[dimension][dimension][dimension];
        
        int totalLinesRead = 0;
        
        for (int z = 0; z < dimension; z++) {
            for (int y = 0; y < dimension; y++) {
                for (int x = 0; x < dimension; x++) {
                    if (!scanner.hasNextLong()) {
                        throw new IOException(ANSI_RED + "Incomplete maze. Expected " + dimension + " elements on each line." + ANSI_RESET);
                    }
                    cube[z][y][x] = scanner.nextLong();
                }
                totalLinesRead++;
            }
        }
        
        int expectedTotalLines = dimension * dimension;
        if (totalLinesRead != expectedTotalLines) {
            throw new IOException(ANSI_RED + "Incomplete maze. Expected " + expectedTotalLines + " lines for the entire maze." + ANSI_RESET);
        }
        
        return new Maze(cube, dimension);
    }    

    private static long getMaxSize(ArrayList<Maze> cubes) {
        long maxSize = 0;
        for (Maze maze : cubes) {
            long size = maze.getSize();
            if (size > maxSize) {
                maxSize = size;
            }
        }
        return maxSize;
    }

    public static void main(String[] args) {
        String filePath = "mazes.txt";
        ArrayList<Maze> cubes = new ArrayList<>(10);
        
        try (Scanner scanner = new Scanner(new File(filePath))) {
            while (true) {
                Maze maze = createMaze(scanner);
                if (maze == null) {
                    break; // End of file reached
                }
                cubes.add(maze);
            }
            System.out.println(ANSI_GREEN + "Successfully read " + cubes.size() + " maze(s) from the file." + ANSI_RESET);
        } catch (IOException e) {
            e.printStackTrace();
        }

        long maxSize = getMaxSize(cubes);
        System.out.println("Max size of maze: " + maxSize);
    }
}

final class Maze {
    private final long[][][] cube;
    private final long size;

    public Maze(long[][][] cube, long size) {
        this.cube = cube;
        this.size = size;
    }

    public long[][][] getCube() {
        return cube;
    }

    public long getSize() {
        return size;
    }
}
