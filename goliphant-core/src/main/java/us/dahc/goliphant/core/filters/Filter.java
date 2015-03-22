package us.dahc.goliphant.core.filters;

import us.dahc.goliphant.core.Board;
import us.dahc.goliphant.core.Move;

public abstract class Filter {

    public void init(Board board) {}

    public abstract boolean accept(Board board, Move move);

}
