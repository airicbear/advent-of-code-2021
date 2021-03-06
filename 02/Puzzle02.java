import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

class Puzzle02 {
    public static void main(String[] args) {
        File file = new File(args[0]);
        try {
            Submarine submarine = new Submarine();

            Scanner s = new Scanner(file);
            while (s.hasNextLine()) {
                String[] command = s.nextLine().split(" ");
                Direction direction = Direction.parse(command[0]);
                int distance = Integer.parseInt(command[1]);
                submarine.move(direction, distance);
            }
            s.close();

            int product = submarine.xPos * submarine.yPos;
            System.out.println("xPos * yPos = " + submarine.xPos + " * " + submarine.yPos + " = " + product);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}

enum Direction {
    forward, down, up;

    public static Direction parse(String input) throws IllegalArgumentException {
        return switch (input) {
            case "forward" -> Direction.forward;
            case "down" -> Direction.down;
            case "up" -> Direction.up;
            default -> throw new IllegalArgumentException("Unrecognized direction");
        };
    }
}

class Submarine {
    int xPos;
    int yPos;
    int aim;

    Submarine(int xPos, int yPos, int aim) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.aim = aim;
    }

    Submarine() {
        this(0, 0, 0);
    }

    public void move(Direction direction, int distance) {
        switch (direction) {
            case forward -> {
                this.xPos += distance;
                this.yPos += aim * distance;
            }
            case down -> this.aim += distance;
            case up -> this.aim -= distance;
        }
    }
}
