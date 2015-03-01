package us.dahc.goliphant.sgf;

import org.junit.Before;
import org.junit.Test;
import us.dahc.goliphant.go.DefaultBoard;
import us.dahc.goliphant.util.ZobristTable;

import java.util.Random;

public class SgfParserTest {

    SgfParser sgfParser;

    @Before
    public void setup() {
        sgfParser = new SgfParser(new DefaultBoard(new ZobristTable(new Random())));
    }

    @Test
    public void testKomiProperty() {

    }

}
