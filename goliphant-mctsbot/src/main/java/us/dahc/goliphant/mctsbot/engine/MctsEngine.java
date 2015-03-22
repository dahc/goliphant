package us.dahc.goliphant.mctsbot.engine;

import us.dahc.goliphant.core.Board;
import us.dahc.goliphant.core.Color;
import us.dahc.goliphant.core.Move;
import us.dahc.goliphant.core.Vertex;
import us.dahc.goliphant.mcts.Policy;
import us.dahc.goliphant.mcts.Tree;
import us.dahc.goliphant.mcts.TreeNode;

import javax.inject.Inject;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

public class MctsEngine implements Engine {

    private Tree tree;

    @Inject
    public MctsEngine(Board board, Policy policy) {
        tree = new Tree(board.getCopy(), policy);
    }

    public Move getMove(Board board, Color player) {
        if (!tree.getRootBoard().equals(board)) {
            tree = new Tree(board.getCopy(), tree.getPolicy());
        }
        new SearchThread(tree, board.getCopy(), new Random()).run();
        if (tree.getRootNode().getChildren() != null) {
            TreeNode choice =  Collections.max(tree.getRootNode().getChildren(), new Comparator<TreeNode>() {
                @Override
                public int compare(TreeNode t1, TreeNode t2) {
                    if (t1.getSimulations() > t2.getSimulations())
                        return 1;
                    else if (t2.getSimulations() > t1.getSimulations())
                        return -1;
                    else
                        return 0;
                }
            });
            System.err.println("Chose " + choice.getMove() + " - winRatio: " + choice.getWinRatio()
                    + " playouts: " + choice.getSimulations());
            return choice.getMove();
        } else {
            return Move.get(player, Vertex.PASS);
        }
    }

}
