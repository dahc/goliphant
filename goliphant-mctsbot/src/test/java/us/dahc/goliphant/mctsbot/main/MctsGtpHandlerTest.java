package us.dahc.goliphant.mctsbot.main;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import us.dahc.goliphant.core.ApplicationIdentity;
import us.dahc.goliphant.core.DefaultBoard;
import us.dahc.goliphant.core.ZobristTable;
import us.dahc.goliphant.gtp.GtpException;
import us.dahc.goliphant.gtp.GtpHandler;

import java.util.Random;

import static org.junit.Assert.assertTrue;

public class MctsGtpHandlerTest {

    private GtpHandler gtpHandler;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setup() {
        Random random = new Random();
        gtpHandler = new MctsGtpHandler(new ApplicationIdentity("Test Client", "1.0"),
                new DefaultBoard(new ZobristTable(random)), random);
    }

    @Test
    public void testBoardsizeCommand() throws GtpException {
        assertTrue(gtpHandler.handle("genmove", "black").matches("[a-zA-Z][0-9]+"));
    }

    @Test
    public void testGenmoveCommand_NoColor() throws GtpException {
        thrown.expect(GtpException.class);
        thrown.expectMessage("invalid color");
        gtpHandler.handle("genmove");
    }

    @Test
    public void testGenmoveCommand_BadColor() throws GtpException {
        thrown.expect(GtpException.class);
        thrown.expectMessage("invalid color");
        gtpHandler.handle("genmove", "purple");
    }

}
