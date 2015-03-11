package us.dahc.goliphant.core;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class DefaultBoardTest {

    private DefaultBoard stdBoard;
    private DefaultBoard asymBoard;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setup() throws Exception {
        ZobristTable stdZobTab = new ZobristTable(new Random());
        ZobristTable asymZobTab = new ZobristTable(new Random());
        stdBoard = new DefaultBoard(stdZobTab);
        asymBoard = new DefaultBoard(asymZobTab);
        asymBoard.resize(13, 21);
    }

    @Test
    public void testSizeGetters() {
        assertEquals(19, stdBoard.getRows());
        assertEquals(19, stdBoard.getColumns());
        assertEquals(13, asymBoard.getRows());
        assertEquals(21, asymBoard.getColumns());
    }

    @Test
    public void testInitialState() {
        for (int i = 0; i < stdBoard.getRows(); i++) {
            for (int j = 0; j < stdBoard.getColumns(); j++) {
                assertThat("initial (" + i + ", " + j + ") emptiness",
                        stdBoard.getColorAt(i, j), is(nullValue()));
                assertThat("initial (" + i + ", " + j + ") legality",
                        stdBoard.isLegal(Move.get(Color.Black, i, j)));
            }
        }
        assertEquals(19 * 19 + 1, stdBoard.getLegalMoveVertices(Color.Black).size());
        assertEquals(0L, stdBoard.getZobristHash());
        assertNull(stdBoard.getLastMove());
        assertNull(stdBoard.getKoMove());
        assertEquals(0.0F, stdBoard.getKomi(), 0.1F);
    }

    @Test
    public void testReset() {
        playSomeStuff(stdBoard);
        stdBoard.reset();
        testInitialState();
        testHashHistory();
    }

    @Test
    public void testInvalidResize() throws Exception {
        thrown.expect(InvalidSizeException.class);
        stdBoard.resize(500, 500);
    }

    @Test
    public void testPlay() {
        assertEquals(null, stdBoard.getColorAt(3, 3));
        stdBoard.play(Move.get(Color.Black, 3, 3));
        assertEquals(Color.Black, stdBoard.getColorAt(3, 3));
        assertEquals(Move.get(Color.Black, 3, 3), stdBoard.getLastMove());
        assertThat("play on other stone", !stdBoard.isLegal(Move.get(Color.White, 3, 3)));
        for (int i = 0; i < stdBoard.getRows(); i++)
            for (int j = 0; j < stdBoard.getColumns(); j++)
                if (i != 3 || j != 3)
                    assertThat("wide open square (" + i + ", " + j + ") legality",
                            stdBoard.isLegal(Move.get(Color.White, i, j)));
        assertEquals(19 * 19, stdBoard.getLegalMoveVertices(Color.White).size());
        assertNotEquals(0L, stdBoard.getZobristHash());
    }

    @Test
    public void testCornerCapture() {
        stdBoard.play(Move.get(Color.Black, 0, 1));
        stdBoard.play(Move.get(Color.White, 0, 0));
        stdBoard.play(Move.get(Color.Black, 1, 0));
        assertThat("stone was captured", stdBoard.getColorAt(0, 0), is(nullValue()));
        assertThat("black did capture", stdBoard.getStonesCapturedBy(Color.Black), is(equalTo(1)));
        assertThat("white did not capture", stdBoard.getStonesCapturedBy(Color.White), is(equalTo(0)));
        assertThat("capturing stones remain", stdBoard.getColorAt(0, 1), is(Color.Black));
        assertThat("capturing stones remain", stdBoard.getColorAt(1, 0), is(Color.Black));
        assertThat("corner now illegal for white", !stdBoard.isLegal(Move.get(Color.White, 0, 0)));
        assertThat("corner legal for black", stdBoard.isLegal(Move.get(Color.Black, 0, 0)));
    }

    @Test
    public void testMultiCapture() {
        stdBoard.play(Move.get(Color.Black, 1, 1));
        stdBoard.play(Move.get(Color.White, 0, 1));
        stdBoard.play(Move.get(Color.Black, 1, 0));
        stdBoard.play(Move.get(Color.White, 0, 0));
        stdBoard.play(Move.get(Color.Black, 0, 2));
        assertThat("immediate neighbor was captured", stdBoard.getColorAt(0, 1), is(nullValue()));
        assertThat("other stone was captured", stdBoard.getColorAt(0, 0), is(nullValue()));
        assertEquals(2, stdBoard.getStonesCapturedBy(Color.Black));
        assertEquals(0, stdBoard.getStonesCapturedBy(Color.White));
    }

    @Test
    public void testSimpleKo() {
        stdBoard.play(Move.get(Color.Black, 5, 5));
        stdBoard.play(Move.get(Color.White, 5, 6));
        stdBoard.play(Move.get(Color.Black, 6, 4));
        stdBoard.play(Move.get(Color.White, 6, 7));
        stdBoard.play(Move.get(Color.Black, 7, 5));
        stdBoard.play(Move.get(Color.White, 7, 6));
        stdBoard.play(Move.get(Color.Black, 6, 6));
        assertThat("surrounded but capturing", stdBoard.isLegal(Move.get(Color.White, 6, 5)));
        stdBoard.play(Move.get(Color.White, 6, 5));
        assertThat("initial capture occurred", stdBoard.getColorAt(6, 6), is(nullValue()));
        assertThat("simple ko fact", stdBoard.getKoMove().equals(Move.get(Color.Black, 6, 6)));
        assertThat("simple ko illegality", !stdBoard.isLegal(Move.get(Color.Black, 6, 6)));
        stdBoard.play(Move.get(Color.Black, 8, 8));
        assertThat("no longer ko", stdBoard.isLegal(Move.get(Color.Black, 6, 6)));
    }

    @Test
    public void testSimpleKo_PassReset() {
        stdBoard.play(Move.get(Color.Black, 5, 5));
        stdBoard.play(Move.get(Color.White, 5, 6));
        stdBoard.play(Move.get(Color.Black, 6, 4));
        stdBoard.play(Move.get(Color.White, 6, 7));
        stdBoard.play(Move.get(Color.Black, 7, 5));
        stdBoard.play(Move.get(Color.White, 7, 6));
        stdBoard.play(Move.get(Color.Black, 6, 6));
        stdBoard.play(Move.get(Color.White, 6, 5));
        assertThat("move is ko", stdBoard.getKoMove().equals(Move.get(Color.Black, 6, 6)));
        stdBoard.play(Move.get(Color.Black, Vertex.PASS));
        assertThat("no longer ko", stdBoard.getKoMove(), is(nullValue()));
    }

    @Test
    public void testPassing() {
        assertTrue(stdBoard.getLegalMoveVertices(Color.Black).contains(Vertex.PASS));
        assertTrue(stdBoard.getLegalMoveVertices(Color.White).contains(Vertex.PASS));
        assertEquals(0, stdBoard.getConsecutivePasses());
        stdBoard.play(Move.get(Color.Black, 5, 5));
        assertEquals(0, stdBoard.getConsecutivePasses());
        stdBoard.play(Move.get(Color.White, Vertex.PASS));
        assertEquals(1, stdBoard.getConsecutivePasses());
        stdBoard.play(Move.get(Color.Black, 5, 6));
        assertEquals(0, stdBoard.getConsecutivePasses());
        stdBoard.play(Move.get(Color.White, Vertex.PASS));
        assertEquals(1, stdBoard.getConsecutivePasses());
        stdBoard.play(Move.get(Color.Black, Vertex.PASS));
        assertEquals(2, stdBoard.getConsecutivePasses());
    }

    @Test
    public void testPassingLeavesZobristPositionUnchanged() {
        stdBoard.play(Move.get(Color.Black, Vertex.PASS));
        assertEquals(0, stdBoard.getZobristHash());
        stdBoard.play(Move.get(Color.White, 5, 5));
        long hash = stdBoard.getZobristHash();
        stdBoard.play(Move.get(Color.Black, Vertex.PASS));
        assertEquals(hash, stdBoard.getZobristHash());
    }

    @Test
    public void testGetAllVertices() {
        Collection<Vertex> allVertices = stdBoard.getAllVertices();
        assertEquals(stdBoard.getRows() * stdBoard.getColumns(), allVertices.size());
        for (int i = 0; i < stdBoard.getRows(); i++)
            for (int j = 0; j < stdBoard.getColumns(); j++)
                assertTrue(allVertices.contains(stdBoard.getVertexAt(i, j)));
    }

    @Test
    public void testHashHistory() {
        assertEquals(0, stdBoard.getPreviousHashes().size());
        stdBoard.play(Move.get(Color.Black, 17, 17));
        assertEquals(1, stdBoard.getPreviousHashes().size());
        assertThat("current is not in previous", !stdBoard.getPreviousHashes().contains(stdBoard.getZobristHash()));
        playSomeStuff(stdBoard);
        assertThat("more play", stdBoard.getPreviousHashes().size(), is(greaterThan(10)));
        assertThat("current still not previous", !stdBoard.getPreviousHashes().contains(stdBoard.getZobristHash()));
    }

    @Test
    public void testCopyAccuracy() {
        playSomeStuff(stdBoard);
        stdBoard.setKomi(5.5F);
        DefaultBoard copy = stdBoard.getCopy();
        for (int i = 0; i < 19; i++)
            for (int j = 0; j < 19; j++)
                assertEquals(stdBoard.getColorAt(i, j), copy.getColorAt(i, j));
        assertEquals(stdBoard.getZobristHash(), copy.getZobristHash());
        assertEquals(stdBoard.getLastMove(), copy.getLastMove());
        assertEquals(stdBoard.getKoMove(), copy.getKoMove());
        assertEquals(stdBoard.getKomi(), copy.getKomi(), 0.1F);
    }

    @Test
    public void testCopyIndependence() {
        DefaultBoard copy = stdBoard.getCopy();
        playSomeStuff(stdBoard);
        for (int i = 0; i < 19; i++)
            for (int j = 0; j < 19; j++)
                assertEquals(null, copy.getColorAt(i, j));
        assertEquals(19 * 19 + 1, copy.getLegalMoveVertices(Color.Black).size());
        assertEquals(0, copy.getPreviousHashes().size());
    }

    @Test
    public void testInterfaceCopyAccuracy() {
        playSomeStuff(stdBoard);
        stdBoard.setKomi(5.5F);
        Board iBoard = stdBoard;
        DefaultBoard copy = stdBoard.getCopy(iBoard);
        for (int i = 0; i < 19; i++)
            for (int j = 0; j < 19; j++)
                assertEquals(iBoard.getColorAt(i, j), copy.getColorAt(i, j));
        assertEquals(iBoard.getZobristHash(), copy.getZobristHash());
        assertEquals(iBoard.getLastMove(), copy.getLastMove());
        assertEquals(iBoard.getKoMove(), copy.getKoMove());
        assertEquals(iBoard.getKomi(), copy.getKomi(), 0.1F);
    }

    @Test
    public void testInterfaceCopyIndependence() {
        Board iBoard = stdBoard;
        DefaultBoard copy = stdBoard.getCopy(iBoard);
        playSomeStuff(iBoard);
        for (int i = 0; i < 19; i++)
            for (int j = 0; j < 19; j++)
                assertEquals(null, copy.getColorAt(i, j));
        assertEquals(19 * 19 + 1, copy.getLegalMoveVertices(Color.Black).size());
    }

    @Test
    public void testCopySizePreservation() {
        Board iBoard = stdBoard;
        Board newStdTypeChangedBoard = asymBoard.getCopy(iBoard);
        assertEquals(stdBoard.getRows(), newStdTypeChangedBoard.getRows());
        assertEquals(stdBoard.getColumns(), newStdTypeChangedBoard.getColumns());
        Board asymCopy = asymBoard.getCopy();
        assertEquals(asymBoard.getRows(), asymCopy.getRows());
        assertEquals(asymBoard.getColumns(), asymCopy.getColumns());
    }

    @Test
    public void testCornerIntersections() {
        DefaultBoard.Intersection[] corners = new DefaultBoard.Intersection[] {
            stdBoard.new Intersection(0, 0),
            stdBoard.new Intersection(0, stdBoard.getColumns() - 1),
            stdBoard.new Intersection(stdBoard.getRows() - 1, 0),
            stdBoard.new Intersection(stdBoard.getRows() - 1, stdBoard.getColumns() - 1),
            asymBoard.new Intersection(0, 0),
            asymBoard.new Intersection(0, asymBoard.getColumns() - 1),
            asymBoard.new Intersection(asymBoard.getRows() - 1, 0),
            asymBoard.new Intersection(asymBoard.getRows() - 1, asymBoard.getColumns() - 1)
        };
        for (DefaultBoard.Intersection corner : corners) {
            corner.initGeometry();
            assertThat("corner neighbors of (" + corner.getRow() + ", " + corner.getColumn() + ")",
                       corner.getNeighbors().size(), is(equalTo(2)));
            assertThat("corner diagonal of (" + corner.getRow() + ", " + corner.getColumn() + ")",
                       corner.getDiagonals().size(), is(equalTo(1)));
        }
    }

    @Test
    public void testEdgeIntersections() {
        List<DefaultBoard.Intersection> edges = new ArrayList<>();
        for (int i = 1; i < stdBoard.getRows() - 1; i++) {
            edges.add(stdBoard.new Intersection(i, 0));
            edges.add(stdBoard.new Intersection(i, stdBoard.getColumns() - 1));
            edges.add(stdBoard.new Intersection(0, i));
            edges.add(stdBoard.new Intersection(stdBoard.getRows() - 1, i));
        }
        for (int i = 1; i < asymBoard.getRows() - 1; i++) {
            edges.add(asymBoard.new Intersection(i, 0));
            edges.add(asymBoard.new Intersection(i, asymBoard.getColumns() - 1));
        }
        for (int i = 1; i < asymBoard.getColumns() - 1 ; i++) {
            edges.add(asymBoard.new Intersection(0, i));
            edges.add(asymBoard.new Intersection(asymBoard.getRows() - 1, i));
        }
        for (DefaultBoard.Intersection edge : edges) {
            edge.initGeometry();
            assertThat("edge neighbors of (" + edge.getRow() + ", " + edge.getColumn() + ")",
                       edge.getNeighbors().size(), is(equalTo(3)));
            assertThat("edge diagonals of (" + edge.getRow() + ", " + edge.getColumn() + ")",
                       edge.getDiagonals().size(), is(equalTo(2)));
        }
    }

    @Test
    public void testMiddleIntersections() {
        List<DefaultBoard.Intersection> mids = new ArrayList<>();
        for (int i = 1; i < stdBoard.getRows() - 1; i++)
            for (int j = 1; j < stdBoard.getColumns() - 1; j++)
                mids.add(stdBoard.new Intersection(i, j));
        for (int i = 1; i < asymBoard.getRows() - 1; i++)
            for (int j = 1; j < asymBoard.getColumns() - 1; j++)
                mids.add(asymBoard.new Intersection(i, j));
        for (DefaultBoard.Intersection mid : mids) {
            mid.initGeometry();
            assertThat("neighbors of (" + mid.getRow() + ", " + mid.getColumn() + ")",
                       mid.getNeighbors().size(), is(equalTo(4)));
            assertThat("diagonals of (" + mid.getRow() + ", " + mid.getColumn() + ")",
                       mid.getDiagonals().size(), is(equalTo(4)));
        }
    }

    private void playSomeStuff(Board board) {
        board.play(Move.get(Color.Black, 3, 3));
        board.play(Move.get(Color.White, 3, 16));
        board.play(Move.get(Color.Black, 4, 17));
        board.play(Move.get(Color.White, 2, 2));
        board.play(Move.get(Color.Black, 15, 3));
        board.play(Move.get(Color.White, 8, 7));
        board.play(Move.get(Color.Black, 5, 5));
        board.play(Move.get(Color.White, 5, 6));
        board.play(Move.get(Color.Black, 6, 4));
        board.play(Move.get(Color.White, 6, 7));
        board.play(Move.get(Color.Black, 7, 5));
        board.play(Move.get(Color.White, 7, 6));
        board.play(Move.get(Color.Black, 6, 6));
        board.play(Move.get(Color.White, 6, 5));
        board.play(Move.get(Color.Black, 8, 8));
    }
}
