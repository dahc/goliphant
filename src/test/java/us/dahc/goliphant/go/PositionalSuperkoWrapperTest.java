package us.dahc.goliphant.go;

import org.junit.Before;
import org.junit.Test;
import us.dahc.goliphant.util.ZobristTable;

import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PositionalSuperkoWrapperTest {

    private Board board;

    @Before
    public void setup() {
        board = new PositionalSuperkoWrapper(new DefaultBoard(new ZobristTable(new Random())));
    }

    @Test
    public void testWithoutSuperKo() {
        assertEquals(board.getRows() * board.getColumns() + 1, board.getLegalMoveVertices(Color.Black).size());
    }

    @Test
    public void testSuperKo() {
        board.play(new Move(Color.Black, 0, 3));
        board.play(new Move(Color.Black, 1, 1));
        board.play(new Move(Color.Black, 1, 2));
        board.play(new Move(Color.Black, 1, 3));
        board.play(new Move(Color.White, 1, 0));
        board.play(new Move(Color.White, 0, 1)); // repeatable
        board.play(new Move(Color.White, 0, 2));
        board.play(new Move(Color.Black, 0, 0));
        assertFalse(board.isLegal(new Move(Color.White, 0, 1)));
        assertTrue(board.isLegal(new Move(Color.Black, 0, 1)));
        assertFalse(board.getLegalMoveVertices(Color.White).contains(board.getVertexAt(0, 1)));
        assertTrue(board.getLegalMoveVertices(Color.Black).contains(board.getVertexAt(0, 1)));
    }

}