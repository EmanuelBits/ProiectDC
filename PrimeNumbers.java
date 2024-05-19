public class PrimeNumbers {
    public static void main(String[] args) {
        try {
            Prime prime = new Prime(1299709);
            System.out.println("Prime Value: " + prime.getValue());
            System.out.println("Prime Index: " + prime.getIndex());
            System.out.println("Benchmark Score: " + prime.getScore());
            System.out.println("Computation Time: " + prime.getComputationTime() + " ms");
        } catch (IllegalArgumentException exception) {
            System.out.println(exception.getMessage());
        }
    }
}

final class Prime {
    private final long value;
    private final long index;
    private final long score;
    private final double computationTime;

    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_RESET = "\u001B[0m";

    public Prime(long value) throws IllegalArgumentException {
        long startTime = System.nanoTime();  // Record start time

        if (isPrime(value)) {
            this.value = value;
            this.index = findIndex(value);
        } else {
            throw new IllegalArgumentException(ANSI_RED + "The number is NOT prime!" + ANSI_RESET);
        }

        long endTime = System.nanoTime();  // Record end time
        this.computationTime = (endTime - startTime) / 1_000_000.0;  // Calculate and store computation time in milliseconds
        this.score = calculateBenchmarkScore();  // Calculate score when a prime is created
    }

    public Prime(long indexOrValue, boolean indexOrFind) {
        long startTime = System.nanoTime();  // Record start time

        if (indexOrFind) {
            if (indexOrValue < 1) {
                throw new IllegalArgumentException(ANSI_RED + "Value is smaller than 1!" + ANSI_RESET);
            } else {
                this.value = findPrime(indexOrValue);
                this.index = indexOrValue;
            }
        } else {
            if (!isPrime(indexOrValue)) {
                throw new IllegalArgumentException(ANSI_RED + "The number is NOT prime!" + ANSI_RESET);
            } else {
                this.value = indexOrValue;
                this.index = findIndex(indexOrValue);
            }
        }

        long endTime = System.nanoTime();  // Record end time
        this.computationTime = (endTime - startTime) / 1_000_000.0;  // Calculate and store computation time in milliseconds
        this.score = calculateBenchmarkScore();  // Calculate score when a prime is created
    }

    private static boolean isPrime(long val) {
        if ((val == 0) || (val == 1)) return false;
        else if (val == 2) return true;
        else if (val % 2 == 0) return false;
        else if ((val % 5 == 0) && (val != 5)) return false;
        else {
            for (long i = 3; i <= Math.sqrt(val); i+=2) {
                if (val % i == 0) return false;
            }
        }
        return true;
    }

    private static long findPrime(long indexOfPrime){
        long index = 2;
        long number = 3;
        if (indexOfPrime == 1) return 2;
        else if (indexOfPrime == 2) return 3;
        else {
            while (index <= indexOfPrime) {
                number+=2;
                if (isPrime(number)){
                    index++;
                }
                if (index == indexOfPrime) return number;
            }
        }
        return -1;
    }

    private static long findIndex(long prime) {
        long number = 7;
        long index = 4;
        if (prime == 2) return 1;
        else if (prime == 3) return 2;
        else if (prime == 5) return 3;
        else if (prime == 7) return 4;
        else {
            while (number < prime) {
                number += 2;
                if (isPrime(number)) {
                    index++;
                    if (number == prime) return index;
                }
            }
        }
        return -1;
    }

    private long calculateBenchmarkScore() {
        double weightValue = 0.3;
        double weightIndex = 0.3;
        double weightTime = 0.4;
    
        // Apply piecewise scaling
        double valueComponent = weightValue * piecewiseScale(this.value);
        double indexComponent = weightIndex * piecewiseScale(this.index);
        double timeComponent = weightTime * (this.computationTime + 1);
    
        // Adjusting the scale factor to decrease the score
        double scaleFactor = 1000;  // Smaller scale factor for a smaller score
    
        // Final score calculation, ensuring the result is of type long
        double score = scaleFactor * (valueComponent + indexComponent) / timeComponent;
    
        // Return the score as a long type
        return (long) Math.round(score);
    }
    
    // Helper method for piecewise scaling
    private double piecewiseScale(long number) {
        if (number < 10) {
            return number;
        } else if (number < 100) {
            return Math.log(number) * 10;
        } else if (number < 1000) {
            return Math.log(number) * 20;
        } else {
            return Math.log(number) * 50;
        }
    }    

    public long getValue() {
        return this.value;
    }

    public long getIndex() {
        return this.index;
    }

    public long getScore() {
        return this.score;
    }

    public double getComputationTime() {
        return this.computationTime;
    }
}