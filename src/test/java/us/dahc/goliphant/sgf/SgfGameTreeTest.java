package us.dahc.goliphant.sgf;

import org.junit.Before;
import org.junit.Test;
import us.dahc.goliphant.go.DefaultBoard;
import us.dahc.goliphant.util.ZobristTable;

import java.util.Random;

import static org.junit.Assert.assertEquals;

public class SgfGameTreeTest {

    private SgfGameTree gameTree;

    @Before
    public void setup() {
        gameTree = new SgfGameTree(new DefaultBoard(new ZobristTable(new Random())));
    }

    @Test
    public void testGetRootGameTree() {
        assertEquals(gameTree, gameTree.getRootGameTree());
        SgfGameTree child = new SgfGameTree(gameTree);
        assertEquals(gameTree, child.getRootGameTree());
    }
}
