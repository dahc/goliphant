package us.dahc.goliphant.core.filters;

import org.junit.Before;
import org.junit.Test;
import us.dahc.goliphant.core.Board;
import us.dahc.goliphant.core.Color;
import us.dahc.goliphant.core.DefaultBoard;
import us.dahc.goliphant.core.Move;
import us.dahc.goliphant.core.ZobristTable;

import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class EyeLikeFilterTest {

    private Board board;
    private FilterList filterList;

    @Before
    public void setup() {
        board = new DefaultBoard(new ZobristTable(new Random()));
        filterList = new FilterList();
        filterList.add(new EyeLikeFilter());
    }

    @Test
    public void testWithEmptyBoard() {
        assertEquals(board.getRows() * board.getColumns() + 1, filterList.apply(board, Color.Black).size());
    }

    @Test
    public void testMiddleNotSurrounded() {
        board.play(Move.get(Color.Black, 1, 3));
        board.play(Move.get(Color.Black, 1, 1));
        board.play(Move.get(Color.Black, 0, 2));
        assertTrue(filterList.apply(board, Color.Black).contains(Move.get(Color.Black, 1, 2)));
    }

    @Test
    public void testMiddleWithCardinalFriendlies() {
        board.play(Move.get(Color.Black, 1, 3));
        board.play(Move.get(Color.Black, 1, 1));
        board.play(Move.get(Color.Black, 0, 2));
        board.play(Move.get(Color.Black, 2, 2));
        assertFalse(filterList.apply(board, Color.Black).contains(Move.get(Color.Black, 1, 2)));
    }

    @Test
    public void testMiddleWithOneDiagonalEnemy() {
        board.play(Move.get(Color.Black, 1, 3));
        board.play(Move.get(Color.Black, 1, 1));
        board.play(Move.get(Color.Black, 0, 2));
        board.play(Move.get(Color.Black, 2, 2));
        board.play(Move.get(Color.White, 2, 3));
        assertFalse(filterList.apply(board, Color.Black).contains(Move.get(Color.Black, 1, 2)));
    }

    @Test
    public void testMiddleWithTwoDiagonalEnemies() {
        board.play(Move.get(Color.Black, 1, 3));
        board.play(Move.get(Color.Black, 1, 1));
        board.play(Move.get(Color.Black, 0, 2));
        board.play(Move.get(Color.Black, 2, 2));
        board.play(Move.get(Color.White, 2, 3));
        board.play(Move.get(Color.White, 2, 1));
        assertTrue(filterList.apply(board, Color.Black).contains(Move.get(Color.Black, 1, 2)));
    }

    @Test
    public void testEdgeWithCardinalFriendlies() {
        board.play(Move.get(Color.Black, 5, 0));
        board.play(Move.get(Color.Black, 6, 1));
        board.play(Move.get(Color.Black, 7, 0));
        assertFalse(filterList.apply(board, Color.Black).contains(Move.get(Color.Black, 6, 0)));
    }

    @Test
    public void testEdgeWithOneDiagonalEnemy() {
        board.play(Move.get(Color.Black, 5, 0));
        board.play(Move.get(Color.Black, 6, 1));
        board.play(Move.get(Color.Black, 7, 0));
        board.play(Move.get(Color.White, 5, 1));
        assertTrue(filterList.apply(board, Color.Black).contains(Move.get(Color.Black, 6, 0)));
    }

}