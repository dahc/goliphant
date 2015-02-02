package us.dahc.goliphant.go;

// null indicates "off board", hence the need for Empty
public enum Color {
    Black, White, Empty;

    public Color getOpponent() {
        switch (this) {
        case White:
            return Black;
        case Black:
            return White;
        default:
            return null;
        }
    }
}
