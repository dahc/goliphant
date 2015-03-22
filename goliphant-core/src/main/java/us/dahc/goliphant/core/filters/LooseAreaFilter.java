package us.dahc.goliphant.core.filters;

import us.dahc.goliphant.core.Board;
import us.dahc.goliphant.core.Move;
import us.dahc.goliphant.core.Vertex;
import us.dahc.goliphant.util.StarPointHelper;

import java.util.Collection;

public class LooseAreaFilter extends Filter {

    private boolean[][] accepted;

    @Override
    public void init(Board board) {
        accepted = new boolean[board.getRows()][board.getColumns()];
        if (board.getRows() > 13)
            largeBoardInit(board);
        else
            smallBoardInit(board);
    }

    @Override
    public boolean accept(Board board, Move move) {
        return accepted[move.getVertex().getRow()][move.getVertex().getColumn()];
    }

    protected void smallBoardInit(Board board) {
        boolean advanced = board.getMoveNumber() > 20;
        for (int i = 0; i < board.getRows(); i++)
            for (int j = 0; j < board.getColumns(); j++)
                accepted[i][j] = advanced;
        if (advanced)
            return;
        if (board.getMoveNumber() < 5) {
            initSurroundings(board, StarPointHelper.getHandicapPoints(board, 5), 1);
        } else if (board.getMoveNumber() < 10) {
            initSurroundings(board, StarPointHelper.getHandicapPoints(board, 9), 2);
        } else {
            initSurroundings(board, StarPointHelper.getHandicapPoints(board, 4), 4);
            initSurroundings(board, StarPointHelper.getHandicapPoints(board, 9), 3);
        }

    }

    protected void largeBoardInit(Board board) {
        boolean advanced = board.getMoveNumber() > 48;
        for (int i = 0; i < board.getRows(); i++)
            for (int j = 0; j < board.getColumns(); j++)
                accepted[i][j] = advanced;
        if (advanced)
            return;
        if (board.getMoveNumber() < 4) {
            for (Vertex star : StarPointHelper.getHandicapPoints(board, 4)) {
                accepted[star.getRow()][star.getColumn()] = true;
                for (Vertex card : board.getNeighbors(star))
                    accepted[card.getRow()][card.getColumn()] = true;
            }
        } else if (board.getMoveNumber() < 10) {
            initSurroundings(board, StarPointHelper.getHandicapPoints(board, 4), 1);
        } else if (board.getMoveNumber() < 20) {
            initSurroundings(board, StarPointHelper.getHandicapPoints(board, 4), 2);
            initSurroundings(board, StarPointHelper.getHandicapPoints(board, 9), 2);
        } else if (board.getMoveNumber() < 30) {
            initSurroundings(board, StarPointHelper.getHandicapPoints(board, 4), 3);
            initSurroundings(board, StarPointHelper.getHandicapPoints(board, 9), 2);
        } else {
            initSurroundings(board, StarPointHelper.getHandicapPoints(board, 4), 4);
            initSurroundings(board, StarPointHelper.getHandicapPoints(board, 9), 3);
        }

    }

    protected void initSurroundings(Board board, Collection<? extends Vertex> vertices, int delta) {
        for (Vertex vertex : vertices) {
            accepted[vertex.getRow()][vertex.getColumn()] = true;
            if (delta > 0) {
                initSurroundings(board, board.getNeighbors(vertex), delta - 1);
                initSurroundings(board, board.getDiagonals(vertex), delta - 1);
            }
        }
    }

}
