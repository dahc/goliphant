package us.dahc.goliphant.go;

import java.util.ArrayList;
import java.util.List;
import us.dahc.goliphant.go.exceptions.IllegalMoveException;

public class StockBoard implements Board {
    private final int rows;
    private final int columns;
    private Stone[][] stones;

    public StockBoard(int rows, int columns) {
        stones = new Stone[rows][columns];
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < columns; j++)
                stones[i][j] = new Stone(Color.Empty, i, j);
        this.rows = rows;
        this.columns = columns;
    }

    public StockBoard(StockBoard board) {
        this(board.getRows(), board.getColumns());
    }

    public void fastPlay(Move move) {
        stones[move.getRow()][move.getColumn()] = new Stone(move);
    }

    public void strictPlay(Move move) throws IllegalMoveException {
        stones[move.getRow()][move.getColumn()] = new Stone(move);
    }

    public List<Move> getLegalMoves() {
        return getLegalMovesIgnoringSuperKo();
    }

    public List<Move> getLegalMovesIgnoringSuperKo() {
        List<Move> result = new ArrayList<Move>(rows * columns);
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < columns; j++)
                if (stones[i][j].getColor() == Color.Empty)
                    result.add(stones[i][j]);
        return result;
    }

    public Color getColorAt(int row, int column) {
        return stones[row][column].getColor();
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    protected class Stone extends Move {
        public Stone(Color color, int row, int column) {
            super(color, row, column);
        }

        public Stone(Move move) {
            super(move.getColor(), move.getRow(), move.getColumn());
        }

        protected Stone north() {
            if (row == 0)
                return null;
            else
                return stones[row - 1][column];
        }

        protected Stone south() {
            if (row + 1 == rows)
                return null;
            else
                return stones[row + 1][column];
        }

        protected Stone east() {
            if (column + 1 == columns)
                return null;
            else
                return stones[row][column + 1];
        }

        protected Stone west() {
            if (column == 0)
                return null;
            else
                return stones[row][column - 1];
        }
    }

    protected class Group {
        private int pseudoLiberties;
        private List<Stone> stones;

        public Group(Stone stone) {
            pseudoLiberties = 0;
            stones = new ArrayList<Stone>();
            stones.add(stone);
        }

        public Group(Group group) {
            pseudoLiberties = group.pseudoLiberties;
            stones = new ArrayList<Stone>(group.stones);
        }

        public void add(Stone stone) {
            stones.add(stone);
        }

        public int getPseudoLiberties() {
            return pseudoLiberties;
        }
    }
}
