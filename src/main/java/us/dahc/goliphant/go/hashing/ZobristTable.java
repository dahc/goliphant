package us.dahc.goliphant.go.hashing;

import us.dahc.goliphant.go.Color;

import java.io.Serializable;

public class ZobristTable implements Serializable {

    private static final long serialVersionUID = 20150218013122L;

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

    @Override
    public boolean equals(Object object) {
        ZobristTable other;
        if (object instanceof ZobristTable)
            other = (ZobristTable) object;
        else
            return false;
        for (int i = 0; i < getRows(); i++) {
            for (int j = 0; j < getColumns(); j++) {
                if (getEntry(Color.Black, i, j) != other.getEntry(Color.Black, i, j)
                        || getEntry(Color.White, i, j) != other.getEntry(Color.White, i, j))
                    return false;
            }
        }
        return true;
    }
}
