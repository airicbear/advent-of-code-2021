import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

class Puzzle04 {
    public static void main(String[] args) {
        File file = new File(args[0]);
        try {
            Scanner s = new Scanner(file);
            int[] numbers = readNumbers(s.nextLine());
            s.nextLine();

            List<Board> boards = new LinkedList<>();
            Board.Slot[][] nextBoard = new Board.Slot[5][5];
            int row = 0;
            int col = 0;
            while (s.hasNextInt()) {
                nextBoard[row][col] = new Board.Slot(s.nextInt());
                col++;
                if (col % 5 == 0) {
                    col = 0;
                    row++;
                }
                if (col % 5 == 0 && row % 5 == 0) {
                    boards.add(new Board(nextBoard));
                    nextBoard = new Board.Slot[5][5];
                    row = 0;
                }
            }
            s.close();

            run(boards, numbers);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void run(List<Board> boards, int[] numbers) {
        for (int i = 0; i < numbers.length; i++) {
            for (Board board : boards) {
                board.markNumber(numbers[i]);
                if (board.isWinner()) {
                    System.out.println(board.unmarkedSum() * numbers[i]);
                    return;
                }
            }
        }
    }

    private static int[] readNumbers(String line) {
        String[] numbersInput = line.split(",");
        int[] numbers = new int[numbersInput.length];
        for (int i = 0; i < numbers.length; i++) {
            numbers[i] = Integer.parseInt(numbersInput[i]);
        }
        return numbers;
    }
}

class Board {
    int size;
    Slot[][] board;

    Board() {
        this.size = 5;
        this.board = new Slot[this.size][this.size];
    }

    Board(Slot[][] board) {
        this.size = 5;
        this.board = board;
    }

    public void markNumber(int number) {
        for (int i = 0; i < this.size; i++) {
            for (int j = 0; j < this.size; j++) {
                if (this.board[i][j].value == number) {
                    this.board[i][j].mark();
                }
            }
        }
    }

    public int unmarkedSum() {
        int sum = 0;
        for (int i = 0; i < this.size; i++) {
            for (int j = 0; j < this.size; j++) {
                if (!this.board[i][j].marked) {
                    sum += this.board[i][j].value;
                }
            }
        }
        return sum;
    }

    public boolean isWinner() {
        return isRowWinner() || isColumnWinner();
    }

    private Slot[] getColumn(int index) {
        Slot[] column = new Slot[this.size];
        for (int i = 0; i < this.size; i++) {
            column[i] = this.board[i][index];
        }
        return column;
    }

    private boolean isColumnWinner() {
        for (int i = 0; i < this.size; i++) {
            if (isAllMarked(getColumn(i))) {
                return true;
            }
        }
        return false;
    }

    private boolean isRowWinner() {
        for (int i = 0; i < this.size; i++) {
            if (isAllMarked(this.board[i])) {
                return true;
            }
        }
        return false;
    }

    private boolean isAllMarked(Slot[] row) {
        for (int i = 0; i < this.size; i++) {
            if (!row[i].marked) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < this.size; i++) {
            for (int j = 0; j < this.size; j++) {
                if (this.board[i][j].marked) {
                    sb.append("[");
                }
                sb.append(this.board[i][j]);
                if (this.board[i][j].marked) {
                    sb.append("]");
                }
                sb.append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    static class Slot {
        boolean marked;
        int value;

        Slot(int value) {
            this.marked = false;
            this.value = value;
        }

        void mark() {
            this.marked = true;
        }

        @Override
        public String toString() {
            return Integer.toString(this.value);
        }
    }
}

