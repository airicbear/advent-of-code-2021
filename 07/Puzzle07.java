import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

class Puzzle07 {
    public static void main(String[] args) {
        File file = new File(args[0]);
        try {
            Scanner s = new Scanner(file);
            String[] crabInput = s.nextLine().split(",");
            int[] crabs = new int[crabInput.length];
            for (int i = 0; i < crabs.length; i++) {
                crabs[i] = Integer.parseInt(crabInput[i]);
            }
            s.close();

            long minSum = Integer.MAX_VALUE;
            int pos = 0;
            for (int i = 0; i < max(crabs); i++) {
                long sum = 0;
                for (int j = 0; j < crabs.length; j++) {
                    sum += Math.abs(crabs[j] - i);
                }
                if (sum < minSum) {
                    minSum = sum;
                    pos = i;
                }
            }
            System.out.println(minSum);
            System.out.println(pos);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
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