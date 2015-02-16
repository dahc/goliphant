package us.dahc.goliphant.go;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class DefaultBoardTest {
    public DefaultBoard stdBoard;
    public DefaultBoard asymBoard;

    @Before
    public void setup() {
        stdBoard = new DefaultBoard(19, 19);
        asymBoard = new DefaultBoard(13, 21);
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
                        stdBoard.isLegal(new Move(Color.Black, i, j)), is(true));
            }
        }
        assertEquals(19 * 19, stdBoard.getLegalMoves(Color.Black).size());
        assertEquals(19 * 19, stdBoard.getLegalMovesIgnoringSuperKo(Color.Black).size());
    }

    @Test
    public void testFastPlay() {
        assertEquals(null, stdBoard.getColorAt(3, 3));
        stdBoard.fastPlay(new Move(Color.Black, 3, 3));
        assertEquals(Color.Black, stdBoard.getColorAt(3, 3));
        assertThat("play on other stone", stdBoard.isLegal(new Move(Color.White, 3, 3)), is(false));
        for (int i = 0; i < stdBoard.getRows(); i++)
            for (int j = 0; j < stdBoard.getColumns(); j++)
                if (i != 3 || j != 3)
                    assertThat("wide open square (" + i + ", " + j + ") legality",
                            stdBoard.isLegal(new Move(Color.White, i, j)), is(true));
        assertEquals(19 * 19 - 1, stdBoard.getLegalMoves(Color.White).size());
        assertEquals(19 * 19 - 1, stdBoard.getLegalMovesIgnoringSuperKo(Color.White).size());
    }

    @Test
    public void testCopyAccuracy() {
        playSomeStuff(stdBoard);
        Board copy = new DefaultBoard(stdBoard);
        for (int i = 0; i < 19; i++)
            for (int j = 0; j < 19; j++)
                assertEquals(stdBoard.getColorAt(i, j), copy.getColorAt(i, j));
    }

    @Test
    public void testCopyIndependence() {
        Board copy = new DefaultBoard(stdBoard);
        playSomeStuff(stdBoard);
        for (int i = 0; i < 19; i++)
            for (int j = 0; j < 19; j++)
                assertEquals(null, copy.getColorAt(i, j));
        assertEquals(19 * 19, copy.getLegalMoves(Color.Black).size());
    }

    @Test
    public void testCornerIntersections() {
        DefaultBoard.Intersection[] corners = new DefaultBoard.Intersection[] {
            stdBoard.new Intersection(0, 0),
            stdBoard.new Intersection(0, 18),
            stdBoard.new Intersection(18, 0),
            stdBoard.new Intersection(18, 18)
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
        List<DefaultBoard.Intersection> edges = new ArrayList<DefaultBoard.Intersection>(17 * 4);
        for (int i = 1; i < 18; i++) {
            edges.add(stdBoard.new Intersection(i, 0));
            edges.add(stdBoard.new Intersection(i, 18));
            edges.add(stdBoard.new Intersection(0, i));
            edges.add(stdBoard.new Intersection(18, i));
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
        List<DefaultBoard.Intersection> mids = new ArrayList<DefaultBoard.Intersection>(17 * 17);
        for (int i = 1; i < 18; i++)
            for (int j = 1; j < 18; j++)
                mids.add(stdBoard.new Intersection(i, j));
        for (DefaultBoard.Intersection mid : mids) {
            mid.initGeometry();
            assertThat("neighbors of (" + mid.getRow() + ", " + mid.getColumn() + ")",
                       mid.getNeighbors().size(), is(equalTo(4)));
            assertThat("diagonals of (" + mid.getRow() + ", " + mid.getColumn() + ")",
                       mid.getDiagonals().size(), is(equalTo(4)));
        }
    }

    private void playSomeStuff(Board board) {
        board.fastPlay(new Move(Color.Black, 3, 3));
        board.fastPlay(new Move(Color.White, 3, 16));
        board.fastPlay(new Move(Color.Black, 4, 17));
        board.fastPlay(new Move(Color.White, 2, 2));
        board.fastPlay(new Move(Color.Black, 15, 3));
        board.fastPlay(new Move(Color.White, 8, 7));
    }
}
