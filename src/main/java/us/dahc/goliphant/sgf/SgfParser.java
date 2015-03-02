package us.dahc.goliphant.sgf;

import us.dahc.goliphant.go.Board;

import javax.inject.Inject;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class SgfParser {

    private Board board;

    @Inject
    public SgfParser(Board board) {
        this.board = board;
    }

    public SgfGameTree parseFile(String pathToFile) throws IOException, SgfException {
        return parse(Files.readAllBytes(Paths.get(pathToFile)));
    }

    public SgfGameTree parse(byte[] bytes) throws SgfException {
        SgfGameTree gameTree = new SgfGameTree(board);
        gameTree.parse(bytes, 0);
        return gameTree;
    }

}
