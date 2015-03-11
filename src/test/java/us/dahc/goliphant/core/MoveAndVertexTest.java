package us.dahc.goliphant.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class MoveAndVertexTest {

    @Test
    public void testMoveString1() {
        Move move = Move.get("black", "a1");
        assertEquals(Color.Black, move.getColor());
        assertEquals(0, move.getColumn());
        assertEquals(0, move.getRow());
        assertEquals("Black A1", move.toString());
    }

    @Test
    public void testMoveString2() {
        Move move = Move.get("WHITE", "C2");
        assertEquals(Color.White, move.getColor());
        assertEquals(2, move.getColumn());
        assertEquals(1, move.getRow());
        assertEquals("White C2", move.toString());
    }

    @Test
    public void testMoveEquality() {
        Move move = Move.get("Black", "a1");
        assertTrue(move.equals(Move.get("black", "A1")));
        assertFalse(move.equals(Move.get("white", "A1")));
        assertFalse(move.equals(Move.get("black", "A2")));
        assertFalse(move.equals(Move.get("black", "B1")));
    }

    @Test
    public void testPassVertex() {
        Move move = Move.get("Black", "PASS");
        assertEquals(move.getVertex(), Vertex.PASS);
    }
}
