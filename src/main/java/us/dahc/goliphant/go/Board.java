package us.dahc.goliphant.go;

import java.util.List;

public interface Board {
    // basic info
    public int getRows();
    public int getColumns();
    public Color getColorAt(int row, int column);
    public int getStonesCapturedBy(Color player);

    // legality
    public boolean isLegal(Move move);
    public List<Move> getLegalMoves(Color player);
    public List<Move> getLegalMovesIgnoringSuperKo(Color player);

    // play
    public void fastPlay(Move move);
    public void strictPlay(Move move) throws IllegalMoveException;
}
