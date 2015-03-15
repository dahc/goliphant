package us.dahc.goliphant.core;

import static org.junit.Assert.assertEquals;

import org.apache.commons.lang3.SerializationUtils;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

public class ZobristTableTest {

    private ZobristTable zobristTable;

    @Before
    public void setup() {
        zobristTable = new ZobristTable(new Random());
    }

    @Test
    public void serializationTest() {
        byte[] serializedZobristTable = SerializationUtils.serialize(zobristTable);
        assertEquals(zobristTable, SerializationUtils.deserialize(serializedZobristTable));
    }

}
