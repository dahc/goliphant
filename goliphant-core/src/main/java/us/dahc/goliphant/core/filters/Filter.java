package us.dahc.goliphant.core.filters;

import us.dahc.goliphant.core.Board;
import us.dahc.goliphant.core.Move;

public interface Filter {

    public boolean accept(Board board, Move move);

}
