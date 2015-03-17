package us.dahc.goliphant.mcts;

import us.dahc.goliphant.core.Move;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TreeNode {

    private TreeNode parent = null;
    private List<TreeNode> children = null;
    private Move move = null;
    private int simulations = 0;
    private int pendingSimulations = 0;
    private double winRatio = 0.5D;

    /* TODO: Evaluate the initial winRatio value.
     * It will be discarded when the first results are added, but it isn't
     * obvious what's best while simulations are still pending.
     */

    public TreeNode(TreeNode parent, Move move) {
        this.parent = parent;
        this.move = move;
    }

    public void setParent(TreeNode treeNode) {
        parent = treeNode;
    }

    public void setMovesAsChildren(Collection<? extends Move> moves) {
        children = new ArrayList<>(moves.size());
        for (Move move : moves)
            children.add(new TreeNode(this, move));
    }

    public void addPending(int count) {
        pendingSimulations += count;
    }

    public void addResults(int simulations, int wins) {
        winRatio = (this.simulations * winRatio + wins) / (this.simulations + simulations);
        pendingSimulations -= simulations;
        this.simulations += simulations;
    }

    @Nullable public TreeNode getParent() {
        return parent;
    }

    @Nullable public Collection<TreeNode> getChildren() {
        return children;
    }

    @Nullable public Move getMove() {
        return move;
    }

    public int getSimulations() {
        return simulations;
    }

    public int getPendingSimulations() {
        return pendingSimulations;
    }

    public double getWinRatio() {
        return winRatio;
    }

}