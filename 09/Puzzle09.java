import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

class Puzzle09 {
    public static void main(String[] args) {
        final File file = new File(args[0]);
        final Heightmap heightmap = readHeightmap(file);

        int threeLargestBasinProduct = 1;
        for (List<Point> basin : getThreeLargestBasins(heightmap.getBasins())) {
            threeLargestBasinProduct *= basin.size();
        }
        System.out.println(threeLargestBasinProduct);
    }

    private static List<List<Point>> getThreeLargestBasins(List<List<Point>> basins) {
        final Iterator<List<Point>> basinIterator = basins.iterator();
        final List<List<Point>> threeLargestBasins = new ArrayList<>();
        threeLargestBasins.add(basinIterator.next());
        threeLargestBasins.add(basinIterator.next());
        threeLargestBasins.add(basinIterator.next());

        while (basinIterator.hasNext()) {
            final List<Point> basin = basinIterator.next();
            for (List<Point> largeBasin : threeLargestBasins) {
                if (largeBasin.size() < basin.size()) {
                    threeLargestBasins.remove(largeBasin);
                    threeLargestBasins.add(basin);
                    break;
                }
            }
        }
        return threeLargestBasins;
    }

    private static Heightmap readHeightmap(File file) {
        try {
            final Scanner s = new Scanner(file);

            final List<List<Point>> points = new ArrayList<>();
            int rowCount = 0;
            while (s.hasNextLine()) {
                List<Point> row = new ArrayList<>();
                String[] rowInput = s.nextLine().split("");
                for (int col = 0; col < rowInput.length; col++) {
                    row.add(new Point(Integer.parseInt(rowInput[col]), col, rowCount));
                }
                points.add(row);
                rowCount++;
            }
            s.close();

            return new Heightmap(points);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}

class Heightmap {
    private final List<List<Point>> matrix;
    private final List<List<Point>> basins;

    public Heightmap(List<List<Point>> matrix) {
        this.matrix = matrix;
        calculateAdjacentPoints();
        this.basins = calculateBasins();
    }

    public List<List<Point>> getBasins() {
        return this.basins;
    }

    // Inspiration from
    // https://www.geeksforgeeks.org/breadth-first-search-or-bfs-for-a-graph/
    private List<Point> getBasin(Point point) {
        if (point.height() == 9) {
            return new ArrayList<>();
        }

        final List<Point> basin = new ArrayList<>();
        final Deque<Point> buffer = new ArrayDeque<>();
        basin.add(point);
        buffer.add(point);
        while (!buffer.isEmpty()) {
            Point nextPoint = buffer.poll();

            Iterator<Point> adjPoints = nextPoint.getAdjacentPoints().iterator();
            while (adjPoints.hasNext()) {
                Point nextAdjPoint = adjPoints.next();
                if (nextAdjPoint.height() < 9 && !basin.contains(nextAdjPoint)) {
                    basin.add(nextAdjPoint);
                    buffer.add(nextAdjPoint);
                }
            }
        }
        return basin;
    }

    private List<Point> getLowPoints() {
        final List<Point> points = new ArrayList<>();
        for (int y = 0; y < this.matrix.size(); y++) {
            for (int x = 0; x < this.matrix.get(0).size(); x++) {
                if (isLowPoint(this.matrix.get(y).get(x))) {
                    points.add(this.matrix.get(y).get(x));
                }
            }
        }
        return points;
    }

    private boolean isLowPoint(Point point) {
        if (point.height() == 9) {
            return false;
        }

        for (Point adjPoint : point.getAdjacentPoints()) {
            if (point.height() >= adjPoint.height()) {
                return false;
            }
        }
        return true;
    }

    private List<List<Point>> calculateBasins() {
        final List<List<Point>> basins = new ArrayList<>();
        for (Point point : getLowPoints()) {
            basins.add(getBasin(point));
        }
        return basins;
    }

    private void calculateAdjacentPoints() {
        for (int y = 0; y < this.matrix.size(); y++) {
            for (int x = 0; x < this.matrix.get(0).size(); x++) {
                setAdjacentPoints(this.matrix.get(y).get(x));
            }
        }
    }
    
    private void setAdjacentPoints(Point point) {
        if (point.adjacentPoints.isEmpty()) {
            if (point.x() - 1 >= 0) {
                point.adjacentPoints.add(matrix.get(point.y()).get(point.x() - 1));
            }
            if (point.x() + 1 < matrix.get(0).size()) {
                point.adjacentPoints.add(matrix.get(point.y()).get(point.x() + 1));
            }
            if (point.y() - 1 >= 0) {
                point.adjacentPoints.add(matrix.get(point.y() - 1).get(point.x()));
            }
            if (point.y() + 1 < matrix.size()) {
                point.adjacentPoints.add(matrix.get(point.y() + 1).get(point.x()));
            }
        }
    }
}

class Point {
    List<Point> adjacentPoints;
    final int height;
    final int x;
    final int y;

    public Point(int height, int x, int y) {
        this.adjacentPoints = new ArrayList<>();
        this.height = height;
        this.x = x;
        this.y = y;
    }

    public List<Point> getAdjacentPoints() {
        return this.adjacentPoints;
    }
    public int height() {
        return this.height;
    }
    public int x() {
        return this.x;
    }
    public int y() {
        return this.y;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Point(");
        sb.append(this.height);
        sb.append(",");
        sb.append(this.x);
        sb.append(",");
        sb.append(this.y);
        sb.append(")");
        return sb.toString();
    }
}