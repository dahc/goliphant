package us.dahc.goliphant.mctsbot.engine;

import us.dahc.goliphant.core.Board;
import us.dahc.goliphant.core.Color;
import us.dahc.goliphant.core.Move;

public interface Engine {

    public Move getMove(Board board, Color player);

}
