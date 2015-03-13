package us.dahc.goliphant.core;

import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SuperkoFilterTest {

    private Board board;
    private FilterList filterList;

    @Before
    public void setup() {
        board = new DefaultBoard(new ZobristTable(new Random()));
        filterList = new FilterList();
        filterList.add(new SuperkoFilter());
    }

    @Test
    public void testWithoutSuperKo() {
        assertEquals(board.getRows() * board.getColumns() + 1, filterList.apply(board, Color.Black).size());
    }

    @Test
    public void testSuperKo() {
        board.play(Move.get(Color.Black, 0, 3));
        board.play(Move.get(Color.Black, 1, 1));
        board.play(Move.get(Color.Black, 1, 2));
        board.play(Move.get(Color.Black, 1, 3));
        board.play(Move.get(Color.White, 1, 0));
        board.play(Move.get(Color.White, 0, 1)); // repeatable
        board.play(Move.get(Color.White, 0, 2));
        board.play(Move.get(Color.Black, 0, 0));
        assertFalse(filterList.apply(board, Color.White).contains(Move.get(Color.White, 0, 1)));
        assertTrue(filterList.apply(board, Color.Black).contains(Move.get(Color.Black, 0, 1)));
    }

}