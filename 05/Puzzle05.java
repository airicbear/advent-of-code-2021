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
    boolean isVertical() {
        return point1.x() == point2.x();
    }

    boolean isHorizontal() {
        return point1.y() == point2.y();
    }
}

record Point(int x, int y) {}

class HydrothermalVentScanner {
    private Grid grid;

    HydrothermalVentScanner() {
        this.grid = new Grid();
    }

    public void scan(File file) {
        try {
            Scanner s = new Scanner(file);
            while (s.hasNextLine()) {
                String[] pointInputs = s.nextLine().split(" -> ");
                String[] pointInput1 = pointInputs[0].split(",");
                String[] pointInput2 = pointInputs[1].split(",");
                int x1 = Integer.parseInt(pointInput1[0]);
                int y1 = Integer.parseInt(pointInput1[1]);
                int x2 = Integer.parseInt(pointInput2[0]);
                int y2 = Integer.parseInt(pointInput2[1]);
                Point p1 = new Point(x1, y1);
                Point p2 = new Point(x2, y2);
                Line line = new Line(p1, p2);
                if (line.isHorizontal() || line.isVertical()) {
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
        List<List<Integer>> grid;
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
            int fromY, toY, fromX, toX;
            if (line.point1().y() < line.point2().y()) {
                fromY = line.point1().y();
                toY = line.point2().y();
            } else {
                fromY = line.point2().y();
                toY = line.point1().y();
            }
            if (line.point1().x() < line.point2().x()) {
                fromX = line.point1().x();
                toX = line.point2().x();
            } else {
                fromX = line.point2().x();
                toX = line.point1().x();
            }
            Point from = new Point(fromX, fromY);
            Point to = new Point(toX, toY);
            for (int y = from.y(); y <= to.y(); y++) {
                for (int x = from.x(); x <= to.x(); x++) {
                    this.grid.get(y).set(x, this.grid.get(y).get(x) + 1);
                }
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