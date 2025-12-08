package cc.graph;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Stream;

public class HungarianAlgorithm {

    // the input is assumed to be 100% valid
    private static final String INPUT_FILE_NAME = "hungarian_input_1.txt";

    private int[][] costMatrix;
    private int n;

    public HungarianAlgorithm() throws Exception {
        initCostMatrix();
    }

    public static void main(String[] args) throws Exception {
        HungarianAlgorithm algorithm = new HungarianAlgorithm();
        algorithm.debug("Input read:");
        algorithm.rowReduction();
        algorithm.columnReduction();
        algorithm.debug("Reduction applied:");
    }

    private void initCostMatrix() throws Exception {
        System.out.println("Reading " + INPUT_FILE_NAME);
        try (InputStream resource = this.getClass().getClassLoader().getResourceAsStream(INPUT_FILE_NAME);
             BufferedReader reader = new BufferedReader(new InputStreamReader(resource))
        ) {
            n = Integer.parseInt(reader.readLine().trim());
            costMatrix = new int[n][n];
            for (int i = 0; i < n; i++) {
                costMatrix[i] = Stream.of(reader.readLine().trim().split("\\s+"))
                        .mapToInt(Integer::parseInt)
                        .toArray();
            }
        }
    }

    private void debug(String title) {
        System.out.println("\n" + title);
        System.out.printf("n = %s\n", n);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                System.out.printf("%4d ", costMatrix[i][j]);
            }
            System.out.println();
        }
    }

    /**
     * Step 1 of algo - see readme.
     */
    private void rowReduction() {
        for (int i = 0; i < n; i++) {
            int min = costMatrix[i][0];
            for (int j = 1; j < n; j++) {
                min = Math.min(min, costMatrix[i][j]);
            }
            for (int j = 0; j < n; j++) {
                costMatrix[i][j] -= min;
            }
        }
    }


    /**
     * Step 2 of algo - see readme.
     */
    private void columnReduction() {
        for (int j = 0; j < n; j++) {
            int min = costMatrix[0][j];
            for (int i = 1; i < n; i++) {
                min = Math.min(min, costMatrix[i][j]);
            }
            for (int i = 0; i < n; i++) {
                costMatrix[i][j] -= min;
            }
        }
    }

}
