package us.dahc.goliphant.util.hashing;

import us.dahc.goliphant.go.Color;
import us.dahc.goliphant.util.Size;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.Serializable;
import java.util.Random;

@Singleton
public class ZobristTable implements Serializable {

    private static final long serialVersionUID = 20150218013122L;

    private long[][][] table;

    @Inject
    public ZobristTable(Random rand, Size size) {
        table = new long[2][size.getRows()][size.getColumns()];
        for (int i = 0; i < 2; i++)
            for (int j = 0; j < size.getRows(); j++)
                for (int k = 0; k < size.getColumns(); k++)
                    table[i][j][k] = rand.nextLong();
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
        if (object instanceof ZobristTable) {
            ZobristTable other = (ZobristTable) object;
            if (getRows() != other.getRows() || getColumns() != other.getColumns())
                return false;
            for (int i = 0; i < getRows(); i++)
                for (int j = 0; j < getColumns(); j++)
                    if (getEntry(Color.Black, i, j) != other.getEntry(Color.Black, i, j)
                            || getEntry(Color.White, i, j) != other.getEntry(Color.White, i, j))
                        return false;
            return true;
        } else {
            return false;
        }
    }
}
