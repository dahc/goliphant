package us.dahc.goliphant.go;

import org.junit.Test;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class MoveTest {

    @Test
    public void testMoveString1() {
        Move move = new Move("black", "a19");
        assertTrue(move.getColor() == Color.Black);
        assertTrue(move.getColumn() == 0);
        assertTrue(move.getRow() == 0);
        assertTrue(move.toString().equals("Black A19"));
    }

    @Test
    public void testMoveString2() {
        Move move = new Move("WHITE", "C2");
        assertTrue(move.getColor() == Color.White);
        assertTrue(move.getColumn() == 2);
        assertTrue(move.getRow() == 17);
        assertTrue(move.toString().equals("White C2"));
    }

    @Test
    public void testMoveEquality() {
        Move move = new Move("Black", "a1");
        assertTrue(move.equals(new Move("black", "A1")));
        assertFalse(move.equals(new Move("white", "A1")));
        assertFalse(move.equals(new Move("black", "A2")));
        assertFalse(move.equals(new Move("black", "B1")));
    }
}
