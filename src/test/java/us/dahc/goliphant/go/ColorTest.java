package us.dahc.goliphant.go;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

public class ColorTest {

    @Test
    public void testOpponent() {
        assertTrue(Color.White.getOpponent() == Color.Black);
        assertTrue(Color.Black.getOpponent() == Color.White);
        assertTrue(Color.Empty.getOpponent() == null);
    }
}
