import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Puzzle11 {
    public static void main(String[] args) {
        File file = new File(args[0]);
        OctopusGrid octopusGrid = OctopusGrid.read(file);
        while (octopusGrid.getFirstStepAllFlash() == -1) {
            octopusGrid.takeStep();
        }
        System.out.println(octopusGrid.getNumFlashes());
        System.out.println(octopusGrid.getFirstStepAllFlash());
    }
}

class Octopus {
    private List<Octopus> adjacentOctopi;
    private int energyLevel;
    private int row;
    private int column;
    private boolean flashed;

    Octopus(int energyLevel, int row, int column) {
        this.adjacentOctopi = new ArrayList<>();
        this.energyLevel = energyLevel;
        this.row = row;
        this.column = column;
        this.flashed = false;
    }

    public boolean isFlashed() {
        return this.flashed;
    }

    public void setFlashed(boolean flashed) {
        this.flashed = flashed;
    }

    public List<Octopus> getAdjacentOctopi() {
        return adjacentOctopi;
    }

    public void setAdjacentOctopi(List<Octopus> adjacentOctopi) {
        this.adjacentOctopi = adjacentOctopi;
    }

    public int getEnergyLevel() {
        return energyLevel;
    }

    public void setEnergyLevel(int energyLevel) {
        this.energyLevel = energyLevel;
    }

    public int getRow() {
        return row;
    }
    
    public int getColumn() {
        return column;
    }

    public void increaseEnergyLevel() {
        this.energyLevel++;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Octopus(energyLevel=");
        sb.append(this.energyLevel);
        sb.append(",row=");
        sb.append(this.row);
        sb.append(",column=");
        sb.append(this.column);
        sb.append(",flashed=");
        sb.append(this.flashed);
        sb.append(")");
        return sb.toString();
    }
}

class OctopusGrid {
    List<List<Octopus>> octopi;
    int numFlashes;
    int step;
    int firstStepAllFlash;
    
    OctopusGrid(List<List<Octopus>> octopi) {
        this.octopi = octopi;
        this.step = 0;
        this.firstStepAllFlash = -1;
        this.numFlashes = 0;
        for (List<Octopus> row : octopi) {
            for (Octopus octopus : row) {
                calculateAdjacentOctopi(octopus);
            }
        }
    }

    public Octopus getOctopus(int row, int col) {
        return this.octopi.get(row).get(col);
    }

    public int getNumFlashes() {
        return this.numFlashes;
    }

    public static OctopusGrid read(File file) {
        try {
            Scanner s = new Scanner(file);
            List<List<Octopus>> octopi = new ArrayList<>();
            int row = 0;
            while (s.hasNextLine()) {
                String[] rowInput = s.nextLine().split("");
                List<Octopus> octopusRow = new ArrayList<>();
                for (int col = 0; col < rowInput.length; col++) {
                    int energy = Integer.parseInt(rowInput[col]);
                    octopusRow.add(new Octopus(energy, row, col));
                }
                octopi.add(octopusRow);
                row++;
            }
            s.close();

            return new OctopusGrid(octopi);
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
        this.step++;
        resetFlash();
        increaseAll();
        flashOctopi();
        if (isAllFlashed()) {
            this.firstStepAllFlash = this.step;
        }
        setFlashedToZero();
    }

    public int getFirstStepAllFlash() {
        return this.firstStepAllFlash;
    }

    private boolean isAllFlashed() {
        for (int row = 0; row < this.octopi.size(); row++) {
            for (int col = 0; col < this.octopi.get(0).size(); col++) {
                if (!getOctopus(row, col).isFlashed()) {
                    return false;
                }
            }
        }
        return true;
    }

    private void setFlashedToZero() {
        for (int row = 0; row < this.octopi.size(); row++) {
            for (int col = 0; col < this.octopi.get(0).size(); col++) {
                if (getOctopus(row, col).isFlashed()) {
                    getOctopus(row, col).setEnergyLevel(0);
                }
            }
        }
    }

    private void flashOctopi() {
        for (int row = 0; row < this.octopi.size(); row++) {
            for (int col = 0; col < this.octopi.get(0).size(); col++) {
                if (getOctopus(row, col).getEnergyLevel() > 9) {
                    flash(getOctopus(row, col));
                }
            }
        }
    }

    private void increaseAll() {
        for (int row = 0; row < this.octopi.size(); row++) {
            for (int col = 0; col < this.octopi.get(0).size(); col++) {
                getOctopus(row, col).increaseEnergyLevel();
            }
        }
    }

    private void resetFlash() {
        for (int row = 0; row < this.octopi.size(); row++) {
            for (int col = 0; col < this.octopi.get(0).size(); col++) {
                getOctopus(row, col).setFlashed(false);
            }
        }
    }

    private void flash(Octopus octopus) {
        this.numFlashes++;
        octopus.setEnergyLevel(0);
        octopus.setFlashed(true);
        for (Octopus adjOctopus : octopus.getAdjacentOctopi()) {
            adjOctopus.increaseEnergyLevel();
            if (adjOctopus.getEnergyLevel() > 9 && !adjOctopus.isFlashed()) {
                adjOctopus.setEnergyLevel(0);
                flash(adjOctopus);
            }
        }
    }

    private void calculateAdjacentOctopi(Octopus octopus) {
        List<Octopus> adjacentOctopi = new ArrayList<>();
        for (int row = octopus.getRow() - 1; row <= octopus.getRow() + 1; row++) {
            for (int col = octopus.getColumn() - 1; col <= octopus.getColumn() + 1; col++) {
                if (row >= 0 && row < octopi.size() && col >= 0 && col < octopi.get(0).size() && !(row == octopus.getRow() && col == octopus.getColumn())) {
                    adjacentOctopi.add(getOctopus(row, col));
                }
            }
        }
        octopus.setAdjacentOctopi(adjacentOctopi);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (List<Octopus> row : octopi) {
            for (Octopus octopus : row) {
                sb.append(octopus.getEnergyLevel());
            }
            sb.append('\n');
        }
        return sb.toString();
    }
}