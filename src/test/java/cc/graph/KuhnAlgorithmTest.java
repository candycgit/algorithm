package cc.graph;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class KuhnAlgorithmTest {

    @Test
    void testEmptyGraph() {
        int nV = 0;
        int nW = 0;
        int[][] edges = {};
        List<Integer>[] graph = createGraph(nV, edges);
        KuhnAlgorithm solver = new KuhnAlgorithm(nV, nW, graph);
        var result = solver.findMaxBipartiteMatching();
        assertEquals(0, result.m);
    }

    @Test
    void testPerfectMatching() {
        // V={0, 1, 2}, W={0, 1, 2}. Edges: (0, 0), (1, 1), (2, 2)
        int nV = 3;
        int nW = 3;
        int[][] edges = {{0, 0}, {1, 1}, {2, 2}};
        List<Integer>[] graph = createGraph(nV, edges);
        KuhnAlgorithm solver = new KuhnAlgorithm(nV, nW, graph);
        var result = solver.findMaxBipartiteMatching();

        assertEquals(3, result.m);

        assertEquals(0, result.matchedWToV[0]); // w0 -> v0
        assertEquals(1, result.matchedWToV[1]); // w1 -> v1
        assertEquals(2, result.matchedWToV[2]); // w2 -> v2
    }

    @Test
    void testMaximumMatching_AugmentingPathRequired() {
        // V={0, 1}, W={0, 1}. Edges: (0, 0), (1, 0), (0, 1)
        int nV = 2;
        int nW = 2;
        int[][] edges = {{0, 0}, {1, 0}, {0, 1}};
        List<Integer>[] graph = createGraph(nV, edges);
        KuhnAlgorithm solver = new KuhnAlgorithm(nV, nW, graph);
        var result = solver.findMaxBipartiteMatching();

        assertEquals(2, result.m);

        assertEquals(1, result.matchedWToV[0]); // w0 -> v1
        assertEquals(0, result.matchedWToV[1]); // w1 -> v0
    }

    @Test
    void testLeftSetLargerThanRightSet() {
        // V={0, 1, 2}, W={0, 1}. Max possible match is nW=2.
        int nV = 3;
        int nW = 2;
        int[][] edges = {{0, 0}, {1, 0}, {2, 1}};
        List<Integer>[] graph = createGraph(nV, edges);
        KuhnAlgorithm solver = new KuhnAlgorithm(nV, nW, graph);
        var result = solver.findMaxBipartiteMatching();

        assertEquals(2, result.m);
    }

    @Test
    void testRightSetLargerThanLeftSet() {
        // V={0, 1}, W={0, 1, 2, 3}. Max possible match is nV=2.
        int nV = 2;
        int nW = 4;
        int[][] edges = {{0, 0}, {0, 1}, {1, 2}, {1, 3}};
        List<Integer>[] graph = createGraph(nV, edges);
        KuhnAlgorithm solver = new KuhnAlgorithm(nV, nW, graph);
        var result = solver.findMaxBipartiteMatching();

        assertEquals(2, result.m);
    }

    @Test
    void testDisjointComponents() {
        // Two separate small matchings in the graph: (0, 0) and (2, 2)
        // V={0, 1, 2, 3}, W={0, 1, 2, 3}. Edges: (0, 0), (2, 2)
        int nV = 4;
        int nW = 4;
        int[][] edges = {{0, 0}, {2, 2}};
        List<Integer>[] graph = createGraph(nV, edges);
        KuhnAlgorithm solver = new KuhnAlgorithm(nV, nW, graph);
        var result = solver.findMaxBipartiteMatching();

        // Expected match size = 2
        assertEquals(2, result.m);

        assertEquals(0, result.matchedWToV[0]); // w0 -> v0
        assertEquals(-1, result.matchedWToV[1]); // w1 unmatched
        assertEquals(2, result.matchedWToV[2]); // w2 -> v2
        assertEquals(-1, result.matchedWToV[3]); // w3 unmatched
    }

    private List<Integer>[] createGraph(int nV, int[][] edges) {
        @SuppressWarnings("unchecked")
        List<Integer>[] graph = new List[nV];
        for (int i = 0; i < nV; i++) {
            graph[i] = new ArrayList<>();
        }
        for (int[] edge : edges) {
            int v = edge[0];
            int w = edge[1];
            graph[v].add(w);
        }
        return graph;
    }
}