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
        playSomeStuff(gtpHandler);
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
        gtpHandler.handle("play", "black", "a1");
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

    @Test
    public void testSetFreeHandicapCommand() throws GtpException {
        gtpHandler.handle("set_free_handicap", "Q16", "D4");
        assertEquals(Color.Black, gtpHandler.currentBoard.getColorAt(3, 3));
        assertEquals(Color.Black, gtpHandler.currentBoard.getColorAt(15, 15));
    }

    @Test
    public void testSetFreeHandicapCommand_TooSmall() throws GtpException {
        thrown.expect(GtpException.class);
        thrown.expectMessage("invalid handicap");
        gtpHandler.handle("set_free_handicap", "D4");
    }

    @Test
    public void testSetFreeHandicapCommand_Large() throws GtpException {
        gtpHandler.handle("set_free_handicap", "D4", "Q16", "D16", "Q4", "D10", "Q10", "K4", "K16", "K10", "A1");
        assertEquals(Color.Black, gtpHandler.currentBoard.getColorAt(0, 0));
    }

    @Test
    public void testSetFreeHandicapCommand_NoPassing() throws GtpException {
        thrown.expect(GtpException.class);
        thrown.expectMessage("invalid coordinate");
        gtpHandler.handle("set_free_handicap", "D4", "PASS");
    }

    @Test
    public void testSetFreeHandicapCommand_MangledCoord() throws GtpException {
        thrown.expect(GtpException.class);
        thrown.expectMessage("invalid coordinate");
        gtpHandler.handle("set_free_handicap", "Q16", "D4", "16D");
    }

    @Test
    public void testSetFreeHandicapCommand_NoRepeats() throws GtpException {
        thrown.expect(GtpException.class);
        thrown.expectMessage("repeated vertex");
        gtpHandler.handle("set_free_handicap", "Q16", "D4", "D16", "Q16");
    }

    @Test
    public void testSetFreeHandicapCommand_BoardNotEmpty() throws GtpException {
        thrown.expect(GtpException.class);
        thrown.expectMessage("board not empty");
        playSomeStuff(gtpHandler);
        gtpHandler.handle("set_free_handicap", "Q16", "D4", "D16", "Q16");
    }

    @Test
    public void testFixedHandicapCommand() throws GtpException {
        gtpHandler.handle("fixed_handicap", "2");
        assertEquals(Color.Black, gtpHandler.currentBoard.getColorAt(3, 3));
        assertEquals(Color.Black, gtpHandler.currentBoard.getColorAt(15, 15));
    }

    @Test
    public void testFixedHandicapCommand_TooSmall() throws GtpException {
        thrown.expect(GtpException.class);
        thrown.expectMessage("invalid handicap");
        gtpHandler.handle("fixed_handicap", "1");
    }

    @Test
    public void testFixedHandicapCommand_TooBig() throws GtpException {
        thrown.expect(GtpException.class);
        thrown.expectMessage("invalid handicap");
        gtpHandler.handle("fixed_handicap", "10");
    }

    @Test
    public void testFixedHandicapCommand_BoardNotEmpty() throws GtpException {
        thrown.expect(GtpException.class);
        thrown.expectMessage("board not empty");
        playSomeStuff(gtpHandler);
        gtpHandler.handle("fixed_handicap", "2");
    }

    @Test
    public void testPlaceFreeHandicapCommand() throws GtpException {
        gtpHandler.handle("place_free_handicap", "2");
        assertEquals(Color.Black, gtpHandler.currentBoard.getColorAt(3, 3));
        assertEquals(Color.Black, gtpHandler.currentBoard.getColorAt(15, 15));
    }

    @Test
    public void testPlaceFreeHandicapCommand_TooSmall() throws GtpException {
        thrown.expect(GtpException.class);
        thrown.expectMessage("invalid handicap");
        gtpHandler.handle("place_free_handicap", "1");
    }

    @Test
    public void testPlaceFreeHandicapCommand_Large() throws GtpException {
        gtpHandler.handle("place_free_handicap", "15");
        assertNotEquals(0L, gtpHandler.currentBoard.getZobristHash());
    }

    @Test
    public void testPlaceFreeHandicapCommand_BoardNotEmpty() throws GtpException {
        thrown.expect(GtpException.class);
        thrown.expectMessage("board not empty");
        playSomeStuff(gtpHandler);
        gtpHandler.handle("place_free_handicap", "2");
    }

    private void playSomeStuff(GtpHandler handler) throws GtpException {
        handler.handle("play", "black", "d4");
        handler.handle("play", "white", "k10");
        handler.handle("play", "black", "d15");
        handler.handle("play", "white", "q16");
    }

}
