import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

class Puzzle03 {
    public static void main(String[] args) {
        File file = new File(args[0]);
        DiagnosticReport report = DiagnosticsInterpreter.parse(file);
        System.out.println(report);
        System.out.println("Power consumption: " + report.powerConsumption());
    }
}

record DiagnosticReport(int length, int gamma, int epsilon) {
    int powerConsumption() {
        return gamma * epsilon;
    }
}

class DiagnosticsInterpreter {
    public static DiagnosticReport parse(File file) {
        try {
            Scanner s = new Scanner(file);
            int codeLength = s.nextLine().length();
            int[] frequencies = new int[codeLength];
            int reportLength = 0;
            s.close();

            s = new Scanner(file);
            while (s.hasNextLine()) {
                Code tokens = new Code(tokenize(s.nextLine()));
                for (int i = 0; i < codeLength; i++) {
                    if (tokens.getBit(i)) {
                        frequencies[i]++;
                    }
                }
                reportLength++;
            }
            s.close();

            Code gammaCode = calculateGamma(frequencies, reportLength);
            Code epsilonCode = gammaCode.inverse();
            return new DiagnosticReport(reportLength, gammaCode.toDecimal(), epsilonCode.toDecimal());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return new DiagnosticReport(-1, 0, 0);
        }
    }

    private static Code calculateGamma(int[] frequencies, int reportLength) {
        boolean[] gammaCode = new boolean[frequencies.length];
        for (int i = 0; i < frequencies.length; i++) {
            if (frequencies[i] > reportLength / 2) {
                gammaCode[i] = true;
            } else {
                gammaCode[i] = false;
            }
        }
        return new Code(gammaCode);
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