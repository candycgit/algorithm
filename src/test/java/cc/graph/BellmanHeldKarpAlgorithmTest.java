package cc.graph;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class BellmanHeldKarpAlgorithmTest {


    @Test
    public void testSimpleSquareCycle() {
        // Graph: 0-1, 1-2, 2-3, 3-0 (All weights 10)
        // Shortest cycle: 0 -> 1 -> 2 -> 3 -> 0. Cost: 40
        BellmanHeldKarpAlgorithm.Node[] nodes = new BellmanHeldKarpAlgorithm.Node[4];
        nodes[0] = new BellmanHeldKarpAlgorithm.Node(new int[]{1, 3}, new int[]{10, 10});
        nodes[1] = new BellmanHeldKarpAlgorithm.Node(new int[]{0, 2}, new int[]{10, 10});
        nodes[2] = new BellmanHeldKarpAlgorithm.Node(new int[]{1, 3}, new int[]{10, 10});
        nodes[3] = new BellmanHeldKarpAlgorithm.Node(new int[]{0, 2}, new int[]{10, 10});

        String output = captureOutput(() -> BellmanHeldKarpAlgorithm.solveTSP(nodes));

        assertTrue(output.contains("Minimum Cost: 40"));
        assertTrue(output.contains("0 -> 1 -> 2 -> 3 -> 0") || output.contains("0 -> 3 -> 2 -> 1 -> 0"));
    }

    @Test
    public void testWeightedGraph() {
        // Graph with asymmetric weights
        // 0 -> 1 (2), 0 -> 2 (9)
        // 1 -> 2 (1), 1 -> 0 (10)
        // 2 -> 0 (6)
        // Best cycle: 0 -> 1 -> 2 -> 0. Cost: 2 + 1 + 6 = 9
        BellmanHeldKarpAlgorithm.Node[] nodes = new BellmanHeldKarpAlgorithm.Node[3];
        nodes[0] = new BellmanHeldKarpAlgorithm.Node(new int[]{1, 2}, new int[]{2, 9});
        nodes[1] = new BellmanHeldKarpAlgorithm.Node(new int[]{0, 2}, new int[]{10, 1});
        nodes[2] = new BellmanHeldKarpAlgorithm.Node(new int[]{0}, new int[]{6});

        String output = captureOutput(() -> BellmanHeldKarpAlgorithm.solveTSP(nodes));

        assertTrue(output.contains("Minimum Cost: 9"));
        assertTrue(output.contains("0 -> 1 -> 2 -> 0"));
    }

    @Test
    public void testNoHamiltonianCycle() {
        // Graph where node 3 is isolated or it's impossible to visit all and return
        // 0 -> 1 -> 2, but no way to reach 3 and return
        BellmanHeldKarpAlgorithm.Node[] nodes = new BellmanHeldKarpAlgorithm.Node[4];
        nodes[0] = new BellmanHeldKarpAlgorithm.Node(new int[]{1}, new int[]{1});
        nodes[1] = new BellmanHeldKarpAlgorithm.Node(new int[]{2}, new int[]{1});
        nodes[2] = new BellmanHeldKarpAlgorithm.Node(new int[]{0}, new int[]{1});
        nodes[3] = new BellmanHeldKarpAlgorithm.Node(new int[]{0}, new int[]{1}); // Node 3 can only go to 0, but no one can reach 3

        String output = captureOutput(() -> BellmanHeldKarpAlgorithm.solveTSP(nodes));

        assertTrue(output.contains("No Hamiltonian Cycle exists."));
    }

    /**
     * Helper to capture System.out for verification.
     */
    private String captureOutput(Runnable runnable) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(out));
        try {
            runnable.run();
        } finally {
            System.setOut(originalOut);
        }
        return out.toString();
    }
}
