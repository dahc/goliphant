package us.dahc.goliphant.core.filters;

import us.dahc.goliphant.core.Board;
import us.dahc.goliphant.core.Color;
import us.dahc.goliphant.core.Move;

import java.util.ArrayList;
import java.util.List;

public class FilterList extends ArrayList<Filter> {

    public List<Move> apply(Board board, Color player) {
        List<Move> results = new ArrayList<>();
        for (Move move : board.getLegalMoves(player)) {
            boolean accept = true;
            for (Filter filter : this) {
                if (!filter.accept(board, move)) {
                    accept = false;
                    break;
                }
            }
            if (accept)
                results.add(move);
        }
        return results;
    }

}
