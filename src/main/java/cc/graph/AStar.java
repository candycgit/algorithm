package cc.graph;

import java.awt.Point;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;

/**
 * A Star algorithm for M x N matrix
 */
public class AStar {

    private static final String MSG_INVALID_GRID = "Invalid grid";
    private static final int[][] MOVE = new int[][]{
            {-1, 0}, {0, -1}, {+1, 0}, {0, +1}
            //{-1, -1}, {-1, +1}, {+1, -1}, {+1, +1}
    };

    private static final char EMPTY = '-';
    private static final char OBSTACLE = '#';

    private final int m;
    private final int n;
    private final char[][] grid;

    public AStar(char[][] grid) {
        if (grid == null) {
            throw new IllegalArgumentException(MSG_INVALID_GRID);
        }
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

    public Stack<Point> findPath(Point source, Point target) {
        Stack<Point> result = new Stack<>();
        if (!valid(source) || !valid(target) || source.equals(target)) {
            return result;
        }

        boolean[][] closedList = new boolean[m][n];

        Track[][] tracks = new Track[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                tracks[i][j] = new Track();
            }
        }
        tracks[source.x][source.y] = new Track(source.x, source.y, 0.0, 0.0, 0.0);

        Queue<FPoint> openList = new PriorityQueue<>();
        openList.add(new FPoint(0.0, source));
        boolean found = false;

        while (!found && !openList.isEmpty()) {
            FPoint p = openList.poll();
            closedList[p.x][p.y] = true;
            for (int k = 0; k < MOVE.length; k++) {
                int x = p.x + MOVE[k][0];
                int y = p.y + MOVE[k][1];
                if (valid(x, y)) {
                    if (x == target.x && y == target.y) {
                        tracks[x][y].parentX = p.x;
                        tracks[x][y].parentY = p.y;
                        trace(tracks, target, result);
                        found = true;
                    } else if (!closedList[x][y]) {
                        double G = tracks[p.x][p.y].g + 1.0;
                        double H = heuristic(x, y, target.x, target.y);
                        double F = G + H;
                        if (F < tracks[x][y].f) {
                            tracks[x][y].parentX = p.x;
                            tracks[x][y].parentY = p.y;
                            tracks[x][y].g = G;
                            tracks[x][y].h = H;
                            tracks[x][y].f = F;
                            openList.add(new FPoint(F, new Point(x, y)));
                        }
                    }
                }
            }
        }

        return result;
    }

    private void trace(Track[][] tracks, Point target, Stack<Point> result) {
        int x = target.x;
        int y = target.y;
        while (!(tracks[x][y].parentX == x && tracks[x][y].parentY == y)) {
            result.push(new Point(x, y));
            int nextX = tracks[x][y].parentX;
            int nextY = tracks[x][y].parentY;
            x = nextX;
            y = nextY;
        }
    }

    /**
     * Heuristics for grid maps:
     *   - Manhattan distance
     *   - Diagonal distance
     *   - Euclidean distance (selected)
     */
    private double heuristic(int x1, int y1, int x2, int y2) {
        double deltaX = x1 - x2;
        double deltaY = y1 - y2;
        return Math.sqrt(deltaX * deltaX + deltaY * deltaY);
    }

    private boolean valid(Point point) {
        return point != null && valid(point.x, point.y);
    }

    private boolean valid(int x, int y) {
        return x >= 0 && x < m && y >= 0 && y < n && grid[x][y] == EMPTY;
    }

    private static class Track {
        private int parentX = -1;
        private int parentY = -1;
        // f = g + h
        private double f = Double.MAX_VALUE;
        private double g = Double.MAX_VALUE;
        private double h = Double.MAX_VALUE;

        private Track() {
        }

        private Track(int parentX, int parentY, double f, double g, double h) {
            this.parentX = parentX;
            this.parentY = parentY;
            this.f = f;
            this.g = g;
            this.h = h;
        }
    }

    private static class FPoint extends Point implements Comparable<FPoint> {
        // f = g + h
        private double f = 0.0;

        public FPoint(double f, Point point) {
            super(point);
            this.f = f;
        }

        @Override
        public int compareTo(FPoint other) {
            return java.lang.Double.compare(this.f, other.f);
        }
    }

}