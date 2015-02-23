package us.dahc.goliphant.gtp;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import us.dahc.goliphant.go.DefaultBoard;
import us.dahc.goliphant.util.ZobristTable;

import java.util.Random;

import static org.junit.Assert.assertEquals;

public class StatefulGtpHandlerTest {

    private StatefulGtpHandler gtpHandler;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setup() {
        gtpHandler = new StatefulGtpHandler(new GtpClientIdentity("Test Client", "1.0"),
                                            new DefaultBoard(new ZobristTable(new Random())));
    }

    @Test
    public void testBoardsizeCommand() throws GtpException {
        gtpHandler.handle("boardsize", "13");
        assertEquals(13, gtpHandler.currentBoard.getRows());
        assertEquals(13, gtpHandler.currentBoard.getColumns());
        assertEquals(0L, gtpHandler.currentBoard.getZobristHash());
        assertEquals(0, gtpHandler.pastBoards.size());
    }

    @Test
    public void testBoardsizeCommand_NonInteger() throws GtpException {
        thrown.expect(GtpException.class);
        thrown.expectMessage("boardsize not an integer");
        gtpHandler.handle("boardsize", "nineteen");
    }

    @Test
    public void testBoardsizeCommand_Oversized() throws GtpException {
        thrown.expect(GtpException.class);
        gtpHandler.handle("boardsize", "1000");
    }

    @Test
    public void testClearBoardCommand() throws GtpException {
        // TODO: Play some stuff first...
        gtpHandler.handle("clear_board");
        assertEquals(0L, gtpHandler.currentBoard.getZobristHash());
        assertEquals(0, gtpHandler.pastBoards.size());
    }

}
