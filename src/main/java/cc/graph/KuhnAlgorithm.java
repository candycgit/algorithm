package cc.graph;

import java.util.List;

/**
 * Kuhn's Algorithm for Maximum Bipartite Matching using DFS.
 * U - is a left set of vertices.
 * V - is a right set of vertices.
 * graph - it's enough to have adjacency list of "v -> u" edges.
 *
 * The complexity of algo:
 */
public class KuhnAlgorithm {

    private final int sizeU, sizeV;
    private final List<Integer>[] graph;
    private final int[] matchedVToU;

    public KuhnAlgorithm(int sizeU, int sizeV, List<Integer>[] graph) {
        this.sizeU = sizeU;
        this.sizeV = sizeV;
        this.graph = graph;

        // result;
        this.matchedVToU = new int[sizeV];
    }


}
