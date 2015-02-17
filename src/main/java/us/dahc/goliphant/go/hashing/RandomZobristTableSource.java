package us.dahc.goliphant.go.hashing;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.inject.Inject;

public class RandomZobristTableSource implements ZobristTableSource {

    private Map<Size, ZobristTable> tableSpace;

    private Random rand;

    @Inject
    public RandomZobristTableSource(Random rand) {
        this.rand = rand;
        tableSpace = new HashMap<Size, ZobristTable>();
    }

    public ZobristTable get(int rows, int columns) {
        Size size = new Size(rows, columns);
        if (!tableSpace.containsKey(size)) {
            long[][][] table = new long[2][rows][columns];
            for (int i = 0; i < 2; i++)
                for (int j = 0; j < rows; j++)
                    for (int k = 0; k < columns; k++)
                        table[i][j][k] = rand.nextLong();
            tableSpace.put(size, new ZobristTable(table));
        }
        return tableSpace.get(size);
    }

    class Size {

        private int size;

        Size(int rows, int columns) {
            size = (rows << 16) + columns;
        }

        protected boolean equals(Size other) {
            return size == other.size;
        }
    }

}
