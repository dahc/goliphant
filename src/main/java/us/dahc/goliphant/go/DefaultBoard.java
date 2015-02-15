package us.dahc.goliphant.go;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultBoard implements Board {
    private final int rows;
    private final int columns;
    private Intersection[][] intersect;
    private Map<Intersection, Color> colors;
    private Map<Intersection, Group> groups;
    private Map<Intersection, Group> references;

    public DefaultBoard(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        intersect = new Intersection[rows][columns];
        colors = new HashMap<Intersection, Color>();
        groups = new HashMap<Intersection, Group>();
        references = new HashMap<Intersection, Group>();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                intersect[i][j] = new Intersection(i, j);
            }
        }
    }

    public DefaultBoard(DefaultBoard board) {
        rows = board.getRows();
        columns = board.getColumns();
        intersect = board.intersect;
        colors = new HashMap<Intersection, Color>(board.colors.size());
        groups = new HashMap<Intersection, Group>(board.groups.size());
        references = new HashMap<Intersection, Group>(board.references.size());
        for (Group group : board.references.values()) {
            references.put(group.getReference(), new Group(group));
        }
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (board.colors.containsKey(intersect[i][j])) {
                    colors.put(intersect[i][j], board.colors.get(intersect[i][j]));
                    groups.put(intersect[i][j], references.get(board.groups.get(intersect[i][j]).getReference()));
                }
            }
        }
    }

    public void fastPlay(Move move) {
        Intersection location = intersect[move.getRow()][move.getColumn()];
        for (Intersection neighbor : location.getNeighbors()) {
            if (colors.get(location) == colors.get(neighbor))
                touchFriend(location, neighbor);
            else
                touchEnemy(location, neighbor);
        }
    }

    public void strictPlay(Move move) throws IllegalMoveException {
        // TODO: check move validity, including superko trials
        fastPlay(move);
    }

    public List<Move> getLegalMoves(Color player) {
        // TODO: superko tracking...
        return getLegalMovesIgnoringSuperKo(player);
    }

    public List<Move> getLegalMovesIgnoringSuperKo(Color player) {
        // TODO: Actual legality checking, nevermind superko
        List<Move> result = new ArrayList<Move>(rows * columns);
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < columns; j++)
                if (!colors.containsKey(intersect[i][j]))
                    result.add(new Move(player, i, j));
        return result;
    }

    public Color getColorAt(int row, int column) {
        return colors.get(intersect[row][column]);
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    private void touchFriend(Intersection move, Intersection touched) {
        // TODO: associate groups and such
    }

    private void touchEnemy(Intersection move, Intersection touched) {
        // TODO: handle potential captures
    }

    public class Group {
        private int pseudoLiberties;
        private List<Intersection> stones;
        private Intersection reference;

        public Group(Intersection location) {
            pseudoLiberties = 0;
            stones = new ArrayList<Intersection>();
            stones.add(location);
            reference = location;
        }

        public Group(Group group) {
            pseudoLiberties = group.pseudoLiberties;
            stones = new ArrayList<Intersection>(group.stones);
            reference = group.reference;
        }

        public void add(Intersection location) {
            stones.add(location);
        }

        public int getPseudoLiberties() {
            return pseudoLiberties;
        }

        public Intersection getReference() {
            return reference;
        }

        public boolean equals(Group group) {
            return reference.equals(group.reference);
        }
    }

    public class Intersection {
        private int row;
        private int column;
        private List<Intersection> neighbors;
        private List<Intersection> diagonals;

        public Intersection(int row, int column) {
            this.row = row;
            this.column = column;
            initGeometry();
        }

        public Intersection(Move move) {
            this(move.getRow(), move.getColumn());
        }

        public List<Intersection> getNeighbors() {
            return neighbors;
        }

        public List<Intersection> getDiagonals() {
            return diagonals;
        }

        private void initGeometry() {
            neighbors = new ArrayList<Intersection>(4);
            if (north() != null)
                neighbors.add(north());
            if (east() != null)
                neighbors.add(east());
            if (south() != null)
                neighbors.add(south());
            if (west() != null)
                neighbors.add(west());
            diagonals = new ArrayList<Intersection>(4);
            if (northeast() != null)
                diagonals.add(northeast());
            if (southeast() != null)
                diagonals.add(southeast());
            if (southwest() != null)
                diagonals.add(southwest());
            if (northwest() != null)
                diagonals.add(northwest());
        }

        private Intersection north() {
            if (row == 0)
                return null;
            else
                return intersect[row - 1][column];
        }

        private Intersection south() {
            if (row + 1 == rows)
                return null;
            else
                return intersect[row + 1][column];
        }

        private Intersection east() {
            if (column + 1 == columns)
                return null;
            else
                return intersect[row][column + 1];
        }

        private Intersection west() {
            if (column == 0)
                return null;
            else
                return intersect[row][column - 1];
        }

        private Intersection northeast() {
            if (column + 1 == columns || row == 0)
                return null;
            else
                return intersect[row - 1][column + 1];
        }

        private Intersection southeast() {
            if (column + 1 == columns || row + 1 == rows)
                return null;
            else
                return intersect[row + 1][column + 1];
        }

        private Intersection southwest() {
            if (column == 0 || row + 1 == rows)
                return null;
            else
                return intersect[row + 1][column - 1];
        }

        private Intersection northwest() {
            if (column == 0 || row == 0)
                return null;
            else
                return intersect[row - 1][column - 1];
        }
    }
}
