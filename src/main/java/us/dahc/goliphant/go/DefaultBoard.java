package us.dahc.goliphant.go;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;
import javax.inject.Inject;

import us.dahc.goliphant.util.hashing.ZobristTable;

public class DefaultBoard implements Board {

    private final int rows;
    private final int columns;
    private Intersection[][] intersect;
    private Map<Intersection, Color> colors;
    private Map<Intersection, Group> groups;
    private int blackCaptures = 0;
    private int whiteCaptures = 0;
    private Move lastMove = null;
    private Intersection koIntersection = null;
    private ZobristTable zobristTable;
    private long zobristHash = 0L;
    private List<Long> hashHistory;

    @Inject
    protected DefaultBoard(ZobristTable zobristTable) {
        this.zobristTable = zobristTable;
        rows = zobristTable.getRows();
        columns = zobristTable.getColumns();
        initializeNewStructures();
    }

    protected DefaultBoard(DefaultBoard board) {
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
        lastMove = board.lastMove;
        koIntersection = board.koIntersection;
        zobristTable = board.zobristTable;
        zobristHash = board.zobristHash;
    }

    protected DefaultBoard(ZobristTable zobristTable, Board board) {
        this.zobristTable = zobristTable;
        rows = board.getRows();
        columns = board.getColumns();
        blackCaptures = board.getStonesCapturedBy(Color.Black);
        whiteCaptures = board.getStonesCapturedBy(Color.White);
        lastMove = board.getLastMove();
        if (board.getKoMove() != null)
            koIntersection = intersect[board.getKoMove().getRow()][board.getKoMove().getColumn()];
        initializeNewStructures();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (board.getColorAt(i, j) != null) {
                    colors.put(intersect[i][j], board.getColorAt(i, j));
                    zobristHash ^= zobristTable.getEntry(board.getColorAt(i, j), i, j);
                }
            }
        }
        computeGroups();
    }

    public DefaultBoard getCopy() {
        return new DefaultBoard(this);
    }

    public DefaultBoard getCopy(Board board) {
        if (board instanceof DefaultBoard)
            return new DefaultBoard((DefaultBoard) board);
        else
            return new DefaultBoard(zobristTable, board);
    }

    public int getStonesCapturedBy(Color player) {
        if (player == Color.Black)
            return blackCaptures;
        else
            return whiteCaptures;
    }

    @Nullable
    public Move getLastMove() {
        return lastMove;
    }

    @Nullable
    public Move getKoMove() {
        if (koIntersection == null)
            return null;
        else
            return new Move(lastMove.getColor().getOpponent(), koIntersection.getRow(), koIntersection.getColumn());
    }

    public void play(Move move) {
        hashHistory.add(zobristHash);
        Intersection stone = intersect[move.getRow()][move.getColumn()];
        colors.put(stone, move.getColor());
        groups.put(stone, new Group(stone));
        koIntersection = null;
        for (Intersection neighbor : stone.getNeighbors()) {
            if (colors.containsKey(neighbor)) {
                if (colors.get(neighbor) == move.getColor())
                    groups.get(neighbor).absorbFriend(groups.get(stone));
                else
                    groups.get(neighbor).contactEnemy(groups.get(stone));
            }
        }
        zobristHash ^= zobristTable.getEntry(move.getColor(), move.getRow(), move.getColumn());
        lastMove = move;
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

    @Nullable
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

    public List<Long> getPreviousHashes() {
        return hashHistory;
    }

    @Override
    public int hashCode() {
        return (int) zobristHash;
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof Board)
            return zobristHash == ((Board) object).getZobristHash();
        else
            return false;
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

    private void initializeNewStructures() {
        intersect = new Intersection[rows][columns];
        colors = new HashMap<Intersection, Color>();
        groups = new HashMap<Intersection, Group>();
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < columns; j++)
                intersect[i][j] = new Intersection(i, j);
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < columns; j++)
                intersect[i][j].initGeometry();
        hashHistory = new ArrayList<Long>();
    }

    private void computeGroups() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (!groups.containsKey(intersect[i][j])) {
                    Group group = new Group(intersect[i][j]);
                    groups.put(intersect[i][j], group);
                    group.searchNeighbors(intersect[i][j]);
                    group.reComputePseudoLiberties();
                }
            }
        }
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


        @Override
        public boolean equals(Object object) {
            if (object instanceof Group)
                return representative.equals(((Group) object).representative);
            else
                return false;
        }

        @Override
        public int hashCode() {
            return representative.hashCode();
        }

        void searchNeighbors(Intersection current) {
            for (Intersection neighbor : current.getNeighbors()) {
                if (colors.get(neighbor) == colors.get(representative) && !members.contains(neighbor)) {
                    members.add(neighbor);
                    searchNeighbors(neighbor);
                }
            }
        }

        void reComputePseudoLiberties() {
            pseudoLiberties = 0;
            for (Intersection member : members)
                for (Intersection neighbor : member.getNeighbors())
                    if (colors.get(neighbor) != colors.get(member))
                        pseudoLiberties++;
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

        @Override
        public boolean equals(Object object) {
            if (object instanceof Intersection)
                return row == ((Intersection) object).row && column == ((Intersection) object).column;
            else
                return false;
        }

        @Override
        public int hashCode() {
            return (row << 16) + column;
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
