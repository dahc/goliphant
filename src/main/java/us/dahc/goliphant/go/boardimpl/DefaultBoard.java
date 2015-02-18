package us.dahc.goliphant.go.boardimpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import us.dahc.goliphant.go.Board;
import us.dahc.goliphant.go.Color;
import us.dahc.goliphant.go.Move;
import us.dahc.goliphant.go.hashing.ZobristTable;

public class DefaultBoard implements Board {

    private final int rows;

    private final int columns;

    private Intersection[][] intersect;

    private Map<Intersection, Color> colors;

    private Map<Intersection, Group> groups;

    private int blackCaptures = 0;

    private int whiteCaptures = 0;

    private Intersection koIntersection = null;

    private ZobristTable zobristTable;

    private long zobristHash = 0L;

    public DefaultBoard(ZobristTable zobristTable) {
        rows = zobristTable.getRows();
        columns = zobristTable.getColumns();
        intersect = new Intersection[rows][columns];
        colors = new HashMap<Intersection, Color>();
        groups = new HashMap<Intersection, Group>();
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < columns; j++)
                intersect[i][j] = new Intersection(i, j);
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < columns; j++)
                intersect[i][j].initGeometry();
        this.zobristTable = zobristTable;
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
        blackCaptures = board.blackCaptures;
        whiteCaptures = board.whiteCaptures;
        koIntersection = board.koIntersection;
        zobristTable = board.zobristTable;
        zobristHash = board.zobristHash;
    }

    public int getStonesCapturedBy(Color player) {
        if (player == Color.Black)
            return blackCaptures;
        else
            return whiteCaptures;
    }

    public void play(Move move) {
        Intersection stone = intersect[move.getRow()][move.getColumn()];
        colors.put(stone, move.getColor());
        groups.put(stone, new Group(stone));
        zobristHash ^= zobristTable.getEntry(move.getColor(), move.getRow(), move.getColumn());
        koIntersection = null;
        for (Intersection neighbor : stone.getNeighbors()) {
            if (colors.containsKey(neighbor)) {
                if (colors.get(neighbor) == move.getColor())
                    groups.get(neighbor).absorbFriend(groups.get(stone));
                else
                    groups.get(neighbor).contactEnemy(groups.get(stone));
            }
        }
    }

    public boolean isLegal(Move move) {
        return isLegal(move.getColor(), intersect[move.getRow()][move.getColumn()]);
    }

    public List<Move> getLegalMoves(Color player) {
        List<Move> result = new ArrayList<Move>(rows * columns);
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < columns; j++)
                if (isLegal(player, intersect[i][j]))
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

    public long getZobristHash() {
        return zobristHash;
    }

    private boolean isLegal(Color color, Intersection intersection) {
        if (colors.containsKey(intersection) || intersection == koIntersection)
            return false;
        List<Intersection> neighbors = intersection.getNeighbors();
        for (Intersection neighbor : neighbors)
            if (!colors.containsKey(neighbor))
                return true;
        int[] affectedPseudoLiberties = new int[4];
        Group[] neighboringGroups = new Group[4];
        for (int i = 0; i < neighbors.size(); i++)
            neighboringGroups[i] = groups.get(neighbors.get(i));
        for (int i = 0; i < neighbors.size(); i++) {
            affectedPseudoLiberties[i] = 0;
            for (int j = 0; j < neighbors.size(); j++)
                if (neighboringGroups[i] == neighboringGroups[j])
                    affectedPseudoLiberties[i]++;
        }
        for (int i = 0; i < neighbors.size(); i++) {
            if (colors.get(neighbors.get(i)) == color
                    && groups.get(neighbors.get(i)).getPseudoLiberties() > affectedPseudoLiberties[i])
                return true;
            if (colors.get(neighbors.get(i)) == color.getOpponent()
                    && groups.get(neighbors.get(i)).getPseudoLiberties() == affectedPseudoLiberties[i])
                return true;
        }
        return false;
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
            if (pseudoLiberties == 0) {
                for (Intersection member : members) {
                    groups.remove(member);
                    Color captured = colors.remove(member);
                    zobristHash ^= zobristTable.getEntry(captured, member.getRow(), member.getColumn());
                    if (captured == Color.White)
                        blackCaptures++;
                    else
                        whiteCaptures++;
                    for (Intersection neighbor : member.getNeighbors())
                        groups.get(neighbor).pseudoLiberties++;
                }
                if (members.size() == 1 && group.members.size() == 1 && group.pseudoLiberties == 1)
                    koIntersection = members.get(0);
            }
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
        private final int row;
        private final int column;
        private List<Intersection> neighbors;
        private List<Intersection> diagonals;

        protected Intersection(int row, int column) {
            this.row = row;
            this.column = column;
        }

        protected void initGeometry() {
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

        protected List<Intersection> getNeighbors() {
            return neighbors;
        }

        protected List<Intersection> getDiagonals() {
            return diagonals;
        }

        protected int getRow() {
            return row;
        }

        protected int getColumn() {
            return column;
        }

        protected boolean equals(Intersection intersection) {
            return row == intersection.row && column == intersection.column;
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
