package us.dahc.goliphant.core.filters;

import us.dahc.goliphant.core.Board;
import us.dahc.goliphant.core.Move;
import us.dahc.goliphant.core.Vertex;

public class EyeLikeFilter extends Filter {

    @Override
    public boolean accept(Board board, Move move) {
        for (Vertex vertex : board.getNeighbors(move.getVertex()))
            if (board.getColorAt(vertex) != move.getColor())
                return true;
        boolean edge = board.getNeighbors(move.getVertex()).size() < 4;
        for (Vertex vertex : board.getDiagonals(move.getVertex())) {
            if (board.getColorAt(vertex) == move.getColor().getOpponent()) {
                if (edge)
                    return true;
                else
                    edge = true;
            }
        }
        return false;
    }

}
