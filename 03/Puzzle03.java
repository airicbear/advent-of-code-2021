import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

class Puzzle03 {
    public static void main(String[] args) {
        File file = new File(args[0]);
        DiagnosticsInterpreter interpreter = new DiagnosticsInterpreter(file);
        DiagnosticReport report = interpreter.getResults();
        System.out.println(report);
        System.out.println("Power consumption: " + report.powerConsumption());
        System.out.println("Life support rating: " + report.lifeSupportRating());
    }
}

record DiagnosticReport(int length, int gamma, int epsilon, int oxygenGeneratorRating, int co2ScrubberRating) {
    int powerConsumption() {
        return gamma * epsilon;
    }

    int lifeSupportRating() {
        return oxygenGeneratorRating * co2ScrubberRating;
    }
}

class DiagnosticsInterpreter {
    List<String> report;
    int codeLength;
    DiagnosticReport results;

    DiagnosticsInterpreter(File file) {
        this.report = readReport(file);
        this.codeLength = calculateCodeLength(file);
        this.results = interpretReport();
    }

    public DiagnosticReport getResults() {
        return this.results;
    }

    public int getReportLength() {
        return this.report.size();
    }

    public int getCodeLength() {
        return this.codeLength;
    }

    private static int calculateCodeLength(File file) {
        try {
            Scanner s = new Scanner(file);
            int codeLength = s.nextLine().length();
            s.close();
            return codeLength;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return 0;
        }
    }

    private DiagnosticReport interpretReport() {
        Code gammaCode = calculateGamma();
        Code epsilonCode = gammaCode.inverse();
        Code oxygenGeneratorRating = calculateOxygenGeneratorRating();
        Code co2ScrubberRating = calculateCo2ScrubberRating();
        return new DiagnosticReport(
            getReportLength(),
            gammaCode.toDecimal(),
            epsilonCode.toDecimal(),
            oxygenGeneratorRating.toDecimal(),
            co2ScrubberRating.toDecimal()
        );
    }

    private List<String> readReport(File file) {
        try {
            List<String> list = new LinkedList<>();
            Scanner s = new Scanner(file);
            while (s.hasNextLine()) {
                list.add(s.nextLine());
            }
            s.close();
            return list;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return new LinkedList<String>();
        }
    }

    private int[] calculateFrequencies(List<String> report) {
        int[] frequencies = new int[codeLength];
        for (String s : report) {
            Code tokens = new Code(tokenize(s));
            for (int i = 0; i < codeLength; i++) {
                if (tokens.getBit(i)) {
                    frequencies[i]++;
                }
            }
        }

        return frequencies;
    }

    private Code calculateGamma() {
        int[] frequencies = calculateFrequencies(this.report);
        boolean[] gammaCode = new boolean[getCodeLength()];
        for (int i = 0; i < getCodeLength(); i++) {
            if (frequencies[i] > getReportLength() / 2) {
                gammaCode[i] = true;
            } else {
                gammaCode[i] = false;
            }
        }
        return new Code(gammaCode);
    }

    private Code calculateOxygenGeneratorRating() {
        List<String> report = new LinkedList<>(this.report);
        boolean[] oxygrCode = new boolean[getCodeLength()];
        for (int i = 0; i < getCodeLength(); i++) {
            int[] frequencies = calculateFrequencies(report);
            final int finalI = i;
            if (report.size() == 1) {
                for (int j = i; j < getCodeLength(); j++) {
                    if (report.get(0).charAt(j) == '1') {
                        oxygrCode[j] = true;
                    } else {
                        oxygrCode[j] = false;
                    }
                }
                break;
            }
            if ((report.size() % 2 == 0 && frequencies[i] == report.size() / 2)
                || (frequencies[i] > report.size() / 2)) {
                oxygrCode[i] = true;
                report.removeIf(code -> code.charAt(finalI) == '0');
            } else {
                oxygrCode[i] = false;
                report.removeIf(code -> code.charAt(finalI) == '1');
            }
        }
        return new Code(oxygrCode);
    }

    private Code calculateCo2ScrubberRating() {
        List<String> report = new LinkedList<>(this.report);
        boolean[] co2srcode = new boolean[getCodeLength()];
        for (int i = 0; i < getCodeLength(); i++) {
            int[] frequencies = calculateFrequencies(report);
            final int finalI = i;
            if (report.size() == 1) {
                for (int j = i; j < getCodeLength(); j++) {
                    if (report.get(0).charAt(j) == '1') {
                        co2srcode[j] = true;
                    } else {
                        co2srcode[j] = false;
                    }
                }
                break;
            }
            if ((report.size() % 2 == 0 && frequencies[i] == report.size() / 2)
                || (frequencies[i] > report.size() / 2)) {
                co2srcode[i] = false;
                report.removeIf(code -> code.charAt(finalI) == '1');
            } else {
                co2srcode[i] = true;
                report.removeIf(code -> code.charAt(finalI) == '0');
            }
        }
        return new Code(co2srcode);
    }

    private static boolean[] tokenize(String code) {
        String[] bits = code.split("");
        boolean[] tokens = new boolean[code.length()];
        for (int i = 0; i < code.length(); i++) {
            if (bits[i].equals("1")) {
                tokens[i] = true;
            } else {
                tokens[i] = false;
            }
        }
        return tokens;
    }

    static class Code {
        private boolean[] code;

        Code(boolean[] code) {
            this.code = code;
        }

        int getLength() {
            return this.code.length;
        }

        boolean[] getCode() {
            return this.code;
        }

        boolean getBit(int i) {
            return this.code[i];
        }

        void flipBit(int i) {
            if (this.code[i]) {
                this.code[i] = false;
            } else {
                this.code[i] = true;
            }
        }

        int toDecimal() {
            int result = 0;
            for (int i = 0; i < getLength(); i++) {
                if (this.code[i]) {
                    result += Math.pow(2, (getLength() - 1) - i);
                }
            }
            return result;
        }

        Code inverse() {
            Code copy = new Code(this.code.clone());
            for (int i = 0; i < getLength(); i++) {
                copy.flipBit(i);
            }
            return copy;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < getLength(); i++) {
                if (this.code[i]) {
                    sb.append("1");
                } else {
                    sb.append("0");
                }
            }
            return sb.toString();
        }
    }
}