package us.dahc.goliphant.go.hashing;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class ZobristTableTest {

    private ZobristTable zobTab;

    @Before
    public void setup() {
        zobTab = new ZobristTable(new long[2][13][21]);
    }

    @Test
    public void sizeTest() {
        assertEquals(13, zobTab.getRows());
        assertEquals(21, zobTab.getColumns());
    }

}
