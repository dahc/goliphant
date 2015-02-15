package us.dahc.goliphant.go;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class DefaultBoard implements Board {
    private final int rows;
    private final int columns;
    private Intersection[][] intersect;
    private Map<Intersection, Color> colors;
    private Map<Intersection, Group> groups;

    public DefaultBoard(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        intersect = new Intersection[rows][columns];
        colors = new HashMap<Intersection, Color>();
        groups = new HashMap<Intersection, Group>();
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < columns; j++)
                intersect[i][j] = new Intersection(i, j);
    }

    public DefaultBoard(DefaultBoard board) {
        rows = board.getRows();
        columns = board.getColumns();
        intersect = board.intersect;
        colors = new HashMap<Intersection, Color>(board.colors);
        groups = new HashMap<Intersection, Group>(board.groups.size());
        for (Group group : new HashSet<Group>(board.groups.values()))
            groups.put(group.getRepresentative(), new Group(group));
        for (Intersection stone : colors.keySet())
            groups.put(stone, groups.get(board.groups.get(stone).getRepresentative()));
    }

    public void fastPlay(Move move) {
        // TODO: (simple) ko flow should be in here somewhere
        Intersection stone = intersect[move.getRow()][move.getColumn()];
        colors.put(stone, move.getColor());
        groups.put(stone, new Group(stone));
        for (Intersection neighbor : stone.getNeighbors()) {
            if (colors.containsKey(neighbor)) {
                if (colors.get(neighbor) == move.getColor())
                    groups.get(neighbor).absorbFriend(groups.get(stone));
                else
                    groups.get(neighbor).contactEnemy(groups.get(stone));
            }
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

    protected class Group {
        private int pseudoLiberties;
        private List<Intersection> members;
        private Intersection representative;

        protected Group(Intersection stone) {
            members = new ArrayList<Intersection>();
            members.add(stone);
            representative = stone;
            pseudoLiberties = 0;
            for (Intersection neighbor : stone.getNeighbors())
                if (colors.get(neighbor) == null)
                    pseudoLiberties++;
        }

        protected Group(Group group) {
            members = new ArrayList<Intersection>(group.members);
            representative = group.representative;
            pseudoLiberties = group.pseudoLiberties;
        }

        protected void absorbFriend(Group group) {
            if (this.equals(group))
                return;
            members.addAll(group.members);
            pseudoLiberties += group.pseudoLiberties;
            Intersection move = group.members.get(group.members.size() - 1);
            for (Intersection neighbor : move.getNeighbors())
                if (this.equals(groups.get(neighbor)))
                    pseudoLiberties--;
            for (Intersection member : members) {
                groups.put(member, this);
            }
        }

        protected void contactEnemy(Group group) {
            pseudoLiberties--;
            if (pseudoLiberties > 0)
                return;
            // TODO: capture stuff!
        }

        protected int getPseudoLiberties() {
            return pseudoLiberties;
        }

        protected Intersection getRepresentative() {
            return representative;
        }

        protected int getSize() {
            return members.size();
        }

        protected boolean equals(Group group) {
            return representative.equals(group.representative);
        }
    }

    protected class Intersection {
        private int row;
        private int column;
        private List<Intersection> neighbors;
        private List<Intersection> diagonals;

        protected Intersection(int row, int column) {
            this.row = row;
            this.column = column;
            initGeometry();
        }

        protected Intersection(Move move) {
            this(move.getRow(), move.getColumn());
        }

        protected List<Intersection> getNeighbors() {
            return neighbors;
        }

        protected List<Intersection> getDiagonals() {
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
