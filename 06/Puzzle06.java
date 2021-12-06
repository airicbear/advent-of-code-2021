import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Puzzle06 {
    public static void main(String[] args) {
        File file = new File(args[0]);
        try {
            Scanner s = new Scanner(file);
            String[] timersInput = s.nextLine().split(",");
            int[] timers = new int[timersInput.length];
            for (int i = 0; i < timers.length; i++) {
                timers[i] = Integer.parseInt(timersInput[i]);
            }
            School school = new School(timers);
            school.grow(80);
            System.out.println(school.size());
            s.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}

class School {
    List<Lanternfish> fish;
    List<Lanternfish> newFish;

    School(int[] timers) {
        this.fish = new ArrayList<>();
        this.newFish = new ArrayList<>();
        for (int i = 0; i < timers.length; i++) {
            this.fish.add(new Lanternfish(timers[i]));
        }
    }

    public void addFish(Lanternfish fish) {
        this.fish.add(fish);
    }

    public List<Lanternfish> getFish() {
        return this.fish;
    }

    public int size() {
        return this.fish.size();
    }

    public void grow(int numDays) {
        for (int i = 0; i < numDays; i++) {
            grow();
        }
    }

    public void grow() {
        this.fish.addAll(this.newFish);
        this.newFish.clear();
        for (Lanternfish fish : this.fish) {
            if (fish.isWaiting()) {
                fish.stopWaiting();
            } else {
                fish.tick();
                if (fish.isZero()) {
                    newFish.add(new Lanternfish());
                }
            }
        }
    }
}

class Lanternfish {
    int timer;
    boolean waiting;

    Lanternfish() {
        this.timer = 8;
        this.waiting = true;
    }

    Lanternfish(int timer) {
        this.timer = timer;
        this.waiting = false;
    }

    boolean isWaiting() {
        return this.waiting; 
    }

    void stopWaiting() {
        this.waiting = false;
    }

    void tick() {
        if (timer > 0) {
            timer--;
        } else {
            timer = 6;
        }
    }

    boolean isZero() {
        return this.timer == 0;
    }

    @Override
    public String toString() {
        return Integer.toString(this.timer);
    }
}
