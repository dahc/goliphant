package us.dahc.goliphant.util;

import us.dahc.goliphant.go.Board;
import us.dahc.goliphant.go.Color;
import us.dahc.goliphant.gtp.GtpConstants;

import java.util.Formatter;

public class BoardPrettyPrinter {

    public static String getPrettyString(Board board) {
        StringBuilder stringBuilder = new StringBuilder("   ");
        Formatter formatter = new Formatter(stringBuilder);
        for (int j = 0; j < board.getColumns(); j++)
            stringBuilder.append(' ').append(GtpConstants.COLUMN_NAMES.charAt(j));
        stringBuilder.append('\n');
        for (int i = 0; i < board.getRows(); i++) {
            formatter.format("%1$ 3d", board.getRows() - i);
            for (int j = 0; j < board.getColumns(); j++)
                stringBuilder.append(' ').append(getChar(board.getColorAt(i, j)));
            formatter.format("%1$- 3d\n", board.getRows() - i);
        }
        stringBuilder.append("   ");
        for (int j = 0; j < board.getColumns(); j++)
            stringBuilder.append(' ').append(GtpConstants.COLUMN_NAMES.charAt(j));
        return stringBuilder.toString();
    }

    public static char getChar(Color color) {
        if (color == null)
            return '.';
        return color == Color.Black ? 'X' : 'O';
    }

}
