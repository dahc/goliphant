package us.dahc.goliphant.util;

import us.dahc.goliphant.core.Board;
import us.dahc.goliphant.core.Color;
import us.dahc.goliphant.core.Vertex;
import us.dahc.goliphant.gtp.GtpConstants;

import java.util.Formatter;
import java.util.List;

public class BoardPrettyPrinter {

    public static String getPrettyString(Board board) {
        List<Vertex> stars = StarPointHelper.getDisplayPoints(board);
        StringBuilder stringBuilder = new StringBuilder("   ");
        Formatter formatter = new Formatter(stringBuilder);
        for (int j = 0; j < board.getColumns(); j++)
            stringBuilder.append(' ').append(GtpConstants.COLUMN_NAMES.charAt(j));
        stringBuilder.append('\n');
        for (int i = board.getRows() - 1; i >= 0; i--) {
            formatter.format("%1$ 3d", i + 1);
            for (int j = 0; j < board.getColumns(); j++)
                stringBuilder.append(' ').append(getChar(board, stars, i, j));
            formatter.format("%1$- 3d\n", i + 1);
        }
        stringBuilder.append("   ");
        for (int j = 0; j < board.getColumns(); j++)
            stringBuilder.append(' ').append(GtpConstants.COLUMN_NAMES.charAt(j));
        return stringBuilder.toString();
    }

    protected static char getChar(Board board, List<Vertex> stars, int row, int column) {
        Color color = board.getColorAt(row, column);
        if (color == null) {
            if (stars.contains(new Vertex(row, column)))
                return '+';
            else
                return '.';
        }
        return color == Color.Black ? 'X' : 'O';
    }

}