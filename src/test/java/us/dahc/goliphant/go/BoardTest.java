package us.dahc.goliphant.go;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class BoardTest {
    public Board stdBoard;

    @Before
    public void setup() {
        stdBoard = new StockBoard(19, 19);
    }

    @Test
    public void testInitialState() {
        assertEquals(stdBoard.getLegalMoves().size(), 19 * 19);
        assertEquals(stdBoard.getLegalMovesIgnoringSuperKo().size(), 19 * 19);
    }

    @Test
    public void testFastPlay() {
        assertEquals(stdBoard.getColorAt(3, 3), Color.Empty);
        stdBoard.fastPlay(new Move(Color.Black, 3, 3));
        assertEquals(stdBoard.getColorAt(3, 3), Color.Black);
        assertEquals(stdBoard.getLegalMoves().size(), 19 * 19 - 1);
        assertEquals(stdBoard.getLegalMovesIgnoringSuperKo().size(), 19 * 19 - 1);
    }

    @Test
    public void testCopyAccuracy() {
        playSomeStuff(stdBoard);
        Board copy = stdBoard.independentCopy();
        for (int i = 0; i < 19; i++)
            for (int j = 0; j < 19; j++)
                assertEquals(copy.getColorAt(i, j), stdBoard.getColorAt(i, j));
    }

    @Test
    public void testCopyIndependence() {
        Board copy = stdBoard.independentCopy();
        playSomeStuff(stdBoard);
        for (int i = 0; i < 19; i++)
            for (int j = 0; j < 19; j++)
                assertEquals(copy.getColorAt(i, j), Color.Empty);
        assertEquals(copy.getLegalMoves().size(), 19 * 19);
    }

    private void playSomeStuff(Board board) {
        stdBoard.fastPlay(new Move(Color.Black, 3, 3));
        stdBoard.fastPlay(new Move(Color.White, 3, 16));
        stdBoard.fastPlay(new Move(Color.Black, 4, 17));
        stdBoard.fastPlay(new Move(Color.White, 2, 2));
        stdBoard.fastPlay(new Move(Color.Black, 15, 3));
        stdBoard.fastPlay(new Move(Color.White, 8, 7));
    }
}
