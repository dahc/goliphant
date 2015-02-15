package us.dahc.goliphant.go;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class DefaultBoardTest {
    public DefaultBoard stdBoard;
    public DefaultBoard asymBoard;

    @Before
    public void setup() {
        stdBoard = new DefaultBoard(19, 19);
        asymBoard = new DefaultBoard(13, 21);
    }

    @Test
    public void testSizeGetters() {
        assertEquals(19, stdBoard.getRows());
        assertEquals(19, stdBoard.getColumns());
        assertEquals(13, asymBoard.getRows());
        assertEquals(21, asymBoard.getColumns());
    }

    @Test
    public void testInitialState() {
        assertEquals(19 * 19, stdBoard.getLegalMoves(Color.Black).size());
        assertEquals(19 * 19, stdBoard.getLegalMovesIgnoringSuperKo(Color.Black).size());
    }

    @Test
    public void testFastPlay() {
        assertEquals(null, stdBoard.getColorAt(3, 3));
        stdBoard.fastPlay(new Move(Color.Black, 3, 3));
        assertEquals(Color.Black, stdBoard.getColorAt(3, 3));
        assertEquals(19 * 19 - 1, stdBoard.getLegalMoves(Color.White).size());
        assertEquals(19 * 19 - 1, stdBoard.getLegalMovesIgnoringSuperKo(Color.White).size());
    }

    @Test
    public void testCopyAccuracy() {
        playSomeStuff(stdBoard);
        Board copy = new DefaultBoard(stdBoard);
        for (int i = 0; i < 19; i++)
            for (int j = 0; j < 19; j++)
                assertEquals(stdBoard.getColorAt(i, j), copy.getColorAt(i, j));
    }

    @Test
    public void testCopyIndependence() {
        Board copy = new DefaultBoard(stdBoard);
        playSomeStuff(stdBoard);
        for (int i = 0; i < 19; i++)
            for (int j = 0; j < 19; j++)
                assertEquals(null, copy.getColorAt(i, j));
        assertEquals(19 * 19, copy.getLegalMoves(Color.Black).size());
    }

    private void playSomeStuff(Board board) {
        board.fastPlay(new Move(Color.Black, 3, 3));
        board.fastPlay(new Move(Color.White, 3, 16));
        board.fastPlay(new Move(Color.Black, 4, 17));
        board.fastPlay(new Move(Color.White, 2, 2));
        board.fastPlay(new Move(Color.Black, 15, 3));
        board.fastPlay(new Move(Color.White, 8, 7));
    }
}
