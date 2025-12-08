package cc.graph;

import java.util.Arrays;
import java.util.List;

/**
 * Kuhn's Algorithm for Maximum Bipartite Matching using DFS.
 * V - is a left set of vertices.
 * W - is a right set of vertices.
 * graph - adjacency list of "v -> w" edges.
 *
 * The complexity of algo:
 */
public class KuhnAlgorithm {

    private final int nV, nW;
    private final List<Integer>[] graph;
    private final int[] matchedWToV;

    private boolean[] visited;

    public KuhnAlgorithm(int nV, int nW, List<Integer>[] graph) {
        this.nV = nV;
        this.nW = nW;
        this.graph = graph;

        this.matchedWToV = new int[nW];
        Arrays.fill(matchedWToV, -1);
    }

    public int[] findMaxBipartiteMatching() {
        for (int v = 0; v < nV; v++) {
            visited = new boolean[nW];
            dfs(v);
        }
        return matchedWToV;
    }

    private boolean dfs(int v) {
        for (int w : graph[v]) {
            if (visited[w])
                continue;
            visited[w] = true;
            // a) no match, or b) try to do alternative match
            if (matchedWToV[w] == -1 || dfs(matchedWToV[w])) {
                matchedWToV[w] = v;
                return true;
            }
        }
        return false;
    }
}
