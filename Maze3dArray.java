import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Maze3dArray {
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_YELLOW = "\u001B[33m";
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
            long size = maze.getDimension();
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
                // Print the path for testing
                System.out.println(ANSI_YELLOW + "Path for Maze " + cubes.size() + ": " + ANSI_RESET + maze.getPath());
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
    private final int dimension;
    private final ArrayList<PathNode> path;

    public Maze(long[][][] cube, int dimension) {
        this.cube = cube;
        this.dimension = dimension;
        this.path = generateRandomPath(this.dimension);
    }

    private ArrayList<PathNode> generateRandomPath(int dimension) {
        ArrayList<PathNode> randomPath = new ArrayList<>();
        
        boolean[][][] visitedCube = new boolean[dimension][dimension][dimension];
        visitedCube[0][0][0] = true; // Mark the start position as visited
        
        int x = 0, y = 0, z = 0; // Start position
        randomPath.add(new PathNode(x, y, z));
        
        while (x != dimension - 1 || y != dimension - 1 || z != dimension - 1) {
            ArrayList<Integer> directions = new ArrayList<>();
            
            // Check available directions
            if (x + 1 < dimension && !visitedCube[x + 1][y][z]) directions.add(0); // UP
            if (x - 1 >= 0 && !visitedCube[x - 1][y][z]) directions.add(1); // DOWN
            if (y + 1 < dimension && !visitedCube[x][y + 1][z]) directions.add(2); // AHEAD
            if (y - 1 >= 0 && !visitedCube[x][y - 1][z]) directions.add(3); // BACK
            if (z + 1 < dimension && !visitedCube[x][y][z + 1]) directions.add(4); // LEFT
            if (z - 1 >= 0 && !visitedCube[x][y][z - 1]) directions.add(5); // RIGHT
            
            if (directions.isEmpty()) {
                // If no available directions, backtrack
                PathNode lastNode = randomPath.remove(randomPath.size() - 1);
                x = lastNode.getX();
                y = lastNode.getY();
                z = lastNode.getZ();
            } else {
                // Choose a random direction from available directions
                int randIndex = (int) (Math.random() * directions.size());
                int direction = directions.get(randIndex);
                
                // Move to the new position
                switch (direction) {
                    case 0: x++; break; // UP
                    case 1: x--; break; // DOWN
                    case 2: y++; break; // AHEAD
                    case 3: y--; break; // BACK
                    case 4: z++; break; // LEFT
                    case 5: z--; break; // RIGHT
                }
                
                // Mark the new position as visited
                visitedCube[x][y][z] = true;
                
                // Add the new position to the path
                randomPath.add(new PathNode(x, y, z));
            }
        }
        return randomPath;
    }
    

    public long[][][] getCube() {
        return cube;
    }

    public int getDimension() {
        return dimension;
    }

    public ArrayList<PathNode> getPath() {
        return path;
    }
}

final class PathNode {
    private final int x;
    private final int y;
    private final int z;
    
    public PathNode(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public int getX() {
        return x;
    }
    
    public int getY() {
        return y;
    }
    
    public int getZ() {
        return z;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ", " + z + ")";
    }
}
