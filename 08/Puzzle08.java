import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

class Puzzle08 {
    public static void main(String[] args) {
        File file = new File(args[0]);
        try {
            Scanner s = new Scanner(file);
            int sum = 0;
            while (s.hasNextLine()) {
                String[] line = s.nextLine().split("\\s+\\|\\s+");
                String[] outputs = line[1].split(" ");
                for (int i = 0; i < outputs.length; i++) {
                    int val = outputs[i].length();
                    if (val == 2 || val == 4 || val == 3 || val == 7) {
                        sum++;
                    }
                }
            }
            s.close();
            System.out.println(Integer.toString(sum));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}