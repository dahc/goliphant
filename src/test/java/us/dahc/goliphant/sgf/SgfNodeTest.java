package us.dahc.goliphant.sgf;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import us.dahc.goliphant.go.DefaultBoard;
import us.dahc.goliphant.go.Move;
import us.dahc.goliphant.util.ZobristTable;

import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class SgfNodeTest {

    private SgfGameTree gameTree;

    @Before
    public void setup() {
        gameTree = new SgfGameTree(new DefaultBoard(new ZobristTable(new Random())));
    }

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testSingleProperty() throws SgfException {
        SgfNode node = new SgfNode(gameTree);
        node.parse(";W[aa]".getBytes(), 0);
        assertEquals(new Move("White", "A19"), node.getMove());
        assertEquals(1, node.getProperties().size());
        node = new SgfNode(gameTree);
        node.parse(";B[sa]".getBytes(), 0);
        assertEquals(new Move("Black", "A1"), node.getMove());
        assertEquals(1, node.getProperties().size());
    }

    @Test
    public void testMultiProperty() throws SgfException {
        SgfNode node = new SgfNode(gameTree);
        node.parse(";C[brilliant!]W[sa];B[jk]C[not here yet]".getBytes(), 0);
        assertEquals(new Move("White", "A1"), node.getMove());
        assertEquals("brilliant!", node.getTextProperty("C"));
        assertEquals(2, node.getProperties().size());
    }

    @Test
    public void testNullMove() throws SgfException {
        SgfNode node = new SgfNode(gameTree);
        node.parse(";C[this comment has no move];B[jk]C[not here yet]".getBytes(), 0);
        assertNull(node.getMove());
        assertEquals("this comment has no move", node.getTextProperty("C"));
    }

    @Test
    public void testIncompleteProperty() throws SgfException {
        thrown.expect(SgfException.class);
        new SgfNode(gameTree).parse(";C".getBytes(), 0);
    }

    @Test
    public void testIncompletePropertyValue() throws SgfException {
        thrown.expect(SgfException.class);
        new SgfNode(gameTree).parse(";C[that was great".getBytes(), 0);
    }

}
