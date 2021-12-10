import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.Scanner;

class Puzzle10 {
    public static void main(String[] args) {
        File file = new File(args[0]);
        NavigationSubsystem navigationSubsystem = readNavigationSubsystem(file);
        System.out.println(navigationSubsystem.getTotalSyntaxErrorScore());
        System.out.println(navigationSubsystem.middleScore());
    }

    private static NavigationSubsystem readNavigationSubsystem(File file) {
        try {
            final Scanner s = new Scanner(file);
            final StringBuilder sb = new StringBuilder();
            while (s.hasNextLine()) {
                sb.append(s.nextLine());
                sb.append('\n');
            }
            s.close();
            final String input = sb.toString();
            return new NavigationSubsystem(input);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}

class NavigationSubsystem {
    final String buffer;

    NavigationSubsystem(String buffer) {
        this.buffer = buffer;
    }

    public Long middleScore() {
        final List<Long> scores = new ArrayList<>();
        for (String line : this.buffer.split("\n")) {
            if (firstIllegalCharacter(line) == 0) {
                scores.add(lineCompletionScore(restOfTheLine(line)));
            }
        }
        Collections.sort(scores);
        return scores.get(scores.size() / 2);
    }

    public int getTotalSyntaxErrorScore() {
        int score = 0;
        for (String line : this.buffer.split("\n")) {
            score += syntaxErrorScore(firstIllegalCharacter(line));
        }
        return score;
    }

    private long lineCompletionScore(String lineCompletion) {
        long score = 0;
        for (char c : lineCompletion.toCharArray()) {
            score *= 5;
            switch (c) {
                case ')' -> score += 1;
                case ']' -> score += 2;
                case '}' -> score += 3;
                case '>' -> score += 4;
            }
        }
        return score;
    }

    private String restOfTheLine(String line) {
        final Deque<Character> stack = new ArrayDeque<>();
        final StringBuilder sb = new StringBuilder();
        for (char c : line.toCharArray()) {
            if (!stack.isEmpty() && isMatching(stack.peekLast(), c)) {
                stack.pollLast();
            } else {
                stack.add(c);
            }
        }
        while (!stack.isEmpty()) {
            char closingBracket = matchingBracket(stack.pollLast());
            sb.append(closingBracket);
        }
        return sb.toString();
    }

    private char firstIllegalCharacter(String line) {
        final Deque<Character> stack = new ArrayDeque<>();
        for (char c : line.toCharArray()) {
            if (!stack.isEmpty() && isClosingBracket(c) && !isMatching(stack.peekLast(), c)) {
                return c;
            }
            if (!stack.isEmpty() && isMatching(stack.peekLast(), c)) {
                stack.pollLast();
            } else {
                stack.add(c);
            }
        }
        return 0;
    }

    private boolean isMatching(char openingBracket, char closingBracket) {
        final boolean roundBrackets = openingBracket == '(' && closingBracket == ')';
        final boolean squareBrackets = openingBracket == '[' && closingBracket == ']';
        final boolean curlyBrackets = openingBracket == '{' && closingBracket == '}';
        final boolean angledBrackets = openingBracket == '<' && closingBracket == '>';
        return roundBrackets || squareBrackets || curlyBrackets || angledBrackets;
    }

    private char matchingBracket(char openingBracket) {
        switch (openingBracket) {
            case '(': return ')';
            case '[': return ']';
            case '{': return '}';
            case '<': return '>';
            default: return 0;
        }
    }

    private boolean isClosingBracket(char bracket) {
        return bracket == ')' || bracket == ']' || bracket == '}' || bracket == '>';
    }

    private int syntaxErrorScore(char firstIllegalCharacter) {
        switch (firstIllegalCharacter) {
            case ')':
                return 3;
            case ']':
                return 57;
            case '}':
                return 1197;
            case '>':
                return 25137;
            default:
                return 0;
        }
    }
}