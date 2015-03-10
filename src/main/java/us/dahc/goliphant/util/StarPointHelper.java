package us.dahc.goliphant.util;

import us.dahc.goliphant.core.Board;
import us.dahc.goliphant.core.Vertex;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.lang.Integer.min;

// FIXME: This is pretty much ignores asymmetric boards. (But does anybody really care?)
public class StarPointHelper {

    public static List<Vertex> getDisplayPoints(Board board) {
        if (board.getRows() < 7)
            return Collections.EMPTY_LIST;
        if (board.getRows() % 2 == 0)
            return getLimitedStarPoints(board, 4);
        if (board.getRows() < 9)
            return getLimitedStarPoints(board, 4);
        if (board.getRows() < 14)
            return getLimitedStarPoints(board, 5);
        return getLimitedStarPoints(board, 9);
    }

    public static List<Vertex> getHandicapPoints(Board board, int handicap) {
        if (board.getRows() < 7)
            return Collections.EMPTY_LIST;
        if (board.getRows() % 2 == 0)
            return getLimitedStarPoints(board, min(4, handicap));
        if (board.getRows() < 9)
            return getLimitedStarPoints(board, min(4, handicap));
        return getLimitedStarPoints(board, min(9, handicap));
    }

    private static List<Vertex> getLimitedStarPoints(Board board, int limit) {
        if (board.getRows() < 13)
            return getStarPoints(board, 3, limit).subList(0, limit);
        else
            return getStarPoints(board, 4, limit).subList(0, limit);
    }

    private static List<Vertex> getStarPoints(Board board, int edgeDistance, int total) {
        List<Vertex> starPoints = new ArrayList<>();
        starPoints.add(new Vertex(edgeDistance - 1, edgeDistance - 1));                                 // D4
        starPoints.add(new Vertex(board.getRows() - edgeDistance, board.getColumns() - edgeDistance));  // Q16
        starPoints.add(new Vertex(board.getRows() - edgeDistance, edgeDistance - 1));                   // D16
        starPoints.add(new Vertex(edgeDistance - 1, board.getColumns() - edgeDistance));                // Q4
        if (total > 5) {
            starPoints.add(new Vertex(board.getRows() / 2, edgeDistance - 1));                          // D10
            starPoints.add(new Vertex(board.getRows() / 2, board.getColumns() - edgeDistance));         // Q10
        }
        if (total > 7) {
            starPoints.add(new Vertex(edgeDistance - 1, board.getColumns() / 2));                       // K4
            starPoints.add(new Vertex(board.getRows() - edgeDistance, board.getColumns() / 2));         // K16
        }
        starPoints.add(new Vertex(board.getRows() / 2, board.getColumns() / 2));                        // K10
        return starPoints;
    }

}
