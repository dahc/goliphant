package us.dahc.goliphant.sgf;

import org.junit.Before;
import org.junit.Test;
import us.dahc.goliphant.go.DefaultBoard;
import us.dahc.goliphant.util.ZobristTable;

import java.util.Random;

import static org.junit.Assert.assertEquals;

public class SgfParserTest {

    SgfParser sgfParser;

    @Before
    public void setup() {
        sgfParser = new SgfParser(new DefaultBoard(new ZobristTable(new Random())));
    }

    @Test
    public void testGameTreeParseEmpty() throws SgfException {
        SgfGameTree gameTree = sgfParser.parse("()".getBytes());
        assertEquals(0, gameTree.getNodes().size());
        assertEquals(0, gameTree.getChildGameTrees().size());
    }

    @Test
    public void testGameTreeParseNestedEmpty() throws SgfException {
        SgfGameTree gameTree = sgfParser.parse("(())".getBytes());
        assertEquals(0, gameTree.getNodes().size());
        assertEquals(1, gameTree.getChildGameTrees().size());
    }

    @Test
    public void testGameTreeParseNonEmpty() throws SgfException {
        SgfGameTree gameTree = sgfParser.parse("(;SZ[19];B[dd];W[ss](;B[sd]C[inner tree]))".getBytes());
        assertEquals(3, gameTree.getNodes().size());
        assertEquals(1, gameTree.getChildGameTrees().size());
    }

    @Test
    public void testKomiProperty() {

    }

}
