package us.dahc.goliphant.gtp;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import us.dahc.goliphant.go.Color;
import us.dahc.goliphant.go.DefaultBoard;
import us.dahc.goliphant.util.ZobristTable;

import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

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

    @Test
    public void testKomiCommands() throws GtpException {
        gtpHandler.handle("komi", "5.5");
        assertEquals(5.5F, gtpHandler.currentBoard.getKomi(), 0.1F);
        assertEquals("5.5", gtpHandler.handle("get_komi"));
    }

    @Test
    public void testKomiCommand_NonFloat() throws GtpException {
        thrown.expect(GtpException.class);
        thrown.expectMessage("komi not a float");
        gtpHandler.handle("komi", "nan");
    }

    @Test
    public void testPlayCommand() throws GtpException {
        assertEquals(0L, gtpHandler.currentBoard.getZobristHash());
        gtpHandler.handle("play", "black", "a19");
        assertEquals(Color.Black, gtpHandler.currentBoard.getColorAt(0, 0));
        gtpHandler.handle("play", "white", "pass");
        assertEquals(1, gtpHandler.currentBoard.getConsecutivePasses());
    }

    @Test
    public void testPlayCommand_InvalidCoord() throws GtpException {
        thrown.expect(GtpException.class);
        thrown.expectMessage("invalid color or coordinate");
        gtpHandler.handle("play", "black", "2");
    }

    @Test
    public void testPlayCommand_IllegalMove() throws GtpException {
        thrown.expect(GtpException.class);
        thrown.expectMessage("illegal move");
        gtpHandler.handle("play", "black", "a19");
        gtpHandler.handle("play", "white", "a19");
    }

    @Test
    public void testUndoCommand() throws GtpException {
        assertEquals(0L, gtpHandler.currentBoard.getZobristHash());
        gtpHandler.handle("play", "black", "c4");
        gtpHandler.handle("play", "white", "d17");
        assertNotEquals(0L, gtpHandler.currentBoard.getZobristHash());
        gtpHandler.handle("undo");
        assertNotEquals(0L, gtpHandler.currentBoard.getZobristHash());
        gtpHandler.handle("undo");
        assertEquals(0L, gtpHandler.currentBoard.getZobristHash());
    }

    @Test
    public void testUndoCommand_NoHistory() throws GtpException {
        thrown.expect(GtpException.class);
        thrown.expectMessage("cannot undo");
        gtpHandler.handle("undo");
    }

}
