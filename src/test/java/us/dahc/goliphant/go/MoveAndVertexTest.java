package us.dahc.goliphant.go;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import us.dahc.goliphant.util.ZobristTable;

import java.util.Random;

public class MoveAndVertexTest {

    private static Board referenceBoard = new DefaultBoard(new ZobristTable(new Random()));

    @Test
    public void testMoveString1() {
        Move move = new Move("black", "a19");
        assertEquals(Color.Black, move.getColor());
        assertEquals(0, move.getColumn());
        assertEquals(0, move.getRow());
        assertEquals("Black A19", move.toString(referenceBoard));
    }

    @Test
    public void testMoveString2() {
        Move move = new Move("WHITE", "C2");
        assertEquals(Color.White, move.getColor());
        assertEquals(2, move.getColumn());
        assertEquals(17, move.getRow());
        assertEquals("White C2", move.toString(referenceBoard));
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
