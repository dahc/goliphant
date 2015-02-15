package us.dahc.goliphant.go;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultBoard implements Board {
    private final int rows;
    private final int columns;
    private Stone[][] stones;
    private Map<Stone, Group> groups;
    private Map<Stone, Group> references;

    public DefaultBoard(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        stones = new Stone[rows][columns];
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < columns; j++)
                stones[i][j] = new Stone(Color.Empty, i, j);
        groups = new HashMap<Stone, Group>();
        references = new HashMap<Stone, Group>();
    }

    public DefaultBoard(DefaultBoard board) {
        rows = board.getRows();
        columns = board.getColumns();
        stones = new Stone[rows][columns];
        groups = new HashMap<Stone, Group>(board.groups.size());
        references = new HashMap<Stone, Group>(board.references.size());
        for (Group group : board.references.values()) {
            references.put(group.getReference(), new Group(group));
        }
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                stones[i][j] = board.stones[i][j];
                if (stones[i][j].getColor() != Color.Empty) {
                    groups.put(stones[i][j], references.get(board.groups.get(stones[i][j]).getReference()));
                }
            }
        }
    }

    public void fastPlay(Move move) {
        Stone stone = new Stone(move);
        stones[move.getRow()][move.getColumn()] = stone;
        for (Stone neighbor : stone.getNeighbors()) {
            if (neighbor.getColor() == stone.getColor())
                touchFriend(stone, neighbor);
            else
                touchEnemy(stone, neighbor);
        }
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

    private void touchFriend(Stone move, Stone touched) {
    }

    private void touchEnemy(Stone move, Stone touched) {
    }

    public class Group {
        private int pseudoLiberties;
        private List<Stone> stones;
        private Stone reference;

        public Group(Stone stone) {
            pseudoLiberties = 0;
            stones = new ArrayList<Stone>();
            stones.add(stone);
            reference = stone;
        }

        public Group(Group group) {
            pseudoLiberties = group.pseudoLiberties;
            stones = new ArrayList<Stone>(group.stones);
            reference = group.reference;
        }

        public void add(Stone stone) {
            stones.add(stone);
        }

        public int getPseudoLiberties() {
            return pseudoLiberties;
        }

        public Stone getReference() {
            return reference;
        }

        public boolean equals(Group group) {
            return reference.equals(group.reference);
        }
    }

    public class Stone extends Move {
        private List<Stone> neighbors;
        private List<Stone> diagonals;

        public Stone(Color color, int row, int column) {
            super(color, row, column);
            initGeometry();
        }

        public Stone(Move move) {
            super(move.getColor(), move.getRow(), move.getColumn());
            initGeometry();
        }

        public List<Stone> getNeighbors() {
            return neighbors;
        }

        public List<Stone> getDiagonals() {
            return diagonals;
        }

        private void initGeometry() {
            neighbors = new ArrayList<Stone>(4);
            if (north() != null)
                neighbors.add(north());
            if (east() != null)
                neighbors.add(east());
            if (south() != null)
                neighbors.add(south());
            if (west() != null)
                neighbors.add(west());
            if (northeast() != null)
                diagonals.add(northeast());
            if (southeast() != null)
                diagonals.add(southeast());
            if (southwest() != null)
                diagonals.add(southwest());
            if (northwest() != null)
                diagonals.add(northwest());
        }

        private Stone north() {
            if (row == 0)
                return null;
            else
                return stones[row - 1][column];
        }

        private Stone south() {
            if (row + 1 == rows)
                return null;
            else
                return stones[row + 1][column];
        }

        private Stone east() {
            if (column + 1 == columns)
                return null;
            else
                return stones[row][column + 1];
        }

        private Stone west() {
            if (column == 0)
                return null;
            else
                return stones[row][column - 1];
        }

        private Stone northeast() {
            if (column + 1 == columns || row == 0)
                return null;
            else
                return stones[row - 1][column + 1];
        }

        private Stone southeast() {
            if (column + 1 == columns || row + 1 == rows)
                return null;
            else
                return stones[row + 1][column + 1];
        }

        private Stone southwest() {
            if (column == 0 || row + 1 == rows)
                return null;
            else
                return stones[row + 1][column - 1];
        }

        private Stone northwest() {
            if (column == 0 || row == 0)
                return null;
            else
                return stones[row - 1][column - 1];
        }
    }
}
