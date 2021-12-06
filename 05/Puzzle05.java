import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import javax.sound.sampled.SourceDataLine;

class Puzzle05 {
    public static void main(String[] args) {
        File file = new File(args[0]);
        HydrothermalVentScanner scanner = new HydrothermalVentScanner();
        scanner.scan(file);
        // scanner.printGrid();
        System.out.println(scanner.overlappingPoints().size());
    }
}

record Line(Point point1, Point point2) {
    int angle() {
        Point dPoint = point1.delta(point2);
        return (int) Math.round(Math.toDegrees(Math.atan2(dPoint.y(), dPoint.x())));
    }

    double slope() {
        Point delta = point1.delta(point2);
        return delta.y() / delta.x();
    }

    List<Point> points() {
        List<Point> points = new ArrayList<>();
        Point from = from();
        Point to = to();
        Point dPoint = from.delta(to);
        int[] xs = new int[dPoint.x() + 1];
        int[] ys = new int[dPoint.x() + 1];

        if (dPoint.x() == 0) {
            // Vertical line equation
            xs = new int[dPoint.y() + 1];
            ys = new int[dPoint.y() + 1];
            for (int i = 0; i <= dPoint.y(); i++) {
                int px = from.x();
                int py = from.y() + i;
                xs[i] = px;
                ys[i] = py;
            }
        } else {
            // Line equation
            xs = new int[dPoint.x() + 1];
            ys = new int[dPoint.x() + 1];
            for (int i = 0; i <= dPoint.x(); i++) {
                int px = from.x() + i;
                int py = (int) slope() * (px - point1.x()) + point1.y();
                xs[i] = px;
                ys[i] = py;
            }
        }

        // Draw line
        for (int i = 0; i < xs.length; i++) {
            points.add(new Point(xs[i], ys[i]));
        }

        return points;
    }

    Point from() {
        return fromTo()[0];
    }

    Point to() {
        return fromTo()[1];
    }

    private Point[] fromTo() {
        int fromY, toY, fromX, toX;
        if (point1().y() < point2().y()) {
            fromY = point1().y();
            toY = point2().y();
        } else {
            fromY = point2().y();
            toY = point1().y();
        }
        if (point1().x() < point2().x()) {
            fromX = point1().x();
            toX = point2().x();
        } else {
            fromX = point2().x();
            toX = point1().x();
        }
        Point from = new Point(fromX, fromY);
        Point to = new Point(toX, toY);
        return new Point[]{from, to};
    }
}

record Point(int x, int y) {
    double magnitude() {
        return Math.sqrt((x * x) + (y * y));
    }

    int dot(Point other) {
        return x * other.x() + y * other.y();
    }

    Point delta(Point other) {
        return new Point(other.x() - x, other.y() - y);
    }
}

class HydrothermalVentScanner {
    private Grid grid;

    HydrothermalVentScanner() {
        this.grid = new Grid();
    }

    public void scan(File file) {
        try {
            Scanner s = new Scanner(file);
            while (s.hasNextLine()) {
                final String[] pointInputs = s.nextLine().split(" -> ");
                final String[] pointInput1 = pointInputs[0].split(",");
                final String[] pointInput2 = pointInputs[1].split(",");
                final int x1 = Integer.parseInt(pointInput1[0]);
                final int y1 = Integer.parseInt(pointInput1[1]);
                final int x2 = Integer.parseInt(pointInput2[0]);
                final int y2 = Integer.parseInt(pointInput2[1]);
                final Point p1 = new Point(x1, y1);
                final Point p2 = new Point(x2, y2);
                final Line line = new Line(p1, p2);
                if (line.angle() % 45 == 0) {
                    this.grid.markLine(line);
                }
            }
            s.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void printGrid() {
        System.out.println(this.grid);
    }

    public List<Point> overlappingPoints() {
        return this.grid.overlappingPoints();
    }

    static class Grid {
        final List<List<Integer>> grid;
        int xSize = 0;
        int ySize = 0;

        Grid() {
            this.grid = new ArrayList<>();
        }
        
        private void updateGrid(Line line) {
            updateSize(line.point1());
            updateSize(line.point2());
            while (grid.size() < ySize + 1) {
                grid.add(new ArrayList<Integer>(Collections.nCopies(xSize, 0)));
            }
            for (List<Integer> list : grid) {
                while (list.size() < xSize + 1) {
                    list.add(0);
                }
            }
        }

        public void markLine(Line line) {
            updateGrid(line);
            for (Point point : line.points()) {
                this.grid.get(point.y()).set(point.x(), this.grid.get(point.y()).get(point.x()) + 1);
            }
        }

        public List<Point> overlappingPoints() {
            List<Point> points = new ArrayList<>();
            for (int y = 0; y < this.grid.size(); y++) {
                for (int x = 0; x < this.grid.get(y).size(); x++) {
                    if (this.grid.get(y).get(x) > 1) {
                        points.add(new Point(x, y));
                    }
                }
            }
            return points;
        }

        private void updateSize(Point point) {
            if (point.x() > xSize) {
                xSize = point.x();
            }
            if (point.y() > ySize) {
                ySize = point.y();
            }
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("Size: (" + this.xSize + "," + this.ySize + ")\n");
            for (int i = 0; i < this.grid.size(); i++) {
                for (int j = 0; j < this.grid.get(i).size(); j++) {
                    if (grid.get(i).get(j) == 0) {
                        sb.append('.');
                    } else {
                        sb.append(this.grid.get(i).get(j));
                    }
                }
                sb.append('\n');
            }
            return sb.toString();
        }
    }
}