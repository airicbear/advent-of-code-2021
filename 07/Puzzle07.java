import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Puzzle07 {
    public static void main(String[] args) {
        final File file = new File(args[0]);
        try {
            final Scanner s = new Scanner(file);
            final String[] positionInput = s.nextLine().split(",");
            final int[] positions = new int[positionInput.length];
            for (int i = 0; i < positions.length; i++) {
                positions[i] = Integer.parseInt(positionInput[i]);
            }
            s.close();

            System.out.println(Long.toString(optimizedPosition(positions)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static long optimizedPosition(int[] positions) {
        final int maxPosition = max(positions);
        final List<Long> positionCosts = new ArrayList<>();
        for (int i = 0; i < maxPosition; i++) {
            long sum = 0;
            for (int j = 0; j < positions.length; j++) {
                final long cost = sumTo(Math.abs(positions[j] - i));
                sum += cost;
            }
            positionCosts.add(sum);
        }
        return min(positionCosts);
    }

    private static int sumTo(int n) {
        int sum = 0;
        for (int i = 0; i <= n; i++) {
            sum += i;
        }
        return sum;
    }

    private static long min(List<Long> list) {
        long min = Long.MAX_VALUE;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) < min) {
                min = list.get(i);
            }
        }
        return min;
    }

    private static int max(int[] array) {
        int max = 0;
        for (int i = 0; i < array.length; i++) {
            if (array[i] > max) {
                max = array[i];
            }
        }
        return max;
    }
}