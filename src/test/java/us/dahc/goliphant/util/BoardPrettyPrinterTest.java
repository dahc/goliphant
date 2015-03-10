package us.dahc.goliphant.util;

import org.junit.Test;
import us.dahc.goliphant.core.Board;
import us.dahc.goliphant.core.DefaultBoard;
import us.dahc.goliphant.core.ZobristTable;

import java.util.Random;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;

public class BoardPrettyPrinterTest {

    @Test
    public void testPrettyPrintStability() throws Exception {
        Board board = new DefaultBoard(new ZobristTable(new Random()));
        String prettyBoard = BoardPrettyPrinter.getPrettyString(board);
        assertThat("actually wrote board", prettyBoard.length(), is(greaterThan(800)));
        board.resize(50, 25);
        String prettyHugeBoard = BoardPrettyPrinter.getPrettyString(board);
        assertThat("actually wrote big board", prettyHugeBoard.length(), is(greaterThan(2500)));
    }

}
