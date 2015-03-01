package us.dahc.goliphant.sgf;

import us.dahc.goliphant.go.Board;

import javax.inject.Inject;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;

public class SgfParser {

    private Board board;

    @Inject
    public SgfParser(Board board) {
        this.board = board;
    }

    public Collection<SgfGameTree> parseFile(String pathToFile) throws IOException, ParseException {
        return parse(Files.readAllBytes(Paths.get(pathToFile)));
    }

    public Collection<SgfGameTree> parse(byte[] bytes) throws ParseException {
        Collection<SgfGameTree> gameTrees = new ArrayList<>();

        return gameTrees;
    }


}
