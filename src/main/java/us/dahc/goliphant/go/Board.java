package us.dahc.goliphant.go;

import javax.annotation.Nullable;
import java.util.Collection;

public interface Board {

    // Geometry
    public int getRows();
    public int getColumns();

    // Basic Status
    public int getStonesCapturedBy(Color player);
    @Nullable public Color getColorAt(int row, int column);
    @Nullable public Move getLastMove();
    @Nullable public Move getKoMove();

    // Play (semantics may vary by implementation)
    public boolean isLegal(Move move);
    public Collection<Move> getLegalMoves(Color player);
    public void play(Move move);

    // Copying
    public Board getCopy();
    public Board getCopy(Board board);

    // Superko Facilities
    public long getZobristHash();
    public Collection<Long> getPreviousHashes();

}
