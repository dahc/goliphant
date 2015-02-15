package us.dahc.goliphant.go;

import java.util.List;

public interface Board {
    public void fastPlay(Move move);
    public void strictPlay(Move move) throws IllegalMoveException;
    public List<Move> getLegalMoves(Color player);
    public List<Move> getLegalMovesIgnoringSuperKo(Color player);
    public Color getColorAt(int row, int column);
    public int getRows();
    public int getColumns();
}
