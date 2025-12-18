package cc.graph;

import java.util.*;

/**
 * Traveling Salesman Problem searches for shortest path in graph
 * starting from start-node 0, visiting every vertex exactly once, and returning back to start-node 0 (Hamiltonian cycle).
 * Complexity: O(2^n * E).
 * Note: Hamiltonian path solution will need an adjustment to this code as path can start from any graph node.
 */
public class BellmanHeldKarpAlgorithm {

    private static final int INF = 1_000_000_000;

    public static void solveTSP(Node[] graph) {
        int n = graph.length;
        int numStates = 1 << n; // generated sets of node combinations via bitmask
        int[][] dp = new int[numStates][n];
        int[][] parent = new int[numStates][n]; // path's "breadcrumbs"

        for (int[] row : dp) Arrays.fill(row, INF);
        for (int[] row : parent) Arrays.fill(row, -1);

        dp[1 << 0][0] = 0; // start-node

        for (int mask = 1; mask < numStates; mask++) {
            for (int u = 0; u < n; u++) {
                if ((mask & (1 << u)) == 0) continue; // node u is not in currently selected mask-combination
                if (dp[mask][u] == INF) continue; // unreachable state
                for (int k = 0; k < graph[u].adj.length; k++) {
                    int v = graph[u].adj[k];
                    int cost = graph[u].cost[k];
                    if ((mask & (1 << v)) != 0) continue; // already visited - skip
                    int nextMask = mask | (1 << v);
                    if (dp[nextMask][v] > dp[mask][u] + cost) { // optimizing cost
                        dp[nextMask][v] = dp[mask][u] + cost;
                        parent[nextMask][v] = u; // track that came to v from u
                    }
                }
            }
        }

        int finalMask = (1 << n) - 1; //  all nodes visited
        int minCost = INF;
        int lastNode = -1; // last node before returning to 0
        for (int u = 0; u < n; u++) {
            for (int k = 0; k < graph[u].adj.length; k++) {
                int v = graph[u].adj[k];
                int cost = graph[u].cost[k];
                if (v != 0 || dp[finalMask][u] == INF) continue; // if v is not a start-node 0 OR unreachable - then skip
                if (minCost > dp[finalMask][u] + cost) {
                    minCost = dp[finalMask][u] + cost;
                    lastNode = u;
                }
            }
        }

        if (lastNode == -1) {
            System.out.println("No Hamiltonian Cycle exists.");
        } else {
            System.out.println("Minimum Cost: " + minCost);
            printCycle(parent, lastNode, finalMask);
        }
    }

    private static void printCycle(int[][] parent, int current, int mask) {
        Stack<Integer> path = new Stack<>();
        while (current != -1) {
            path.push(current);
            int prev = parent[mask][current];
            mask &= ~(1 << current); // clear bit
            current = prev;
        }
        System.out.print("Cycle: ");
        while (!path.isEmpty()) {
            System.out.print(path.pop() + " -> ");
        }
        System.out.println("0"); // add start-node at the end to show full cycle
    }

    record Node(int[] adj, int[] cost) {}
}
