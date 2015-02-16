package us.dahc.goliphant.go;

public class Move {

    private static final String columnNames = "ABCDEFGHJKLMNOPRSTUVWXYZ";

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
        this.column = columnNames.indexOf(vertex.toUpperCase().charAt(0));
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

    public boolean equals(Move move) {
        return (color == move.color) && (row == move.row) && (column == move.column);
    }

    @Override
    public String toString() {
        return color.name() + " " + columnNames.charAt(column) + String.valueOf(19 - row);
    }
}
