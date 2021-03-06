package us.dahc.goliphant.core;

import us.dahc.goliphant.core.filters.EyeLikeFilter;
import us.dahc.goliphant.core.filters.Filter;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class DefaultBoard implements Board {

    private int rows = 19;
    private int columns = 19;
    private Intersection[][] intersect;
    private Map<Intersection, Color> colors;
    private Map<Intersection, Group> groups;
    private float komi = 0.0F;
    private int blackCaptures = 0;
    private int whiteCaptures = 0;
    private Move lastMove = null;
    private Vertex koVertex = null;
    private ZobristTable zobristTable;
    private long zobristHash = 0L;
    private List<Long> hashHistory;
    private int moveNumber = 0;
    private int consecutivePasses = 0;
    private Filter randomMoveFilter = new EyeLikeFilter();

    @Inject
    public DefaultBoard(ZobristTable zobristTable) {
        this.zobristTable = zobristTable;
        initializeIntersections();
        hashHistory = new ArrayList<>();
        colors = new HashMap<>();
        groups = new HashMap<>();
    }

    @Override
    public void reset() {
        blackCaptures = 0;
        whiteCaptures = 0;
        lastMove = null;
        moveNumber = 0;
        consecutivePasses = 0;
        koVertex = null;
        zobristHash = 0L;
        komi = 0.0F;
        hashHistory.clear();
        colors.clear();
        groups.clear();
    }

    @Override
    public void resize(int rows, int columns) throws InvalidSizeException {
        if (rows > MAX_ROWS || columns > MAX_COLUMNS)
            throw new InvalidSizeException("max size is " + MAX_ROWS + " rows by " + MAX_COLUMNS + " columns");
        this.rows = rows;
        this.columns = columns;
        reset();
        initializeIntersections();
    }

    @Override
    public DefaultBoard getCopy() {
        DefaultBoard board = new DefaultBoard(zobristTable);
        board.setBasics(this);
        board.copyStructures(this);
        return board;
    }

    @Override
    public void setTo(Board board) {
        setBasics(board);
        if (board instanceof DefaultBoard)
            copyStructures((DefaultBoard) board);
        else
            computeStructures(board);
    }

    @Override
    public void setKomi(float komi) {
        this.komi = komi;
    }

    @Override
    public float getKomi() {
        return komi;
    }

    @Override
    public int getStonesCapturedBy(Color player) {
        if (player == Color.Black)
            return blackCaptures;
        else
            return whiteCaptures;
    }

    @Override
    public int getMoveNumber() {
        return moveNumber;
    }

    @Override
    public int getConsecutivePasses() {
        return consecutivePasses;
    }

    @Override @Nullable
    public Move getLastMove() {
        return lastMove;
    }

    @Override @Nullable
    public Move getKoMove() {
        if (koVertex == null)
            return null;
        else
            return Move.get(lastMove.getColor().getOpponent(), koVertex.getRow(), koVertex.getColumn());
    }

    @Override
    public void play(Move move) {
        lastMove = move;
        moveNumber++;
        koVertex = null;
        if (move.getVertex().equals(Vertex.PASS)) {
            consecutivePasses++;
        } else {
            consecutivePasses = 0;
            hashHistory.add(zobristHash);
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
            zobristHash ^= zobristTable.getEntry(move.getColor(), move.getRow(), move.getColumn());
        }
    }

    @Override
    public boolean isLegal(Move move) {
        return move.getVertex().equals(Vertex.PASS)
                || isLegal(move.getColor(), intersect[move.getRow()][move.getColumn()]);
    }

    @Override
    public Move getRandomMove(Color player, Random random) {
        List<Intersection> tried = new ArrayList<>();
        while (tried.size() < rows * columns) {
            Intersection candidate = intersect[random.nextInt(rows)][random.nextInt(columns)];
            if (!tried.contains(candidate)) {
                if (isLegal(player, candidate) && randomMoveFilter.accept(this, Move.get(player, candidate)))
                    return Move.get(player, candidate);
                else
                    tried.add(candidate);
            }
        }
        return Move.get(player, Vertex.PASS);
    }

    @Override
    public List<Move> getLegalMoves(Color player) {
        List<Move> result = new ArrayList<>(rows * columns + 1);
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < columns; j++)
                if (isLegal(player, intersect[i][j]))
                    result.add(Move.get(player, i, j));
        return result;
    }

    @Override
    public List<Vertex> getAllVertices() {
        List<Vertex> result = new ArrayList<>(rows * columns);
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < columns; j++)
                result.add(intersect[i][j]);
        return result;
    }

    @Override @Nullable
    public Color getColorAt(int row, int column) {
        return colors.get(intersect[row][column]);
    }

    @Override @Nullable
    public Color getColorAt(Vertex vertex) {
        return colors.get(vertex);
    }

    @Override
    public int getRows() {
        return rows;
    }

    @Override
    public int getColumns() {
        return columns;
    }

    @Override
    public Collection<Intersection> getNeighbors(Vertex vertex) {
        if (vertex instanceof Intersection)
            return ((Intersection) vertex).getNeighbors();
        else
            return intersect[vertex.getRow()][vertex.getColumn()].getNeighbors();
    }

    @Override
    public Collection<Intersection> getDiagonals(Vertex vertex) {
        if (vertex instanceof Intersection)
            return ((Intersection) vertex).getDiagonals();
        else
            return intersect[vertex.getRow()][vertex.getColumn()].getDiagonals();
    }

    @Override
    public long getZobristHash() {
        return zobristHash;
    }

    @Override
    public List<Long> getPreviousHashes() {
        return hashHistory;
    }

    @Override
    public int hashCode() {
        return (int) zobristHash;
    }

    @Override
    public boolean equals(Object object) {
        return object instanceof Board && ((Board) object).getRows() == rows
                && ((Board) object).getColumns() == columns && zobristHash == ((Board) object).getZobristHash();
    }

    private boolean isLegal(Color color, Intersection intersection) {
        if (colors.containsKey(intersection) || intersection == koVertex)
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

    private void setBasics(Board board) {
        rows = board.getRows();
        columns = board.getColumns();
        blackCaptures = board.getStonesCapturedBy(Color.Black);
        whiteCaptures = board.getStonesCapturedBy(Color.White);
        komi = board.getKomi();
        lastMove = board.getLastMove();
        moveNumber = board.getMoveNumber();
        consecutivePasses = board.getConsecutivePasses();
        koVertex = getKoMove() == null ? null : getKoMove().getVertex();
        zobristHash = board.getZobristHash();
        hashHistory = new ArrayList<>(board.getPreviousHashes());
    }

    private void copyStructures(DefaultBoard board) {
        intersect = board.intersect;
        colors = new HashMap<>(board.colors);
        groups = new HashMap<>(board.groups.size());
        for (Group group : new HashSet<>(board.groups.values()))
            groups.put(group.getRepresentative(), new Group(group));
        for (Intersection stone : colors.keySet())
            groups.put(stone, groups.get(board.groups.get(stone).getRepresentative()));
    }

    private void computeStructures(Board board) {
        if (intersect.length != board.getRows() || intersect[0].length != board.getColumns()) {
            try {
                resize(board.getRows(), board.getColumns());
            } catch (GoliphantException e) {
                // impossible since these dimensions came from an existing board
            }
        }
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < columns; j++)
                if (board.getColorAt(i, j) != null)
                    colors.put(intersect[i][j], board.getColorAt(i, j));
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

    private void initializeIntersections() {
        intersect = new Intersection[rows][columns];
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < columns; j++)
                intersect[i][j] = new Intersection(i, j);
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < columns; j++)
                intersect[i][j].initGeometry();
    }

    protected class Group {
        private int pseudoLiberties;
        private List<Intersection> members;
        private Intersection representative;

        protected Group(Intersection stone) {
            members = new ArrayList<>();
            members.add(stone);
            representative = stone;
            pseudoLiberties = 0;
            for (Intersection neighbor : stone.getNeighbors())
                if (colors.get(neighbor) == null)
                    pseudoLiberties++;
        }

        protected Group(Group group) {
            members = new ArrayList<>(group.members);
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
                        if (captured.getOpponent().equals(colors.get(neighbor)))
                            groups.get(neighbor).pseudoLiberties++;
                }
                if (members.size() == 1 && group.members.size() == 1 && group.pseudoLiberties == 1)
                    koVertex = members.get(0);
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
            return object instanceof Group && representative.equals(((Group) object).representative);
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

    protected class Intersection extends Vertex {
        private List<Intersection> neighbors;
        private List<Intersection> diagonals;

        protected Intersection(int row, int column) {
            super(row, column);
        }

        protected void initGeometry() {
            neighbors = new ArrayList<>(4);
            if (north() != null)
                neighbors.add(north());
            if (east() != null)
                neighbors.add(east());
            if (south() != null)
                neighbors.add(south());
            if (west() != null)
                neighbors.add(west());
            diagonals = new ArrayList<>(4);
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
