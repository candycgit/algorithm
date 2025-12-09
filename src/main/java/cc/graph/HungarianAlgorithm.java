package cc.graph;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
        var bipartiteGraph = algorithm.createBipartiteGraph();
        var maxBipartiteMatch = algorithm.findMaxBipartiteMatching(bipartiteGraph);
        var minLineCover = algorithm.findMinimumLineCover(bipartiteGraph, maxBipartiteMatch);
        if (algorithm.isDone(minLineCover)) {
            algorithm.adjustMatrix(minLineCover);
        }
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

    /**
     * Step 3.1 of algo - see readme.
     */
    private List<Integer>[] createBipartiteGraph() {
        List<Integer>[] graph = new ArrayList[n];
        for (int i = 0; i < n; i++) {
            graph[i] = new ArrayList<>();
            for (int j = 0; j < n; j++) {
                if (costMatrix[i][j] == 0) {
                    graph[i].add(j);
                }
            }
        }
        return graph;
    }

    /**
     * Step 3.1 of algo - see readme.
     */
    private KuhnAlgorithm.MaxBipartiteMatch findMaxBipartiteMatching(List<Integer>[] bipartiteGraph) {
        KuhnAlgorithm kuhn = new KuhnAlgorithm(n, n, bipartiteGraph);
        return kuhn.findMaxBipartiteMatching();
    }

    /**
     * Step 3.2 of algo - see readme.
     */
    private MinimumLineCover findMinimumLineCover(List<Integer>[] bipartiteGraph, KuhnAlgorithm.MaxBipartiteMatch maxBipartiteMatch) {
        boolean[] rowReached = new boolean[n];
        boolean[] colReached = new boolean[n];

        // find unassigned rows
        boolean[] assignedRow = new boolean[n];
        for (int v : maxBipartiteMatch.matchedWToV) {
            if (v != -1)
                assignedRow[v] = true;
        }

        // start DFS from unassigned rows: unmatched edge -> matched edge -> unmatched edge -> ...
        for (int v = 0; v < n; v++) {
            if (!assignedRow[v])
                reach(v, bipartiteGraph, maxBipartiteMatch, rowReached, colReached);
        }

        // prepare covered rows and covered columns
        var minLineCover = new MinimumLineCover(new HashSet<>(), new HashSet<>());
        for (int i = 0; i < n; i++) {
            if (!rowReached[i]) {
                minLineCover.coveredRows.add(i);
            }
        }
        for (int j = 0; j < n; j++) {
            if (colReached[j]) {
                minLineCover.coveredCols.add(j);
            }
        }
        return minLineCover;
    }

    /**
     * DFS as Alternating Path traversal - for step 3.2
     */
    private void reach(int v, List<Integer>[] graph, KuhnAlgorithm.MaxBipartiteMatch match,
                       boolean[] rowReached, boolean[] colReached) {
        rowReached[v] = true;
        for (int w : graph[v]) {
            if (colReached[w])
                continue;
            colReached[w] = true;
            var nextV = match.matchedWToV[w];
            if (nextV != -1 && !rowReached[nextV]) {
                reach(nextV, graph, match, rowReached, colReached);
            }
        }
    }

    /**
     * Analyze results of step 3 of algo.
     */
    private boolean isDone(MinimumLineCover minLineCover) {
        return minLineCover.linesCount() == n;
    }

    /**
     * Step 4 of algo - see readme.
     */
    private void adjustMatrix(MinimumLineCover minLineCover) {
        int h = Integer.MAX_VALUE;
        // find min uncovered value h
        for (int i = 0; i < n; i++) {
            if (minLineCover.coveredRows.contains(i))
                continue;
            for (int j = 0; j < n; j++) {
                if (minLineCover.coveredCols.contains(j))
                    continue;
                h = Math.min(h, costMatrix[i][j]);
            }
        }
        if (h == Integer.MAX_VALUE) {
            throw new RuntimeException("h cannot be found");
        }
        // adjust matrix
        for (int i = 0; i < n; i++) {
            boolean isCoveredRow = minLineCover.coveredRows.contains(i);
            for (int j = 0; j < n; j++) {
                boolean isCoveredColumn = minLineCover.coveredCols.contains(j);
                if (isCoveredRow && isCoveredColumn)
                    costMatrix[i][j] += h;
                if (!isCoveredRow && !isCoveredColumn)
                    costMatrix[i][j] -= h;
            }
        }
    }

    public record MinimumLineCover(Set<Integer> coveredRows, Set<Integer> coveredCols) {
        public int linesCount() {
            return coveredRows.size() + coveredCols.size();
        }
    }
}
