package us.dahc.goliphant.mctsbot.engine;

import us.dahc.goliphant.core.Board;
import us.dahc.goliphant.core.Color;
import us.dahc.goliphant.core.Move;
import us.dahc.goliphant.core.Vertex;
import us.dahc.goliphant.core.filters.EyeLikeFilter;
import us.dahc.goliphant.core.filters.FilterList;
import us.dahc.goliphant.core.filters.SuperkoFilter;
import us.dahc.goliphant.mcts.Tree;
import us.dahc.goliphant.mcts.TreeNode;

import javax.inject.Inject;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class SearchThread extends Thread {

    private Tree tree;
    private Board board;
    private FilterList filters;
    private Random random;
    private TreeNode node;
    private Color turn;
    private int depth = 0;

    @Inject
    public SearchThread(Tree tree, Board board, Random random) {
        this.tree = tree;
        this.board = board;
        this.random = random;
        filters = new FilterList();
        filters.add(new EyeLikeFilter());
        filters.add(new SuperkoFilter());
    }

    @Override
    public void run() {
        int simulations = Integer.parseInt(System.getProperty("sim", "1000"));
        for (int i = 0; i < simulations; i++)
            search();
    }

    private void search() {
        node = tree.getRootNode();
        board.setTo(tree.getRootBoard());
        turn = board.getLastMove() == null ? Color.Black : board.getLastMove().getColor().getOpponent();
        depth = 0;
        select();
        if (expand())
            select();
        simulate();
        backpropogate();
    }

    private void select() {
        while (node.getChildren() != null && board.getConsecutivePasses() < 2) {
            node = tree.getPolicy().select(tree, node.getChildren());
            board.play(node.getMove());
            turn = turn.getOpponent();
            depth++;
            //System.err.println("Selected " + node.getMove() + " at depth " + depth);
        }
    }

    private boolean expand() {
        if (node.getSimulations() > 0) {
            List<Move> options = filters.apply(board, turn);
            if (options.size() == 0)
                return false;
            Collections.shuffle(options, random);
            node.setMovesAsChildren(options);
            //System.err.println("Expanding...");
            return true;
        } else {
            return false;
        }
    }

    private void simulate() {
        Color turn = this.turn;
        while (board.getConsecutivePasses() < 2) {
            board.play(board.getRandomMove(turn, random));
            turn = turn.getOpponent();
        }
    }

    private void backpropogate() {
        Color player = turn.getOpponent();
        float score = board.getStonesCapturedBy(player) - board.getStonesCapturedBy(player.getOpponent());
        for (Vertex vertex : board.getAllVertices()) {
            if (board.getColorAt(vertex) == null) {
                Color guess = null;
                for (Vertex neighbor : board.getNeighbors(vertex)) {
                    if (guess == null) {
                        guess = board.getColorAt(neighbor);
                    } else if (board.getColorAt(neighbor) != null && board.getColorAt(neighbor) != guess) {
                        guess = null;
                        break;
                    }
                }
                if (guess == player)
                    score += 1;
                else if (guess == player.getOpponent())
                    score -= 1;
            } else {
                score += board.getColorAt(vertex) == player ? 1 : -1;
            }
        }
        if (player == Color.White)
            score += board.getKomi();
        else
            score -= board.getKomi();
        node.addResults(1, score > 0 ? 1 : 0, false);
    }

}
