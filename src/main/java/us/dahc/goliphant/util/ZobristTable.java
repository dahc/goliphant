package us.dahc.goliphant.util;

import us.dahc.goliphant.go.Color;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.Serializable;
import java.util.Random;

@Singleton
public class ZobristTable implements Serializable {

    private static final long serialVersionUID = 20150222171803L;
    private static final int maxRows = 100;
    private static final int maxColumns = 25;
    private long[][][] table;

    @Inject
    public ZobristTable(Random rand) {
        table = new long[2][maxRows][maxColumns];
        for (int i = 0; i < 2; i++)
            for (int j = 0; j < maxRows; j++)
                for (int k = 0; k < maxColumns; k++)
                    table[i][j][k] = rand.nextLong();
    }

    public long getEntry(Color color, int row, int column) {
        return table[color == Color.Black ? 0 : 1][row][column];
    }

    public int getMaxRows() {
        return table[0].length;
    }

    public int getMaxColumns() {
        return table[0][0].length;
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof ZobristTable) {
            ZobristTable other = (ZobristTable) object;
            if (getMaxRows() != other.getMaxRows() || getMaxColumns() != other.getMaxColumns())
                return false;
            for (int i = 0; i < getMaxRows(); i++)
                for (int j = 0; j < getMaxColumns(); j++)
                    if (getEntry(Color.Black, i, j) != other.getEntry(Color.Black, i, j)
                            || getEntry(Color.White, i, j) != other.getEntry(Color.White, i, j))
                        return false;
            return true;
        } else {
            return false;
        }
    }
}
