import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Puzzle13 {
    public static void main(String[] args) {
        File file = new File(args[0]);
        TransparentPaper paper = TransparentPaper.readTransparentPaper(file);
        System.out.println(paper);
    }
}

class Point {
    private int x;
    private int y;

    Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    @Override
    public boolean equals(Object other) {
        return this.x == ((Point) other).x && this.y == ((Point) other).y;
    }
}

class TransparentPaper {
    final List<Point> points;
    int xSize;
    int ySize;

    TransparentPaper() {
        this.points = new ArrayList<>();
        this.xSize = 0;
        this.ySize = 0;
    }

    public static TransparentPaper readTransparentPaper(File file) {
        try {
            Scanner s = new Scanner(file);
            TransparentPaper paper = new TransparentPaper();
            while (s.hasNextLine()) {
                String[] input = s.nextLine().split(",");
                if (input.length < 2) {
                    break;
                }
                int x = Integer.parseInt(input[0]);
                int y = Integer.parseInt(input[1]);
                if (x > paper.xSize) {
                    paper.xSize = x;
                }
                if (y > paper.ySize) {
                    paper.ySize = y;
                }
                paper.points.add(new Point(x, y));
            }
            while (s.hasNextLine()) {
                String[] instruction = s.nextLine().split(" ");
                String[] coordinate = instruction[2].split("=");
                int value = Integer.parseInt(coordinate[1]);
                if (coordinate[0].equals("x")) {
                    paper.foldX(value);
                } else {
                    paper.foldY(value);
                }
            }
            s.close();
            return paper;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void foldY(int y) {
        this.ySize = y - 1;
        for (Point point : this.points) {
            if (point.getY() >= y) {
                point.setY(y - (point.getY() - y));
            }
        }
    }

    public void foldX(int x) {
        this.xSize = x - 1;
        for (Point point : this.points) {
            if (point.getX() >= x) {
                point.setX(x - (point.getX() - x));
            }
        }
    }

    public int numVisiblePoints() {
        int count = 0;
        for (int y = 0; y <= ySize; y++) {
            for (int x = 0; x <= xSize; x++) {
                if (points.contains(new Point(x, y))) {
                    count++;
                }
            }
        }
        return count;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int y = 0; y <= ySize; y++) {
            for (int x = 0; x <= xSize; x++) {
                if (points.contains(new Point(x, y))) {
                    sb.append('#');
                } else {
                    sb.append('.');
                }
            }
            sb.append('\n');
        }
        return sb.toString();
    }
}