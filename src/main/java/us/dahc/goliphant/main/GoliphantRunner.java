package us.dahc.goliphant.main;

import us.dahc.goliphant.go.Board;
import us.dahc.goliphant.go.BoardFactory;
import us.dahc.goliphant.go.boardimpl.DefaultBoardFactory;
import us.dahc.goliphant.util.hashing.RandomZobristTableSource;

import java.util.Random;

public class GoliphantRunner {
    public static void main(String[] args) {
        BoardFactory bf = new DefaultBoardFactory(new RandomZobristTableSource(new Random()));

        Board b = bf.create(19, 19);

        Board c = bf.copy(b);

    }
}
