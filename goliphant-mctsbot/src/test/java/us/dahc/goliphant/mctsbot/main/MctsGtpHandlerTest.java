package us.dahc.goliphant.mctsbot.main;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import us.dahc.goliphant.core.ApplicationIdentity;
import us.dahc.goliphant.core.Board;
import us.dahc.goliphant.core.DefaultBoard;
import us.dahc.goliphant.core.ZobristTable;
import us.dahc.goliphant.gtp.GtpException;
import us.dahc.goliphant.gtp.GtpHandler;
import us.dahc.goliphant.mcts.UcbPolicy;
import us.dahc.goliphant.mctsbot.engine.MctsEngine;

import java.util.Random;

import static org.junit.Assert.assertTrue;

public class MctsGtpHandlerTest {

    private GtpHandler gtpHandler;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setup() {
        Random random = new Random();
        Board board = new DefaultBoard(new ZobristTable(random));
        gtpHandler = new MctsGtpHandler(new ApplicationIdentity("Test Client", "1.0"),
                new MctsEngine(board, new UcbPolicy(2)), board);
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
