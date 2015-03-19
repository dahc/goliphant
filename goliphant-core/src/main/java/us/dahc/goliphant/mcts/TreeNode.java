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
    private double winRatio = 0.0;

    public TreeNode(TreeNode parent, Move move) {
        this.parent = parent;
        this.move = move;
    }

    public synchronized void setParent(TreeNode treeNode) {
        parent = treeNode;
    }

    public synchronized void setMovesAsChildren(Collection<? extends Move> moves) {
        if (children == null) {
            children = new ArrayList<>(moves.size());
            for (Move move : moves)
                children.add(new TreeNode(this, move));
        }
    }

    public synchronized void addPending(int count) {
        pendingSimulations += count;
    }

    public synchronized void addResults(int simulations, int wins, boolean pending) {
        winRatio = (this.simulations * winRatio + wins) / (this.simulations + simulations);
        this.simulations += simulations;
        if (pending)
            pendingSimulations -= simulations;
        if (parent != null)
            parent.addResults(simulations, wins, false);
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