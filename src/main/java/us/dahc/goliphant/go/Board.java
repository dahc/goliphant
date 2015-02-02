package us.dahc.goliphant.go;

import java.util.List;
import us.dahc.goliphant.go.exceptions.IllegalMoveException;

public interface Board {
    public void fastPlay(Move move);
    public void strictPlay(Move move) throws IllegalMoveException;
    public List<Move> getLegalMoves();
    public List<Move> getLegalMovesIgnoringSuperKo();
    public Color getColorAt(int row, int column);
    public int getRows();
    public int getColumns();
}
