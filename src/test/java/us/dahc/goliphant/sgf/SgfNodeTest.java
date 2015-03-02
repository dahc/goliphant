package us.dahc.goliphant.sgf;

import org.junit.Before;
import org.junit.Test;
import us.dahc.goliphant.go.DefaultBoard;
import us.dahc.goliphant.go.Move;
import us.dahc.goliphant.util.ZobristTable;

import java.text.ParseException;
import java.util.Random;

import static org.junit.Assert.assertEquals;

public class SgfNodeTest {

    private SgfGameTree gameTree;

    @Before
    public void setup() {
        gameTree = new SgfGameTree(new DefaultBoard(new ZobristTable(new Random())));
    }

    @Test
    public void testSingleNode() throws ParseException {
        SgfNode node = new SgfNode(gameTree);
        node.parse(";W[aa]".getBytes(), 0);
        assertEquals(new Move("White", "A19"), node.getMove());
    }

}
