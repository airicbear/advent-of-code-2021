import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

class Puzzle06 {
    public static void main(String[] args) {
        final File file = new File(args[0]);
        try {
            final Scanner s = new Scanner(file);
            final String[] timersInput = s.nextLine().split(",");
            final int[] timers = new int[timersInput.length];
            for (int i = 0; i < timers.length; i++) {
                timers[i] = Integer.parseInt(timersInput[i]);
            }
            final School school = new School(timers);
            school.grow(256);
            System.out.println(school.size());
            s.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}

class School {
    final Map<Integer, Long> fish;

    School(int[] timers) {
        this.fish = new HashMap<>();
        for (int i = 0; i <= 8; i++) {
            this.fish.put(i, 0L);
        }
        for (int i = 0; i < timers.length; i++) {
            this.fish.put(timers[i], this.fish.getOrDefault(timers[i], 0L) + 1);
        }
    }

    public long size() {
        long size = 0;
        for (long fishCount : this.fish.values()) {
            size += fishCount;
        }
        return size;
    }

    public void grow(int numDays) {
        for (int i = 0; i < numDays; i++) {
            grow();
        }
    }

    public void grow() {
        long zeroCount = this.fish.get(0);
        for (int i = 0; i < this.fish.size() - 1; i++) {
            this.fish.put(i, this.fish.get(i + 1));
        }
        this.fish.put(6, this.fish.get(6) + zeroCount);
        this.fish.put(8, zeroCount);
    }
}