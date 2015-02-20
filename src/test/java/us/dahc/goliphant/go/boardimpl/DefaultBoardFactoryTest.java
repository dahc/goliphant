package us.dahc.goliphant.go.boardimpl;

import org.junit.Before;
import org.junit.Test;
import us.dahc.goliphant.go.Board;
import us.dahc.goliphant.go.BoardFactory;
import us.dahc.goliphant.go.hashing.RandomZobristTableSource;

import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DefaultBoardFactoryTest {

    private BoardFactory boardFactory;

    @Before
    public void setup() {
        boardFactory = new DefaultBoardFactory(new RandomZobristTableSource(new Random()));
    }

    @Test
    public void createTest() {
        Board board = boardFactory.create(13, 21);
        assertTrue(board instanceof DefaultBoard);
        assertEquals(13, board.getRows());
        assertEquals(21, board.getColumns());
        assertTrue(boardFactory.create(13, 21) != board);
    }

    @Test
    public void copyTest() {
        Board board = boardFactory.create(19, 19);
        Board copy = boardFactory.copy(board);
        assertTrue(copy != board);
        assertTrue(copy.equals(board));
        assertTrue(copy instanceof DefaultBoard);
    }
}
