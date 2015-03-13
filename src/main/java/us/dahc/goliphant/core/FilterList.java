package us.dahc.goliphant.core;

import java.util.ArrayList;
import java.util.Collection;

public class FilterList extends ArrayList<Filter> {

    public Collection<Move> apply(Board board, Color player) {
        Collection<Move> results = new ArrayList<>();
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
