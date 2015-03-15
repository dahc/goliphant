package us.dahc.goliphant.core;

import static us.dahc.goliphant.gtp.GtpConstants.COLUMN_NAMES;

public class Vertex {

    public static final Vertex PASS = new Vertex(-1, -1);

    protected final int row;
    protected final int column;

    private static final Vertex[][] values = new Vertex[Board.MAX_ROWS][Board.MAX_COLUMNS];

    static {
        for (int i = 0; i < Board.MAX_ROWS; i++)
            for (int j = 0; j < Board.MAX_COLUMNS; j++)
                values[i][j] = new Vertex(i, j);
    }

    protected Vertex(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public static Vertex get(int row, int column) {
        return values[row][column];
    }

    public static Vertex get(String vertex) {
        if (vertex.equalsIgnoreCase("PASS")) {
            return PASS;
        } else {
            int row = Integer.valueOf(vertex.substring(1)) - 1;
            int column = COLUMN_NAMES.indexOf(vertex.toUpperCase().charAt(0));
            return values[row][column];
        }
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    @Override
    public String toString() {
        if (this.equals(PASS))
            return "PASS";
        else
            return COLUMN_NAMES.charAt(column) + String.valueOf(row + 1);
    }

    @Override
    public int hashCode() {
        return (row << 16) | column;
    }

    @Override
    public boolean equals(Object object) {
        return object instanceof Vertex && row == ((Vertex) object).row && column == ((Vertex) object).column;
    }

}
