package cc.graph;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.time.StopWatch;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AStarTest {

    private static final char PATH = '*';

    private static final StopWatch WATCH = new StopWatch();

    private static final String CASE_1 = "graph/a_star_case1.txt";
    private static final Point SOURCE_1 = new Point(3, 1);
    private static final Point TARGET_1 = new Point(6, 38);

    private static final String CASE_2 = "graph/a_star_case2.txt";
    private static final Point SOURCE_2 = new Point(3, 1);
    private static final Point TARGET_2 = new Point(6, 38);

    @BeforeEach
    public void setUp() {
        WATCH.reset();
    }

    @Test
    public void shouldFindPathForCase1() {
        WATCH.start();
        InOut case1 = null;
        try {
            case1 = new InOut(CASE_1);
        } catch (URISyntaxException | IOException exc) {
            fail("Could not read an input file: " + CASE_1);
        }
        AStar testInstance = new AStar(case1.grid);
        List<Point> steps = testInstance.findPath(SOURCE_1, TARGET_1);
        WATCH.stop();
        System.out.println("Time elapsed: " + WATCH.getTime());
        case1.process(steps);
        case1.printGrid();
        assertTrue(case1.answerIsValid());
    }

    @Test
    public void shouldFindPathForCase2() {
        WATCH.start();
        InOut case1 = null;
        try {
            case1 = new InOut(CASE_2);
        } catch (URISyntaxException | IOException exc) {
            fail("Could not read an input file: " + CASE_2);
        }
        AStar testInstance = new AStar(case1.grid);
        List<Point> steps = testInstance.findPath(SOURCE_2, TARGET_2);
        WATCH.stop();
        System.out.println("Time elapsed: " + WATCH.getTime());
        case1.process(steps);
        case1.printGrid();
        assertTrue(case1.answerIsValid());
    }

    private static class InOut {


        private final char[][] grid;
        private final char[][] answer;

        private InOut(String fileName) throws URISyntaxException, IOException {
            URL url = getClass().getClassLoader().getResource(fileName);
            if (url == null) {
                throw new FileNotFoundException("No file: " + fileName);
            }
            BufferedReader reader = Files.newBufferedReader(Paths.get(url.toURI()));

            List<String> input = new ArrayList<>();
            String line;
            while (reader.ready() && !(line = reader.readLine()).isEmpty()) {
                input.add(line);
            }

            List<String> output = new ArrayList<>();
            while (reader.ready()) {
                line = reader.readLine();
                output.add(line);
            }

            grid = new char[input.size()][];
            for (int i = 0; i < input.size(); i++) {
                grid[i] = input.get(i).toCharArray();
            }

            answer = new char[output.size()][];
            for (int i = 0; i < output.size(); i++) {
                answer[i] = output.get(i).toCharArray();
            }
        }

        private void process(List<Point> steps) {
            for (Point p : steps) {
                grid[p.x][p.y] = PATH;
            }
        }

        private boolean answerIsValid() {
            for (int i = 0; i < answer.length; i++) {
                for (int j = 0; j < answer[i].length; j++) {
                    if (answer[i][j] != grid[i][j]) {
                        return false;
                    }
                }
            }
            return true;
        }

        public void printGrid() {
            for (int i = 0; i < grid.length; i++) {
                for (int j = 0; j < grid[i].length; j++) {
                    System.out.print(grid[i][j]);
                }
                System.out.println("");
            }
        }
    }

}