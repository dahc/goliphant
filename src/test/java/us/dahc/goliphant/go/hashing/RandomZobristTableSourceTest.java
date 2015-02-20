package us.dahc.goliphant.go.hashing;

import org.junit.Before;
import org.junit.Test;
import us.dahc.goliphant.go.Color;

import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class RandomZobristTableSourceTest {

    private ZobristTableSource zobristTableSource;

    @Before
    public void setup() {
        zobristTableSource = new RandomZobristTableSource(new Random());
    }

    @Test
    public void SizeTableBijectionTest() {
        ZobristTable zta = zobristTableSource.get(19, 19);
        ZobristTable ztb = zobristTableSource.get(19, 19);
        assertEquals(zta, ztb);
        ZobristTable ztc = zobristTableSource.get(13, 21);
        ZobristTable ztd = zobristTableSource.get(13, 21);
        assertEquals(ztc, ztd);
        assertNotEquals(zta, ztc);
    }
}
