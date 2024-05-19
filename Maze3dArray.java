import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class Maze3dArray {
    public static void main(String[] args) throws IOException {
        Maze m1 = new Maze(32);

        System.out.println(m1.getDimension());
        System.out.println(m1.getPath());
        System.out.println("Minimum Read Time: " + m1.getMinReadTime() / 1_000_000.0);
        System.out.println("Maximum Read Time: " + m1.getMaxReadTime() / 1_000_000.0);
        System.out.println("Minimum Write Time: " + m1.getMinWriteTime() / 1_000_000.0);
        System.out.println("Maximum Write Time: " + m1.getMaxWriteTime() / 1_000_000.0);
        System.out.println("Average Read Time: " + m1.getAvgReadTime() / 1_000_000.0);
        System.out.println("Average Write Time: " + m1.getAvgWriteTime() / 1_000_000.0);

        System.out.println("File Length: " + m1.getMazeFileLength());

        System.out.println("Read Score: " + m1.getReadScore());
        System.out.println("Write Score: " + m1.getWriteScore());
        System.out.println("Overall Score: " + m1.getOverallScore());
    }
}

final class Maze {
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_RESET = "\u001B[0m";

    private final long[][][] cube;
    private final int dimension;
    private final ArrayList<PathNode> path;

    private final long mazeFileLength;

    private double[] readingTimes;
    private double[] writingTimes;
    private final double minReadTime;
    private final double maxReadTime;
    private final double avgReadTime;
    private final double minWriteTime;
    private final double maxWriteTime;
    private final double avgWriteTime;

    private final long readScore;
    private final long writeScore;
    private final long overallScore;

    private final int readTimesFor_4_8_16_32_64 = 16;
    private final int readTimesFor_128 = 5;
    private final int readTimesFor_256 = 4;
    private final int writeTimesForAll = 7;

    public Maze(int dimension) throws IOException {
        this.cube = createCube(dimension);
        this.dimension = dimension;
        this.path = generateRandomPath(this.dimension);
        writeCube();
        this.minReadTime = storeMinReadingTime();
        this.maxReadTime = storeMaxReadingTime();
        this.minWriteTime = storeMinWritingTime();
        this.maxWriteTime = storeMaxWritingTime();
        this.avgReadTime = storeAvgReadingTime();
        this.avgWriteTime = storeAvgWritingTime();

        this.mazeFileLength = getMazeFileLength(this.dimension);

        // Calculate benchmark scores
        this.readScore = calculateBenchmarkReadScore(this.dimension);
        this.writeScore = calculateBenchmarkWriteScore();
        this.overallScore = calculateOverallBenchmarkScore();
    }

    private long[][][] createCube(int dim) throws IOException {
        long[][][] readingCube = new long[dim][dim][dim];

        if (dim != 4 && dim != 8 && dim != 16 && dim != 32 && dim != 64 && dim != 128 && dim != 256) {
            throw new IllegalArgumentException(ANSI_RED + "Invalid maze dimension value : " + dim + ANSI_RESET);
        } else {
            switch (dim) {
                case 4:
                    readingCube = readMultipleTimes(readTimesFor_4_8_16_32_64, dim, MazesFiles.getM4());
                    break;
                case 8:
                    readingCube = readMultipleTimes(readTimesFor_4_8_16_32_64, dim, MazesFiles.getM8());
                    break;
                case 16:
                    readingCube = readMultipleTimes(readTimesFor_4_8_16_32_64, dim, MazesFiles.getM16());
                    break;
                case 32:
                    readingCube = readMultipleTimes(readTimesFor_4_8_16_32_64, dim, MazesFiles.getM32());
                    break;
                case 64:
                    readingCube = readMultipleTimes(readTimesFor_4_8_16_32_64, dim, MazesFiles.getM64());
                    break;
                case 128:
                    readingCube = readMultipleTimes(readTimesFor_128, dim, MazesFiles.getM128());
                    break;
                case 256:
                    readingCube = readMultipleTimes(readTimesFor_256, dim, MazesFiles.getM256());
                    break;
                default:
                    throw new IllegalArgumentException(ANSI_RED + "Invalid maze dimension value : " + dim + ANSI_RESET);
            }
            
        }
        return readingCube;
    }

    private long[][][] readMultipleTimes(int readTimes, int dim, String file) throws IOException{
        long[][][] readingCube = new long[dim][dim][dim];
        this.readingTimes = new double[readTimes];

        for (int i = 0; i < readTimes; i++) {
            long startTime = System.nanoTime();
            readingCube = readCube(dim, file);
            long endTime = System.nanoTime();
            this.readingTimes[i] = (endTime - startTime);
        }
        return readingCube;
    }

    private long[][][] readCube(int dim, String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new IOException(ANSI_RED + "File not found: " + filePath + ANSI_RESET);
        }

        Scanner scanner = null;
    
        try {
            scanner = new Scanner(file);
            if (!scanner.hasNextInt()) {
                throw new IOException(ANSI_RED + "No cube dimension found in the file." + ANSI_RESET);
            }
    
            int cubeDimension = scanner.nextInt();
            if (cubeDimension != dim) {
                throw new IOException(ANSI_RED + "Invalid cube parameter (dim) in readCube() method: " + dim + ANSI_RESET);
            }
    
            long[][][] readingCube = new long[cubeDimension][cubeDimension][cubeDimension];
            int totalLinesRead = 0;
    
            for (int z = 0; z < cubeDimension; z++) {
                for (int y = 0; y < cubeDimension; y++) {
                    for (int x = 0; x < cubeDimension; x++) {
                        if (!scanner.hasNextLong()) {
                            throw new IOException(ANSI_RED + "Incomplete cube. Expected " + (cubeDimension * cubeDimension * cubeDimension) + " elements." + ANSI_RESET);
                        }
                        readingCube[x][y][z] = scanner.nextLong();
                    }
                    totalLinesRead++;
                }
            }
    
            int expectedTotalLines = cubeDimension * cubeDimension;
            if (totalLinesRead != expectedTotalLines) {
                throw new IOException(ANSI_RED + "Incomplete cube. Expected " + expectedTotalLines + " (" + cubeDimension + " * " + cubeDimension + ") lines." + ANSI_RESET);
            }
    
            return readingCube;
        } catch (IOException e) {
            throw new IOException(ANSI_RED + "Error reading file: " + filePath + ANSI_RESET, e);
        } catch (IllegalArgumentException e) {
            throw e; // Rethrow the exception after logging if necessary
        } finally {
            if (scanner != null) {
                scanner.close();
            }
        }
    }
    
    private void writeCube() throws IOException {
        this.writeMultipleTimes(MazesFiles.getMazePath());
    }
    
    private void writeMultipleTimes(String file) throws IOException {
        this.writingTimes = new double[this.writeTimesForAll];
        for (int i = 0; i < this.writeTimesForAll; i++) {
            long startTime = System.nanoTime(); 
            this.fprintMazeInfo(file);
            long endTime = System.nanoTime();
            this.writingTimes[i] = (endTime - startTime);
        }
    }
    
    private void fprintMazeInfo(String file) throws IOException {
        long minNode = Long.MAX_VALUE;
        long maxNode = Long.MIN_VALUE;
        long pathLength = 0;
        long pathSum = 0;
        boolean sumExceededMaxValue = false;
    
        // Use try-with-resources to ensure the resources are closed properly
        try (FileWriter fileWriter = new FileWriter(file);  // Overwrites the file
             PrintWriter printWriter = new PrintWriter(fileWriter)) {
            
            printWriter.println("\tMaze dimension : " + this.dimension);
    
            for (int y = 0; y < this.dimension; y++) {
                for (int z = 0; z < this.dimension; z++) {
                    for (int x = 0; x < this.dimension; x++) {
                        printWriter.print(this.cube[x][y][z] + " ");
                    }
                    printWriter.print("\t");
                }
                printWriter.println();
            }
    
            printWriter.println("Maze path (with positions) : " + this.getPath());
            printWriter.print("Maze path (with values) : ");
    
            for (PathNode pathNode : this.getPath()) {
                long currentValue = this.cube[pathNode.getX()][pathNode.getY()][pathNode.getZ()];
                printWriter.print(" " + currentValue);
    
                if (currentValue < minNode) {
                    minNode = currentValue;
                } else if (currentValue > maxNode) {
                    maxNode = currentValue;
                }
    
                pathLength++;
    
                if (!sumExceededMaxValue) {
                    if ((pathSum + currentValue) > Long.MAX_VALUE) {
                        pathSum = Long.MAX_VALUE;
                        sumExceededMaxValue = true;
                    } else {
                        pathSum += currentValue;   
                    }
                }
            }
    
            printWriter.println();
            printWriter.println("Minimum value from the path : " + minNode);
            printWriter.println("Maximum value from the path : " + maxNode);
            printWriter.println("Path Length : " + pathLength);
            printWriter.println("Path Sum : " + pathSum);
            if (sumExceededMaxValue) {
                printWriter.println("\t\t\tSum exceeded Long.MAX_VALUE!");
            }
        }
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
    
    private double storeMinReadingTime() {
        double minTime = Double.MAX_VALUE;
        for (double time : this.readingTimes) {
            if (time < minTime) {
                minTime = time;
            }
        }
        return minTime;
    }

    private double storeMaxReadingTime() {
        double maxTime = Double.MIN_VALUE;
        for (double time : this.readingTimes) {
            if (time > maxTime) {
                maxTime = time;
            }
        }
        return maxTime;
    }

    private double storeAvgReadingTime() {
        double sum = 0;
        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;
        for (double time : this.readingTimes) {
            if (time < min) {
                min = time;
            }
            if (time > max) {
                max = time;
            }
            sum += time;
        }
        sum -= (min + max); // Subtract min and max from sum
        return sum / (readingTimes.length - 2); // Exclude min and max from the count
    }
    
    private double storeMinWritingTime() {
        double minTime = Double.MAX_VALUE;
        for (double time : this.writingTimes) {
            if (time < minTime) {
                minTime = time;
            }
        }
        return minTime;
    }
    
    private double storeMaxWritingTime() {
        double maxTime = Double.MIN_VALUE;
        for (double time : this.writingTimes) {
            if (time > maxTime) {
                maxTime = time;
            }
        }
        return maxTime;
    }

    private double storeAvgWritingTime() {
        double sum = 0;
        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;
        for (double time : this.writingTimes) {
            if (time < min) {
                min = time;
            }
            if (time > max) {
                max = time;
            }
            sum += time;
        }
        sum -= min + max; // Subtract min and max from sum
        return sum / (writingTimes.length - 2); // Exclude min and max from the count
    }

    private long calculateBenchmarkReadScore(long dim) {
        int readTimes;
        switch ((int)dim) {
            case 4, 8, 16, 32, 64:
                readTimes = readTimesFor_4_8_16_32_64;
                break;
            case 128:
                readTimes = readTimesFor_128;
                break;
            case 256:
                readTimes = readTimesFor_256;
                break;
            default:
                throw new IllegalArgumentException("Invalid maze dimension value: " + dim);
        }
    
        double normalizedReadTime = getAvgReadTime() / readTimes;
        double logFileLength = Math.log(getMazeFileLength() + 1); // Logarithmic scaling
        double logDimension = Math.log(dim * dim * dim + 1); // Logarithmic scaling
    
        // Adjust weights and calculate benchmark score
        double score = (normalizedReadTime * 0.2) + (logFileLength * 0.1) + (logDimension * 0.1);
        return Math.round(score);
    }     

    private long calculateBenchmarkWriteScore() {
        double normalizedWriteTime = getAvgWriteTime() / this.writeTimesForAll;
        double logFileLength = Math.log(getMazeFileLength() + 1); // Logarithmic scaling
        double logDimension = Math.log(getDimension() * getDimension() * getDimension() + 1); // Logarithmic scaling
        double logWriteTimes = Math.log(writeTimesForAll + 1); // Logarithmic scaling
    
        // Adjust weights and calculate benchmark score
        double score = (normalizedWriteTime * 0.2) + (logFileLength * 0.1) + (logDimension * 0.1) + (logWriteTimes * 0.1);
        return Math.round(score);
    }      

    private long calculateOverallBenchmarkScore() {
        return Math.round((this.readScore + this.writeScore) / 2);
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

    public double getMinReadTime() {
        return this.minReadTime;
    }
    
    public double getMaxReadTime() {
        return this.maxReadTime;
    }
    
    public double getMinWriteTime() {
        return this.minWriteTime;
    }
    
    public double getMaxWriteTime() {
        return this.maxWriteTime;
    }
    
    public double getAvgReadTime() {
        return this.avgReadTime;
    }
    
    public double getAvgWriteTime() {
        return this.avgWriteTime;
    }

    public long getReadScore() {
        return this.readScore;
    }
    
    public long getWriteScore() {
        return this.writeScore;
    }
    
    public long getOverallScore() {
        return this.overallScore;
    }

    private long getMazeFileLength(int dim) {
        String filePath;
        switch (dim) {
            case 4:
                filePath = MazesFiles.getM4();
                break;
            case 8:
                filePath = MazesFiles.getM8();
                break;
            case 16:
                filePath = MazesFiles.getM16();
                break;
            case 32:
                filePath = MazesFiles.getM32();
                break;
            case 64:
                filePath = MazesFiles.getM64();
                break;
            case 128:
                filePath = MazesFiles.getM128();
                break;
            case 256:
                filePath = MazesFiles.getM256();
                break;
            default:
                throw new IllegalArgumentException("Invalid maze dimension value: " + dim);
        }
        File file = new File(filePath);
        return file.length();
    }

    public long getMazeFileLength() {
        return mazeFileLength;
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

final class MazesFiles {
    private static final String basePath = "";

    private static final String m4 = basePath + "maze4.txt";
    private static final String m8 = basePath + "maze8.txt";
    private static final String m16 = basePath + "maze16.txt";
    private static final String m32 = basePath + "maze32.txt";
    private static final String m64 = basePath + "maze64.txt";
    private static final String m128 = basePath + "maze128.txt";
    private static final String m256 = basePath + "maze256.txt";

    private static final String mazePath = basePath + "path.txt";

    public static String getM4() {
        return MazesFiles.m4;
    }

    public static String getM8() {
        return MazesFiles.m8;
    }

    public static String getM16() {
        return MazesFiles.m16;
    }

    public static String getM32() {
        return MazesFiles.m32;
    }

    public static String getM64() {
        return MazesFiles.m64;
    }

    public static String getM128() {
        return MazesFiles.m128;
    }

    public static String getM256() {
        return MazesFiles.m256;
    }

    public static String getMazePath() {
        return MazesFiles.mazePath;
    }
}