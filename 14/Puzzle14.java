import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

class Puzzle14 {
    public static void main(String[] args) {
        File file = new File(args[0]);
        Polymerizer polymerizer = Polymerizer.readManual(file);
        polymerizer.takeSteps(10);
        System.out.println(polymerizer.getRange());
    }
}

class PolymerConfiguration {
    final Map<String, Character> insertionRules;

    PolymerConfiguration() {
        this.insertionRules = new HashMap<>();
    }

    public void addRule(String pair, Character element) {
        this.insertionRules.put(pair, element);
    }

    public char getRule(String pair) {
        return this.insertionRules.get(pair);
    }
}

class Polymerizer {
    final private PolymerConfiguration polymerConfiguration;
    final private Map<Character, Long> quantities;

    private String polymer;

    Polymerizer(String polymerTemplate, PolymerConfiguration configuration) {
        this.polymerConfiguration = configuration;
        this.polymer = polymerTemplate;
        this.quantities = calculateQuantities(polymer);
    }

    public static Polymerizer readManual(File file) {
        try {
            Scanner s = new Scanner(file);
            PolymerConfiguration configuration = new PolymerConfiguration();
            String polymerTemplate = s.nextLine();
            s.nextLine();
            while (s.hasNextLine()) {
                String[] rule = s.nextLine().split(" -> ");
                configuration.addRule(rule[0], rule[1].charAt(0));
            }
            s.close();
            return new Polymerizer(polymerTemplate, configuration);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public void takeSteps(int n) {
        for (int i = 0; i < n; i++) {
            takeStep();
        }
    }

    public void takeStep() {
        char[] newPolymer = new char[this.polymer.length() * 2 - 1];
        int count = 0;
        for (int i = 0; i < this.polymer.length() - 1; i++) {
            newPolymer[i + count] = this.polymer.charAt(i);
            count++;
            String pair = "" + this.polymer.charAt(i) + this.polymer.charAt(i+1);
            char insertionChar = this.polymerConfiguration.getRule(pair);
            quantities.putIfAbsent(insertionChar, 0L);
            quantities.put(insertionChar, quantities.get(insertionChar) + 1);
            newPolymer[i + count] = insertionChar;
        }
        newPolymer[newPolymer.length - 1] = this.polymer.charAt(this.polymer.length() - 1);
        this.polymer = String.valueOf(newPolymer);
    }

    public String getPolymer() {
        return this.polymer;
    }

    public long getRange() {
        return Collections.max(this.quantities.values()) - Collections.min(this.quantities.values());
    }

    private Map<Character, Long> calculateQuantities(String polymer) {
        final Map<Character, Long> map = new HashMap<>();
        for (char c : polymer.toCharArray()) {
            map.putIfAbsent(c, 0L);
            map.put(c, map.get(c) + 1);
        }
        return map;
    }
}