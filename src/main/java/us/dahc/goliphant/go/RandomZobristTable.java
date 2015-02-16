package us.dahc.goliphant.go;

import java.util.Random;

public class RandomZobristTable implements ZobristTable {
    private long[][][] table;

    RandomZobristTable(Random rand, int rows, int columns) {
        table = new long[2][rows][columns];
        for (int i = 0; i < 2; i++)
            for (int j = 0; j < rows; j++)
                for (int k = 0; k < columns; k++)
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
}
