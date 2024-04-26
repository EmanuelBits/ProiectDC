public class PrimeNumbers {
    public static void main(String[] args) {
        try {
            Prime p1 = new Prime(5);
            System.out.println("Value : " + p1.getValue());
            System.out.println("Index : " + p1.getIndex());
        } catch (IllegalArgumentException exception) {
            System.out.println(exception.getMessage());
        }
    }
}

final class Prime {
    private final long value;
    private final long Index;

    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_RESET = "\u001B[0m";

    public Prime(long value) throws IllegalArgumentException {
        if (isPrime(value)){
            this.value = value;
            this.Index = findIndex(value);
        } else {
            throw new IllegalArgumentException(ANSI_RED + "The number is NOT prime!" + ANSI_RESET);
        }
    }
    public Prime(long indexOrValue, boolean indexOrFind) {
        if (indexOrFind == true) {
            if (indexOrValue < 1) throw new IllegalArgumentException(ANSI_RED + "Value is smaller than 1!" + ANSI_RESET);
            else {
                this.value = findPrime(indexOrValue);
                this.Index = indexOrValue;
            }
        } else {
            if (!isPrime(indexOrValue)){
                throw new IllegalArgumentException(ANSI_RED + "The number is NOT prime!" + ANSI_RESET);
            } else {
                this.value = indexOrValue;   
                this.Index = findIndex(indexOrValue);
            }
        }
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

    public long getValue() {
        return this.value;
    }
    public long getIndex() {
        return this.Index;
    }
}
