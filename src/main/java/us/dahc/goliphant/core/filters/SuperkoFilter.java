package us.dahc.goliphant.core.filters;

import us.dahc.goliphant.core.Board;
import us.dahc.goliphant.core.Move;

public class SuperkoFilter implements Filter {

    @Override
    public boolean accept(Board board, Move move) {
        if (board.isLegal(move)) {
            Board test = board.getCopy();
            test.play(move);
            return !test.getPreviousHashes().contains(test.getZobristHash());
        } else {
            return false;
        }
    }

}
