import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Puzzle01 {
    public static void main(String[] args) {
        File file = new File(args[0]);
        try {
            Scanner s = new Scanner(file);
            List<Integer> measurements = new ArrayList<>();
            while (s.hasNextInt()) {
                measurements.add(s.nextInt());
            }
            s.close();
            System.out.println("Part 1: " + countIncreases(measurements));
            System.out.println("Part 2: " + countThreeMeasurement(measurements));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    private static int countIncreases(List<Integer> measurements) {
        int count = 0;
        for (int i = 1; i < measurements.size(); i++) {
            if (measurements.get(i) > measurements.get(i - 1)) {
                count++;
            }
        }
        return count;
    }

    private static int countThreeMeasurement(List<Integer> measurements) {
        int count = 0;
        for (int i = 0; i < measurements.size() - 3; i++) {
            int winA = (measurements.get(i) + measurements.get(i + 1) + measurements.get(i + 2));
            int winB = (measurements.get(i + 1) + measurements.get(i + 2) + measurements.get(i + 3));
            if (winB > winA) {
                count++;
            }
        }
        return count;
    }
}