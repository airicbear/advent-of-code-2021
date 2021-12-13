import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

class Puzzle12 {
    public static void main(String[] args) {
        final File file = new File(args[0]);
        final CaveGraph caveGraph = CaveGraph.readCaveGraph(file);
        System.out.println(caveGraph.getAllPaths().size());
    }
}

record Cave(String name) {
    public static Cave start() {
        return new Cave("start");
    }

    public static Cave end() {
        return new Cave("end");
    }

    public boolean isStart() {
        return this.name.equals("start");
    }

    public boolean isEnd() {
        return this.name.equals("end");
    }

    public boolean isBig() {
        return this.name.equals(this.name.toUpperCase());
    }

    public boolean isSmall() {
        return this.name.equals(this.name.toLowerCase());
    }
}

class CaveGraph {
    final Map<Cave, Set<Cave>> map;
    final List<List<Cave>> allPaths;

    private CaveGraph() {
        this.map = new HashMap<>();
        this.allPaths = new ArrayList<>();
    }

    public List<List<Cave>> getAllPaths() {
        return this.allPaths;
    }

    public static CaveGraph readCaveGraph(File file) {
        try {
            final Scanner s = new Scanner(file);
            final CaveGraph caveGraph = new CaveGraph();
            while (s.hasNextLine()) {
                final String[] caves = s.nextLine().split("-");
                final Cave caveA = new Cave(caves[0]);
                final Cave caveB = new Cave(caves[1]);
                caveGraph.map.putIfAbsent(caveA, new HashSet<>());
                caveGraph.map.putIfAbsent(caveB, new HashSet<>());
                caveGraph.map.get(caveA).add(caveB);
                caveGraph.map.get(caveB).add(caveA);
            }
            s.close();

            caveGraph.calculateAllPaths();
            return caveGraph;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void calculateAllPaths() {
        final Set<Cave> visitedCaves = new HashSet<>();
        final List<Cave> cavePath = new ArrayList<>();
        final Cave caveStart = Cave.start();
        cavePath.add(caveStart);
        calculateAllPaths(caveStart, Cave.end(), visitedCaves, cavePath);
    }

    // Inspiration from
    // https://www.geeksforgeeks.org/find-paths-given-source-destination/
    private void calculateAllPaths(Cave cave, Cave destination, Set<Cave> visitedCaves, List<Cave> cavePath) {
        if (cave.equals(destination)) {
            this.allPaths.add(cavePath);
            return;
        }

        visitedCaves.add(cave);

        for (Cave nextCave : map.get(cave)) {
            if (nextCave.isBig() || !visitedCaves.contains(nextCave)) {
                cavePath.add(nextCave);
                calculateAllPaths(nextCave, destination, visitedCaves, cavePath);
                cavePath.remove(nextCave);
            }
        }

        visitedCaves.remove(cave);
    }
}