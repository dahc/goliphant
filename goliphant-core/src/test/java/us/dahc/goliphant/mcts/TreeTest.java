package us.dahc.goliphant.mcts;

import org.junit.Before;
import org.junit.Test;
import us.dahc.goliphant.core.Board;
import us.dahc.goliphant.core.Color;
import us.dahc.goliphant.core.DefaultBoard;
import us.dahc.goliphant.core.ZobristTable;

import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class TreeTest {

    private Tree tree;

    @Before
    public void setup() {
        Board board = new DefaultBoard(new ZobristTable(new Random()));
        tree = new Tree(board, new UcbPolicy(2));
    }

    @Test
    public void testInitialState() {
        assertNotNull(tree.getPolicy());
        assertNull(tree.getRootNode().getChildren());
        assertEquals(0L, tree.getRootBoard().getZobristHash());
        assertEquals(0, tree.getRootNode().getSimulations() + tree.getRootNode().getPendingSimulations());
    }

    @Test
    public void testSetRootNode() {
        tree.getRootNode().setMovesAsChildren(tree.getRootBoard().getLegalMoves(Color.Black));
        TreeNode newRoot = tree.getPolicy().select(tree, tree.getRootNode().getChildren());
        assertEquals(tree.getRootNode(), newRoot.getParent());
        tree.setRootNode(newRoot);
        assertNull(newRoot.getParent());
        assertEquals(newRoot, tree.getRootNode());
        assertEquals(tree.getRootNode().getMove(), tree.getRootBoard().getLastMove());
    }

}
