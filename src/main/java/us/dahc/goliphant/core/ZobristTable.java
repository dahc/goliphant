package us.dahc.goliphant.core;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.Serializable;
import java.util.Random;

@Singleton
public class ZobristTable implements Serializable {

    private static final long serialVersionUID = 20150302001928L;
    private long[][][] table;

    @Inject
    public ZobristTable(Random rand) {
        table = new long[2][Board.MAX_ROWS][Board.MAX_COLUMNS];
        for (int i = 0; i < 2; i++)
            for (int j = 0; j < Board.MAX_ROWS; j++)
                for (int k = 0; k < Board.MAX_COLUMNS; k++)
                    table[i][j][k] = rand.nextLong();
    }

    public long getEntry(Color color, int row, int column) {
        return table[color == Color.Black ? 0 : 1][row][column];
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof ZobristTable) {
            ZobristTable other = (ZobristTable) object;
            for (int i = 0; i < Board.MAX_ROWS; i++)
                for (int j = 0; j < Board.MAX_COLUMNS; j++)
                    if (getEntry(Color.Black, i, j) != other.getEntry(Color.Black, i, j)
                            || getEntry(Color.White, i, j) != other.getEntry(Color.White, i, j))
                        return false;
            return true;
        } else {
            return false;
        }
    }
}
