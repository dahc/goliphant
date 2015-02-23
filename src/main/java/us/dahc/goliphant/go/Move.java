package us.dahc.goliphant.go;

import static us.dahc.goliphant.gtp.GtpConstants.COLUMN_NAMES;

public class Move {

    final Color color;
    final int row;
    final int column;

    public Move(Color color, int row, int column) {
        this.color = color;
        this.row = row;
        this.column = column;
    }

    public Move(String color, String vertex) {
        if (color.toUpperCase().charAt(0) == 'B')
            this.color = Color.Black;
        else
            this.color = Color.White;
        this.column = COLUMN_NAMES.indexOf(vertex.toUpperCase().charAt(0));
        this.row = 19 - Integer.valueOf(vertex.substring(1));
    }

    public Color getColor() {
        return color;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof Move) {
            Move move = (Move) object;
            return (color == move.color) && (row == move.row) && (column == move.column);
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return color.name() + " " + COLUMN_NAMES.charAt(column) + String.valueOf(19 - row);
    }
}
