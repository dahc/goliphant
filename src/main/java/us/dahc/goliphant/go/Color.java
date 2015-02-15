package us.dahc.goliphant.go;

public enum Color {
    Black, White;

    public Color getOpponent() {
    	if (this == White)
            return Black;
    	else
            return White;
    }
}
