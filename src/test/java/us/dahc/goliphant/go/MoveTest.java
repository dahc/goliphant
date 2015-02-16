package us.dahc.goliphant.go;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class MoveTest {

    @Test
    public void testMoveString1() {
        Move move = new Move("black", "a19");
        assertEquals(Color.Black, move.getColor());
        assertEquals(0, move.getColumn());
        assertEquals(0, move.getRow());
        assertTrue(move.toString().equals("Black A19"));
    }

    @Test
    public void testMoveString2() {
        Move move = new Move("WHITE", "C2");
        assertEquals(Color.White, move.getColor());
        assertEquals(2, move.getColumn());
        assertEquals(17, move.getRow());
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
