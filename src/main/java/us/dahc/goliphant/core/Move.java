package us.dahc.goliphant.core;

public class Move {

    final Color color;
    final Vertex vertex;

    public Move(Color color, int row, int column) {
        this.vertex = Vertex.get(row, column);
        this.color = color;
    }

    public Move(Color color, Vertex vertex) {
        this.vertex = vertex;
        this.color = color;
    }

    public Move(String color, String vertex) {
        this.vertex = Vertex.get(vertex);
        if (color.toUpperCase().charAt(0) == 'B')
            this.color = Color.Black;
        else
            this.color = Color.White;
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
