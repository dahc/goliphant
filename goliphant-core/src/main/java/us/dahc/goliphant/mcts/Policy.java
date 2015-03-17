package us.dahc.goliphant.mcts;

import java.util.Collection;

public interface Policy {

    public TreeNode select(final Tree tree, Collection<TreeNode> options);

}
