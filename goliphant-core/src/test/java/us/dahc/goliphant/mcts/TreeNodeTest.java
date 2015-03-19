package us.dahc.goliphant.mcts;

import org.junit.Before;
import org.junit.Test;
import us.dahc.goliphant.core.Color;
import us.dahc.goliphant.core.Move;
import us.dahc.goliphant.core.Vertex;

import static org.junit.Assert.assertEquals;

public class TreeNodeTest {

    private TreeNode treeNode;

    @Before
    public void setup() {
        treeNode = new TreeNode(null, Move.get(Color.Black, Vertex.PASS));
    }

    @Test
    public void testInitialState() {
        assertEquals(0, treeNode.getPendingSimulations());
        assertEquals(0, treeNode.getSimulations());
    }

    @Test
    public void testAddPendingResults() {
        treeNode.addPending(1);
        assertEquals(1, treeNode.getPendingSimulations());
        treeNode.addResults(1, 1, true);
        assertEquals(0, treeNode.getPendingSimulations());
        assertEquals(1, treeNode.getSimulations());
        assertEquals(1.0D, treeNode.getWinRatio(), 0.000000001D);
        treeNode.addPending(1);
        treeNode.addResults(1, 0, true);
        assertEquals(2, treeNode.getSimulations());
        assertEquals(0.5D, treeNode.getWinRatio(), 0.000000001D);
    }

}
