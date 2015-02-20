package us.dahc.goliphant.go;

import javax.annotation.Nullable;
import java.util.List;

public interface Board {

    public int getRows();

    public int getColumns();

    public Color getColorAt(int row, int column);

    public int getStonesCapturedBy(Color player);

    public long getZobristHash();

    @Nullable
    public Move getLastMove();

    @Nullable
    public Move getKoMove();

    public boolean isLegal(Move move);

    public List<Move> getLegalMoves(Color player);

    public void play(Move move);

}
