import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ValidateCubicMaze {
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_RESET = "\u001B[0m";
    public static void main(String[] args) {
        String fileName = "maze256.txt"; // File name
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            int dim = Integer.parseInt(reader.readLine());

            boolean isValid = isValidCubicMaze(dim, reader);

            if (isValid)
                System.out.println(ANSI_GREEN + "The input file contains a valid cubic maze." + ANSI_RESET);
            else
                System.out.println(ANSI_RED + "The input file does NOT contain a valid cubic maze." + ANSI_RESET);
        } catch (IOException | NumberFormatException e) {
            System.out.println(ANSI_RED + "Error reading from file or invalid format." + ANSI_RESET);
        }
    }

    private static boolean isValidCubicMaze(int dim, BufferedReader reader) throws IOException {
        // Check if dim is positive
        if (dim <= 0)
            return false;

        // Loop through each matrix
        for (int m = 0; m < dim; m++) {
            // Read and check each value of the matrix
            for (int i = 0; i < dim; i++) {
                String[] values = reader.readLine().trim().split("\\s+");
                if (values.length != dim)
                    return false; // Incorrect number of values in a row
                for (String value : values) {
                    try {
                        Long.parseLong(value); // Attempt to parse value as long
                    } catch (NumberFormatException e) {
                        return false; // Failed to parse value as long
                    }
                }
            }
        }

        // Ensure no extra content in the file
        String line;
        while ((line = reader.readLine()) != null) {
            if (!line.trim().isEmpty())
                return false; // Non-empty line found after reading the maze
        }

        return true;
    }
}
