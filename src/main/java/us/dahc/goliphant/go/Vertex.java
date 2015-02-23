package us.dahc.goliphant.go;

import static us.dahc.goliphant.gtp.GtpConstants.COLUMN_NAMES;

public class Vertex {

    public static final Vertex PASS = new Vertex(-1, -1);

    protected final int row;
    protected final int column;

    public Vertex(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public Vertex(String vertex) {
        if (vertex.equalsIgnoreCase("PASS")) {
            this.row = PASS.row;
            this.column = PASS.column;
        } else {
            this.column = COLUMN_NAMES.indexOf(vertex.toUpperCase().charAt(0));
            this.row = 19 - Integer.valueOf(vertex.substring(1));
        }
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    @Override
    public int hashCode() {
        return (row << 16) | column;
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof Vertex)
            return row == ((Vertex) object).row && column == ((Vertex) object).column;
        else
            return false;
    }

    @Override
    public String toString() {
        if (this.equals(PASS))
            return "PASS";
        return COLUMN_NAMES.charAt(column) + String.valueOf(19 - row);
    }
}
