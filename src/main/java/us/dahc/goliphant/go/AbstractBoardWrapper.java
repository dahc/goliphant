package us.dahc.goliphant.go;

import javax.annotation.Nullable;
import java.util.Collection;

public abstract class AbstractBoardWrapper implements Board {

    protected Board wrappedBoard;

    public AbstractBoardWrapper(Board board) {
        wrappedBoard = board;
    }

    @Override
    public void reset() {
        wrappedBoard.reset();
    }

    @Override
    public int getRows() {
        return wrappedBoard.getRows();
    }

    @Override
    public int getColumns() {
        return wrappedBoard.getColumns();
    }

    @Override
    public Vertex getVertexAt(int row, int column) {
        return wrappedBoard.getVertexAt(row, column);
    }

    @Override
    public Collection<? extends Vertex> getAllVertices() {
        return wrappedBoard.getAllVertices();
    }

    @Override
    public Collection<? extends Vertex> getNeighbors(Vertex vertex) {
        return wrappedBoard.getNeighbors(vertex);
    }

    @Override
    public Collection<? extends Vertex> getDiagonals(Vertex vertex) {
        return wrappedBoard.getDiagonals(vertex);
    }

    @Override
    public void resize(int rows, int columns) throws InvalidSizeException {
        wrappedBoard.resize(rows, columns);
    }

    @Override
    public float getKomi() {
        return wrappedBoard.getKomi();
    }

    @Override
    public void setKomi(float komi) {
        wrappedBoard.setKomi(komi);
    }

    @Override
    public int getStonesCapturedBy(Color player) {
        return wrappedBoard.getStonesCapturedBy(player);
    }

    @Override
    public int getConsecutivePasses() {
        return wrappedBoard.getConsecutivePasses();
    }

    @Nullable @Override
    public Color getColorAt(Vertex vertex) {
        return wrappedBoard.getColorAt(vertex);
    }

    @Nullable @Override
    public Color getColorAt(int row, int column) {
        return wrappedBoard.getColorAt(row, column);
    }

    @Nullable @Override
    public Move getLastMove() {
        return wrappedBoard.getLastMove();
    }

    @Nullable @Override
    public Move getKoMove() {
        return wrappedBoard.getKoMove();
    }

    @Override
    public boolean isLegal(Move move) {
        return wrappedBoard.isLegal(move);
    }

    @Override
    public Collection<? extends Vertex> getLegalMoveVertices(Color player) {
        return wrappedBoard.getLegalMoveVertices(player);
    }

    @Override
    public void play(Move move) {
        wrappedBoard.play(move);
    }

    @Override
    public long getZobristHash() {
        return wrappedBoard.getZobristHash();
    }

    @Override
    public Collection<Long> getPreviousHashes() {
        return wrappedBoard.getPreviousHashes();
    }

}
