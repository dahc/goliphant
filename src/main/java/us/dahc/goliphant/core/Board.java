package us.dahc.goliphant.core;

import javax.annotation.Nullable;
import java.util.Collection;

public interface Board {

    public static final int MAX_ROWS = 52;          // imposed by SGF, a-zA-Z
    public static final int MAX_COLUMNS = 25;       // imposed by GTP, A-Z sans I

    // Lifecycle
    public Board getCopy();
    public Board getCopy(Board board);
    public void reset();

    // Geometry
    public int getRows();
    public int getColumns();
    public Vertex getVertexAt(int row, int column);
    public Collection<? extends Vertex> getAllVertices();
    public Collection<? extends Vertex> getNeighbors(Vertex vertex);
    public Collection<? extends Vertex> getDiagonals(Vertex vertex);
    public void resize(int rows, int columns) throws InvalidSizeException;

    // Basic Status
    public float getKomi();
    public void setKomi(float komi);
    public int getStonesCapturedBy(Color player);
    public int getConsecutivePasses();
    @Nullable public Color getColorAt(Vertex vertex);
    @Nullable public Color getColorAt(int row, int column);
    @Nullable public Move getLastMove();
    @Nullable public Move getKoMove();

    // Play (semantics may vary by implementation)
    public boolean isLegal(Move move);
    public Collection<? extends Move> getLegalMoves(Color player);
    public void play(Move move);

    // Superko Facilities
    public long getZobristHash();
    public Collection<Long> getPreviousHashes();

}
