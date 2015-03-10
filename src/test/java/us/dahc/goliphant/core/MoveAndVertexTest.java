package us.dahc.goliphant.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class MoveAndVertexTest {

    @Test
    public void testMoveString1() {
        Move move = new Move("black", "a1");
        assertEquals(Color.Black, move.getColor());
        assertEquals(0, move.getColumn());
        assertEquals(0, move.getRow());
        assertEquals("Black A1", move.toString());
    }

    @Test
    public void testMoveString2() {
        Move move = new Move("WHITE", "C2");
        assertEquals(Color.White, move.getColor());
        assertEquals(2, move.getColumn());
        assertEquals(1, move.getRow());
        assertEquals("White C2", move.toString());
    }

    @Test
    public void testMoveEquality() {
        Move move = new Move("Black", "a1");
        assertTrue(move.equals(new Move("black", "A1")));
        assertFalse(move.equals(new Move("white", "A1")));
        assertFalse(move.equals(new Move("black", "A2")));
        assertFalse(move.equals(new Move("black", "B1")));
    }

    @Test
    public void testPassVertex() {
        Move move = new Move("Black", "PASS");
        assertEquals(move.getVertex(), Vertex.PASS);
    }
}
