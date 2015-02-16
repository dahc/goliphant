package us.dahc.goliphant.go.hashing;

import java.util.Random;

import javax.inject.Inject;

import us.dahc.goliphant.go.Color;

public class ZobristTable {
    private long[][][] table;

    @Inject
    Random rand;

    ZobristTable(int rows, int columns) {
        table = new long[2][rows][columns];
    }

    void initializeRandomly() {
        for (int i = 0; i < 2; i++)
            for (int j = 0; j < table[0].length; j++)
                for (int k = 0; k < table[0][0].length; k++)
                    table[i][j][k] = rand.nextLong();
    }

    void setTo(long[][][] table) {
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
