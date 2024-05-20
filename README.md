# ProiectDC

  Our project is a performance measurement tool designed to evaluate the usage and efficiency of your computer's processor (CPU) and hard disk drive (HDD). This application provides detailed metrics and analysis, allowing users to understand the performance characteristics of their system under various conditions.

Key features of BenchmarkApp include:

    Real-time monitoring of CPU and HDD usage.
    Detailed reports of performance data.
    Customizable benchmarking tests to simulate different workloads.

CPU

The CPU benchmarking component of the app evaluates the performance of your processor by calculating prime numbers and measuring the computation time. The program uses the following key classes and methods:
PrimeNumbers Class

The PrimeNumbers class serves as the entry point for the CPU benchmarking. It performs the following tasks:

    Instantiates a Prime object with a specified value.
    Prints the prime value, its index, the benchmark score, and the computation time.

Prime Class

The Prime class encapsulates the logic for prime number validation, prime number generation by index, and benchmarking. It provides the following functionalities:

    Constructor: Accepts a number and determines if it's prime. If valid, it calculates the index of the prime number.
    Constructor: Accepts a number and a boolean flag to indicate whether to find the prime by value or index.
    isPrime: Determines if a given number is prime.
    findPrime: Finds the prime number at a specific index.
    findIndex: Finds the index of a given prime number.
    calculateBenchmarkScore: Computes a benchmark score based on the prime number, its index, and computation time.
    piecewiseScale: Helper method for scaling numbers to calculate the benchmark score.

Methods

    isPrime(long val): Checks if a given number is prime.
    findPrime(long indexOfPrime): Returns the prime number at the given index.
    findIndex(long prime): Returns the index of the given prime number.
    calculateBenchmarkScore(): Computes the benchmark score considering the value, index, and computation time.
    piecewiseScale(long number): Scales numbers for the benchmark score calculation.
    getValue(): Returns the prime number value.
    getIndex(): Returns the index of the prime number.
    getScore(): Returns the benchmark score.
    getComputationTime(): Returns the computation time in milliseconds.

The Prime class ensures the prime number calculations are efficient and accurately benchmarks the CPU performance based on the complexity and time taken to perform these calculations.

Main Algorithms

  1.Prime Number Check (isPrime method):
  
        Purpose: To determine if a given number is prime.
        Algorithm:
            A number is not prime if it is less than 2.
            2 is the only even prime number; other even numbers are not prime.
            Numbers divisible by 5 (except 5 itself) are not prime.
            For other numbers, check divisibility by odd numbers up to the square root of the number.

  2.Find Prime by Index (findPrime method):
  
        Purpose: To find the prime number at a specific index.
        Algorithm:
            Start with the known primes: 2 and 3.
            Use a loop to test successive odd numbers for primality until reaching the desired index.

  3.Find Index of a Prime (findIndex method):
  
        Purpose: To find the index of a given prime number.
        Algorithm:
            Start with the known primes and increment through odd numbers, counting primes until the given prime is found.

  "calculateBenchmarkScore" Method
  
        Purpose: To compute a benchmark score based on the prime number value, its index, and the computation time.
        Algorithm:
            Piecewise Scaling: Apply logarithmic scaling to the prime value and index to normalize their impact.
            Weighted Sum: Combine scaled value, scaled index, and computation time using predefined weights.
            Score Calculation: Calculate the final score using a scale factor and ensure the result is a long integer.

HDD

The HDD benchmarking component of the app evaluates the performance of your hard disk drive (HDD) by reading and writing large maze files and measuring the times taken for these operations. The program uses the following key classes and methods:

Maze3dArray Class

The Maze3dArray class serves as the entry point for the HDD benchmarking. It performs the following tasks:

    Instantiates a Maze object with a specified dimension.
    Prints the maze's dimension, file path, minimum read/write times, maximum read/write times, average read/write times, file length, and benchmark           scores for read and write operations.

Maze Class

The Maze class encapsulates the logic for reading and writing maze files, generating random paths, and benchmarking. It provides the following functionalities:

    Constructor: Creates a maze of a specified dimension, writes it to a file, and benchmarks the read/write operations.
    createCube: Creates a 3D array representing the maze.
    readMultipleTimes: Reads the maze file multiple times to measure read times.
    readCube: Reads the maze file into a 3D array.
    writeMultipleTimes: Writes the maze to a file multiple times to measure write times.
    fprintMazeInfo: Writes the maze information and path to a file.
    generateRandomPath: Generates a random path through the maze.
    storeMinReadingTime: Stores the minimum read time.
    storeMaxReadingTime: Stores the maximum read time.
    storeAvgReadingTime: Stores the average read time.
    storeMinWritingTime: Stores the minimum write time.
    storeMaxWritingTime: Stores the maximum write time.
    storeAvgWritingTime: Stores the average write time.
    calculateBenchmarkReadScore: Computes the benchmark score for read operations.
    calculateBenchmarkWriteScore: Computes the benchmark score for write operations.
    calculateOverallBenchmarkScore: Computes the overall benchmark score.

Methods

    getCube: Returns the 3D array representing the maze.
    getDimension: Returns the dimension of the maze.
    getPath: Returns the random path through the maze.
    getMinReadTime: Returns the minimum read time.
    getMaxReadTime: Returns the maximum read time.
    getAvgReadTime: Returns the average read time.
    getMinWriteTime: Returns the minimum write time.
    getMaxWriteTime: Returns the maximum write time.
    getAvgWriteTime: Returns the average write time.
    getReadScore: Returns the benchmark score for read operations.
    getWriteScore: Returns the benchmark score for write operations.
    getOverallScore: Returns the overall benchmark score.
    getMazeFileLength: Returns the length of the maze file.

By following this structure, the HDD benchmarking component provides a comprehensive evaluation of HDD performance through file read/write operations, making it an essential part of the app.

Main Algorithms
1. Maze Cube Creation (createCube method)

    Purpose: To create a 3D array (cube) for a maze of specified dimension by reading data from predefined files.
    Logic:
        The method checks if the given dimension is valid (allowed values are 4, 8, 16, 32, 64, 128, 256).
        Based on the dimension, it reads data from corresponding files multiple times to record the read times.
        It handles exceptions for invalid dimensions and file reading errors.

2. Reading the Cube (readCube method)

    Purpose: To read a maze configuration from a file and populate the 3D array.
    Logic:
        The method verifies the existence of the file and checks the dimension of the cube.
        It reads the maze data line by line, populating the 3D array while ensuring the data is complete and correctly formatted.

3. Writing the Cube (writeCube method)

    Purpose: To write the maze's configuration and path information into a file multiple times to record the write times.
    Logic:
        It writes the maze dimension, the entire 3D array, and the path with node values and positions to a file.
        It uses FileWriter and PrintWriter for writing the data to ensure proper resource management.

4. Generating Random Path (generateRandomPath method)

    Purpose: To generate a random path within the maze from the start to the end point.
    Logic:
        The method starts at (0,0,0) and randomly selects directions to move to the next cell until it reaches the end of the cube.
        It marks visited cells to avoid revisiting and backtracks if it hits a dead end.

Benchmark Calculation Methods

1. Calculating Reading and Writing Times

    Purpose: To store the minimum, maximum, and average reading and writing times.
    Logic:
        These methods (storeMinReadingTime, storeMaxReadingTime, storeAvgReadingTime, storeMinWritingTime, storeMaxWritingTime, storeAvgWritingTime) iterate through the recorded times and compute the respective metrics.
        Average times exclude the minimum and maximum recorded times to avoid outliers.

2. Calculating Benchmark Scores

    Purpose: To calculate the benchmark scores based on the dimension of the maze, file length, and read/write times.
    Logic:
        Reading Score (calculateBenchmarkReadScore):
            Normalizes the average read time by dividing by the number of read times.
            Uses logarithmic scaling for the file length and dimension to balance the score.
            Combines these values with weighted contributions to compute the final score.
        Writing Score (calculateBenchmarkWriteScore):
            Similar to reading score but uses the average write time and also considers the number of write times.
        Overall Score (calculateOverallBenchmarkScore):
            Computes the average of the read and write scores.

Conclusion

Our app provides a comprehensive suite for evaluating both CPU and HDD performance. By calculating prime numbers and measuring read/write times for mazes of various sizes, the app offers detailed insights into your system's capabilities. Whether you're assessing computational efficiency or storage speed, our app delivers accurate and valuable performance metrics.
