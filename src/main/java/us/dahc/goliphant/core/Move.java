package us.dahc.goliphant.core;

public class Move {

    protected final Color color;
    protected final Vertex vertex;

    private static final Move BLACK_PASS = new Move(Color.Black, Vertex.PASS);
    private static final Move WHITE_PASS = new Move(Color.White, Vertex.PASS);
    private static final Move[][][] values = new Move[2][Board.MAX_ROWS][Board.MAX_COLUMNS];

    static {
        for (int i = 0; i < 2; i++)
            for (int j = 0; j < Board.MAX_ROWS; j++)
                for (int k = 0; k < Board.MAX_COLUMNS; k++)
                    values[i][j][k] = new Move(i == 0 ? Color.Black : Color.White, Vertex.get(j, k));
    }

    protected Move(Color color, Vertex vertex) {
        this.vertex = vertex;
        this.color = color;
    }

    public static Move get(Color color, int row, int column) {
        return values[color == Color.Black ? 0 : 1][row][column];
    }

    public static Move get(Color color, Vertex vertex) {
        if (vertex.equals(Vertex.PASS))
            return color == Color.Black ? BLACK_PASS : WHITE_PASS;
        else
            return values[color == Color.Black ? 0 : 1][vertex.getRow()][vertex.getColumn()];
    }

    public static Move get(String color, String vertex) {
        return get(color.toUpperCase().charAt(0) == 'B' ? Color.Black : Color.White, Vertex.get(vertex));
    }

    public Color getColor() {
        return color;
    }

    public Vertex getVertex() {
        return vertex;
    }

    public int getRow() {
        return vertex.getRow();
    }

    public int getColumn() {
        return vertex.getColumn();
    }

    @Override
    public String toString() {
        return color.name() + " " + vertex.toString();
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof Move)
            return color == ((Move) object).color && vertex.equals(((Move) object).getVertex());
        else
            return false;
    }

}
