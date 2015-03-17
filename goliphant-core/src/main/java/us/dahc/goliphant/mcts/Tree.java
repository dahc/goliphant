package us.dahc.goliphant.mcts;

import us.dahc.goliphant.core.Board;

import javax.inject.Inject;

public class Tree {

    private TreeNode rootNode;
    private Board rootBoard;
    private Policy policy;

    @Inject
    public Tree(Board rootBoard, Policy policy) {
        this.rootBoard = rootBoard;
        this.policy = policy;
        rootNode = new TreeNode(null, rootBoard.getLastMove());
    }

    public void setRootNode(TreeNode treeNode) {
        rootBoard.play(treeNode.getMove());
        rootNode = treeNode;
        rootNode.setParent(null);
    }

    public TreeNode getRootNode() {
        return rootNode;
    }

    public Board getRootBoard() {
        return rootBoard;
    }

    public Policy getPolicy() {
        return policy;
    }

}
