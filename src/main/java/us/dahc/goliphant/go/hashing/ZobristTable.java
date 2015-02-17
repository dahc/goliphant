package us.dahc.goliphant.go.hashing;

import us.dahc.goliphant.go.Color;

public class ZobristTable {
    private long[][][] table;

    ZobristTable(long[][][] table) {
        this.table = table;
    }

    public long getEntry(Color color, int row, int column) {
        return table[color == Color.Black ? 0 : 1][row][column];
    }

    public int getRows() {
        return table[0].length;
    }

    public int getColumns() {
        return table[0][0].length;
    }
}
