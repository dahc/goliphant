package us.dahc.goliphant.util.hashing;

import static org.junit.Assert.assertEquals;

import org.apache.commons.lang3.SerializationUtils;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

public class ZobristTableTest {

    private ZobristTable zobristTable;

    @Before
    public void setup() {
        Random rand = new Random();
        long[][][] table = new long[2][13][21];
        for (int i = 0; i < 2; i++)
            for (int j = 0; j < 13; j++)
                for (int k = 0; k < 21; k++)
                    table[i][j][k] = rand.nextLong();
        zobristTable = new ZobristTable(table);
    }

    @Test
    public void sizeTest() {
        assertEquals(13, zobristTable.getRows());
        assertEquals(21, zobristTable.getColumns());
    }

    @Test
    public void serializationTest() {
        byte[] serializedZobristTable = SerializationUtils.serialize(zobristTable);
        assertEquals(zobristTable, SerializationUtils.deserialize(serializedZobristTable));
    }

}
