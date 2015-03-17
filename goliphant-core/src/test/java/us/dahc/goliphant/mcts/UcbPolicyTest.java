package us.dahc.goliphant.mcts;

import org.junit.Before;
import org.junit.Test;
import us.dahc.goliphant.core.DefaultBoard;
import us.dahc.goliphant.core.Move;
import us.dahc.goliphant.core.ZobristTable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;

public class UcbPolicyTest {

    private Tree tree;
    private UcbPolicy policy;

    @Before
    public void setup() {
        policy = new UcbPolicy(2);
        tree = new Tree(new DefaultBoard(new ZobristTable(new Random())), policy);
        tree.getRootNode().setMovesAsChildren(Arrays.asList(Move.get("B", "D4"), Move.get("B", "D3")));
    }

    @Test
    public void testUrgency() {
        List<TreeNode> children = new ArrayList<>(tree.getRootNode().getChildren());
        assertEquals(Double.POSITIVE_INFINITY, policy.urgency(tree, children.get(0)), 1D);
        children.get(0).addPending(10);
        assertEquals(children.get(1), policy.select(tree, children));
        children.get(1).addPending(20);
        assertEquals(children.get(0), policy.select(tree, children));
    }

}
