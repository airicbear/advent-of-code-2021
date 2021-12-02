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
            System.out.println(countIncreases(measurements));
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
}