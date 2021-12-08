import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.NavigableSet;
import java.util.Objects;
import java.util.Scanner;
import java.util.TreeSet;

class Puzzle08 {
    public static void main(String[] args) {
        File file = new File(args[0]);
        try {
            Scanner s = new Scanner(file);
            int sum = 0;
            while (s.hasNextLine()) {
                String[] line = s.nextLine().split("\\s+\\|\\s+");
                String[] signals = line[0].split(" ");
                String[] outputs = line[1].split(" ");
                DisplayConfiguration configuration = new DisplayConfiguration(signals, 1);
                Display display = new Display(configuration);
                Interpreter interpreter = new Interpreter(display);
                int decoded = interpreter.decode(outputs);
                int configurationVersion = 1;
                while (decoded < 0) {
                    configuration = new DisplayConfiguration(signals, ++configurationVersion);
                    display = new Display(configuration);
                    interpreter = new Interpreter(display);
                    decoded = interpreter.decode(outputs);
                }
                System.out.println(decoded);
                sum += decoded;
            }
            s.close();
            System.out.println(sum);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}

class Interpreter {
    Display display;

    Interpreter(Display display) {
        this.display = display;
    }

    int decode(String[] outputs) {
        int number = 0;
        List<Integer> decodedDigits = new ArrayList<>();
        for (String code : outputs) {
            decodedDigits.add(display.parseInt(code));
        }
        for (int i = 0; i < decodedDigits.size(); i++) {
            number += decodedDigits.get(i) * Math.pow(10, decodedDigits.size() - 1 - i);
        }
        return number;
    }
}

class Display {
    final DisplayConfiguration configuration;
    final List<Character> segmentChars;

    Display(DisplayConfiguration configuration) {
        this.configuration = configuration;
        this.segmentChars = new ArrayList<>();
        for (int i = 0; i < this.configuration.segments.size(); i++) {
            segmentChars.add(this.configuration.segments.get(i).value());
        }
    }

    public int parseInt(String code) {
        List<Integer> switches = new ArrayList<>();
        for (char c : code.toCharArray()) {
            switches.add(this.segmentChars.indexOf(c));
        }
        Collections.sort(switches);
        int[] result = switches.stream().mapToInt(Integer::intValue).toArray();
        if (Arrays.equals(result, ZERO)) {
            return 0;
        } else if (Arrays.equals(result, ONE)) {
            return 1;
        } else if (Arrays.equals(result, TWO)) {
            return 2;
        } else if (Arrays.equals(result, THREE)) {
            return 3;
        } else if (Arrays.equals(result, FOUR)) {
            return 4;
        } else if (Arrays.equals(result, FIVE)) {
            return 5;
        } else if (Arrays.equals(result, SIX)) {
            return 6;
        } else if (Arrays.equals(result, SEVEN)) {
            return 7;
        } else if (Arrays.equals(result, EIGHT)) {
            return 8;
        } else if (Arrays.equals(result, NINE)) {
            return 9;
        } else {
            return Integer.MIN_VALUE;
        }
    }

    public final static int[] ZERO = new int[]{0, 1, 2, 4, 5, 6};
    public final static int[] ONE = new int[]{2, 5};
    public final static int[] TWO = new int[]{0, 2, 3, 4, 6};
    public final static int[] THREE = new int[]{0, 2, 3, 5, 6};
    public final static int[] FOUR = new int[]{1, 2, 3, 5};
    public final static int[] FIVE = new int[]{0, 1, 3, 5, 6};
    public final static int[] SIX = new int[]{0, 1, 3, 4, 5, 6};
    public final static int[] SEVEN = new int[]{0, 2, 5};
    public final static int[] EIGHT = new int[]{0, 1, 2, 3, 4, 5, 6};
    public final static int[] NINE = new int[]{0, 1, 2, 3, 5, 6};
}

class Segment {
    NavigableSet<Character> possibleValues;

    Segment() {
        this.possibleValues = new TreeSet<>();
        possibleValues.add('a');
        possibleValues.add('b');
        possibleValues.add('c');
        possibleValues.add('d');
        possibleValues.add('e');
        possibleValues.add('f');
        possibleValues.add('g');
    }

    Character value() {
        if (possibleValues.size() == 1) {
            return possibleValues.iterator().next();
        } else {
            return 'X';
        }
    }

    @Override
    public String toString() {
        return Character.toString(value());
    }
}

class DisplayConfiguration {
    final List<Segment> segments;
    final String[] signals;
    int configuration;

    DisplayConfiguration(String[] signals, int configuration) {
        this.configuration = configuration;
        this.signals = signals;
        this.segments = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            this.segments.add(new Segment());
        }
        update(signals);
    }

    private List<Integer> positions(int number) {
        List<Integer> pos = new ArrayList<>();
        switch (number) {
            case 0 -> {
                for (int i : Display.ZERO) {
                    pos.add(i);
                }
            }
            case 1 -> {
                for (int i : Display.ONE) {
                    pos.add(i);
                }
            }
            case 2 -> {
                for (int i : Display.TWO) {
                    pos.add(i);
                }
            }
            case 3 -> {
                for (int i : Display.THREE) {
                    pos.add(i);
                }
            }
            case 4 -> {
                for (int i : Display.FOUR) {
                    pos.add(i);
                }
            }
            case 5 -> {
                for (int i : Display.FIVE) {
                    pos.add(i);
                }
            }
            case 6 -> {
                for (int i : Display.SIX) {
                    pos.add(i);
                }
            }
            case 7 -> {
                for (int i : Display.SEVEN) {
                    pos.add(i);
                }
            }
            case 8 -> {
                for (int i : Display.EIGHT) {
                    pos.add(i);
                }
            }
            case 9 -> {
                for (int i : Display.NINE) {
                    pos.add(i);
                }
            }
        }
        return pos;
    }

    public void update(String[] signals) {
        for (String signal : signals) {
            update(signal);
        }

        deduce();
    }

    private void update(String signal) {
        switch (signal.length()) {
            case 2 -> update(signal, 1);
            case 3 -> update(signal, 7);
            case 4 -> update(signal, 4);
        }
    }

    private void update(String signal, int num) {
        final List<Integer> pos = positions(num);
        for (char c : signal.toCharArray()) {
            for (int i = 0; i < 7; i++) {
                if (!pos.contains(i)) {
                    segments.get(i).possibleValues.remove(c);
                }
            }
            for (int i : pos) {
                segments.get(i).possibleValues.removeIf(x -> !signal.contains(Character.toString(x)));
            }
        }
    }

    private void deduce() {
        if (configuration % 8 == 1) {
            // LF LF LF
            segments.get(1).possibleValues.pollLast();
            segments.get(3).possibleValues.pollFirst();

            segments.get(2).possibleValues.pollLast();
            segments.get(5).possibleValues.pollFirst();

            segments.get(4).possibleValues.pollLast();
            segments.get(6).possibleValues.pollFirst();
        } else if (configuration % 8 == 2) {
            // FL FL FL
            segments.get(1).possibleValues.pollFirst();
            segments.get(3).possibleValues.pollLast();

            segments.get(2).possibleValues.pollFirst();
            segments.get(5).possibleValues.pollLast();
            
            segments.get(4).possibleValues.pollFirst();
            segments.get(6).possibleValues.pollLast();
        } else if (configuration % 8 == 3) {
            // FL LF LF
            segments.get(1).possibleValues.pollFirst();
            segments.get(3).possibleValues.pollLast();

            segments.get(2).possibleValues.pollLast();
            segments.get(5).possibleValues.pollFirst();
            
            segments.get(4).possibleValues.pollLast();
            segments.get(6).possibleValues.pollFirst();
        } else if (configuration % 8 == 4) {
            // LF FL LF
            segments.get(1).possibleValues.pollLast();
            segments.get(3).possibleValues.pollFirst();

            segments.get(2).possibleValues.pollFirst();
            segments.get(5).possibleValues.pollLast();
            
            segments.get(4).possibleValues.pollLast();
            segments.get(6).possibleValues.pollFirst();
        } else if (configuration % 8 == 5) {
            // LF LF FL
            segments.get(1).possibleValues.pollLast();
            segments.get(3).possibleValues.pollFirst();

            segments.get(2).possibleValues.pollLast();
            segments.get(5).possibleValues.pollFirst();
            
            segments.get(4).possibleValues.pollFirst();
            segments.get(6).possibleValues.pollLast();
        } else if (configuration % 8 == 6) {
            // FL FL LF
            segments.get(1).possibleValues.pollFirst();
            segments.get(3).possibleValues.pollLast();

            segments.get(2).possibleValues.pollFirst();
            segments.get(5).possibleValues.pollLast();
            
            segments.get(4).possibleValues.pollLast();
            segments.get(6).possibleValues.pollFirst();
        } else if (configuration % 8 == 7) {
            // FL LF FL
            segments.get(1).possibleValues.pollFirst();
            segments.get(3).possibleValues.pollLast();

            segments.get(2).possibleValues.pollLast();
            segments.get(5).possibleValues.pollFirst();
            
            segments.get(4).possibleValues.pollFirst();
            segments.get(6).possibleValues.pollLast();
        } else if (configuration % 8 == 0) {
            // LF FL FL
            segments.get(1).possibleValues.pollLast();
            segments.get(3).possibleValues.pollFirst();

            segments.get(2).possibleValues.pollFirst();
            segments.get(5).possibleValues.pollLast();
            
            segments.get(4).possibleValues.pollFirst();
            segments.get(6).possibleValues.pollLast();
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(' ');
        for (int i = 0; i < 4; i++) {
            sb.append(segments.get(0));
        }
        sb.append(" \n");
        for (int i = 0; i < 2; i++) {
            sb.append(segments.get(1));
            sb.append("    ");
            sb.append(segments.get(2));
            sb.append("\n");
        }
        sb.append(" ");
        for (int i = 0; i < 4; i++) {
            sb.append(segments.get(3));
        }
        sb.append(" \n");
        for (int i = 0; i < 2; i++) {
            sb.append(segments.get(4));
            sb.append("    ");
            sb.append(segments.get(5));
            sb.append("\n");
        }
        sb.append(" ");
        for (int i = 0; i < 4; i++) {
            sb.append(segments.get(6));
        }
        return sb.toString();
    }
}