package us.dahc.goliphant.mcts;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

public class UcbPolicy implements Policy {

    protected double explorationFactor;

    public UcbPolicy(double explorationFactor) {
        this.explorationFactor = explorationFactor;
    }

    @Override
    public TreeNode select(final Tree tree, Collection<TreeNode> options) {
        return Collections.max(options, new Comparator<TreeNode>() {
            @Override
            public int compare(TreeNode treeNodeA, TreeNode treeNodeB) {
                double urgencyA = urgency(tree, treeNodeA);
                double urgencyB = urgency(tree, treeNodeB);
                if (urgencyA > urgencyB)
                    return 1;
                else if (urgencyA < urgencyB)
                    return -1;
                else
                    return 0;
            }
        });
    }

    protected double urgency(Tree tree, TreeNode treeNode) {
        double simLocal = treeNode.getSimulations() + treeNode.getPendingSimulations();
        if (simLocal == 0)
            return Double.POSITIVE_INFINITY;
        double simTotal = tree.getRootNode().getSimulations() + tree.getRootNode().getPendingSimulations();
        return treeNode.getWinRatio() + Math.sqrt(explorationFactor * Math.log(simTotal + 1) / simLocal);
    }

}
