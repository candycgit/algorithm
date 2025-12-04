package cc.graph;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Stream;

public class Hungarian {

    // the input is assumed to be 100% valid
    private static final String INPUT_FILE_NAME = "hungarian_input_1.txt";

    private int[][] costMatrix;
    private int n;

    public Hungarian() throws Exception {
        initCostMatrix();
    }

    public static void main(String[] args) throws Exception {
        Hungarian algorithm = new Hungarian();
        algorithm.debug("Input read:");
    }

    private void initCostMatrix() throws Exception {
        System.out.println("Reading " + INPUT_FILE_NAME);
        try (InputStream resource = this.getClass().getClassLoader().getResourceAsStream(INPUT_FILE_NAME);
             BufferedReader reader = new BufferedReader(new InputStreamReader(resource))
        ) {
            this.n = Integer.parseInt(reader.readLine().trim());
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
                System.out.printf("%s ", costMatrix[i][j]);
            }
            System.out.println();
        }
    }

}
