package cc.graph;

import java.awt.Point;
import java.util.Collections;
import java.util.List;

/**
 * A Star algorithm for M x N matrix
 */
public class AStar {

    private static final String MSG_INVALID_GRID = "Invalid grid";

    private static final char EMPTY = '-';
    private static final char OBSTACLE = '#';

    private final int m;
    private final int n;
    private final char[][] grid;

    public AStar(char[][] grid) {
        validateGridSize(grid);
        m = grid.length;
        n = grid[0].length;
        this.grid = new char[m][n];

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] != EMPTY && grid[i][j] != OBSTACLE) {
                    throw new IllegalArgumentException(MSG_INVALID_GRID);
                }
                this.grid[i][j] = grid[i][j];
            }
        }
    }

    public List<Point> findPath(Point source, Point target) {
        if (!valid(source) || !valid(target) || source.equals(target)) {
            return Collections.EMPTY_LIST;
        }
        return null;
    }

    private void validateGridSize(char[][] grid) {
        if (grid == null || grid.length == 0 || grid[0] == null || grid[0].length == 0) {
            throw new IllegalArgumentException(MSG_INVALID_GRID);
        }
        int cols = grid[0].length;
        for (int i = 1; i < grid.length; i++) {
            if (grid[i] == null || grid[i].length != cols) {
                throw new IllegalArgumentException(MSG_INVALID_GRID);
            }
        }
    }

    private boolean valid(Point point) {
        return point != null && point.x >= 0 && point.x < m && point.y >= 0 && point.y < m;
    }

}