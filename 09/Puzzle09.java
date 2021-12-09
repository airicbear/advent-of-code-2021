import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Puzzle09 {
    public static void main(String[] args) {
        File file = new File(args[0]);
        Heightmap heightmap = readHeightmap(file);
        List<Point> lowPoints = heightmap.getLowPoints();
        int lowPointRiskLevelSum = 0;
        for (Point lowPoint : lowPoints) {
            lowPointRiskLevelSum += lowPoint.riskLevel();
        }
        System.out.println(lowPointRiskLevelSum);
    }

    private static Heightmap readHeightmap(File file) {
        try {
            Scanner s = new Scanner(file);
            
            List<List<Point>> points = new ArrayList<>();
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

    public Heightmap(List<List<Point>> matrix) {
        this.matrix = matrix;
    }

    public List<Point> getLowPoints() {
        List<Point> points = new ArrayList<>();
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

        List<Point> adjPoints = getAdjacentPoints(point);
        for (Point adjPoint : adjPoints) {
            if (point.height() >= adjPoint.height()) {
                return false;
            }
        }
        return true;
    }
    
    public List<Point> getAdjacentPoints(Point point) {
        List<Point> adjPoints = new ArrayList<>();
        if (point.x() - 1 >= 0) {
            adjPoints.add(matrix.get(point.y()).get(point.x() - 1));
        }
        if (point.x() + 1 < matrix.get(0).size()) {
            adjPoints.add(matrix.get(point.y()).get(point.x() + 1));
        }
        if (point.y() - 1 >= 0) {
            adjPoints.add(matrix.get(point.y() - 1).get(point.x()));
        }
        if (point.y() + 1 < matrix.size()) {
            adjPoints.add(matrix.get(point.y() + 1).get(point.x()));
        }
        return adjPoints;
    }
}

record Point(int height, int x, int y) {
    int riskLevel() {
        return height + 1;
    }
}