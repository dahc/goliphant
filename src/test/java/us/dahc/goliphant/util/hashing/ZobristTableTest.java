package us.dahc.goliphant.util.hashing;

import static org.junit.Assert.assertEquals;

import org.apache.commons.lang3.SerializationUtils;
import org.junit.Before;
import org.junit.Test;
import us.dahc.goliphant.util.Size;

import java.util.Random;

public class ZobristTableTest {

    private ZobristTable zobristTable;

    @Before
    public void setup() {
        zobristTable = new ZobristTable(new Random(), new Size(13, 21));
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
