package us.dahc.goliphant.util;

import us.dahc.goliphant.go.Color;
import us.dahc.goliphant.go.Move;
import us.dahc.goliphant.go.Vertex;
import us.dahc.goliphant.gtp.GtpClientIdentity;

import javax.inject.Inject;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *  This is a simplified SGF parser and printer for the common case
 *  of a linear game with no need to represent branching.
 */
public class SgfGameLog {

    public static final String COORDINATE_LETTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private int boardRows;
    private int boardColumns;
    private int handicap;
    private float komi;
    private List<Move> moves;
    private Collection<Vertex> whitePlacements;
    private Collection<Vertex> blackPlacements;
    private GtpClientIdentity applicationIdentity;

    @Inject
    public SgfGameLog(GtpClientIdentity applicationIdentity) {
        moves = new ArrayList<>();
        whitePlacements = new ArrayList<>();
        blackPlacements = new ArrayList<>();
        this.applicationIdentity = applicationIdentity;
    }

    public void parse(byte[] sgfBytes) throws GoliphantException {
        int branch = 0;
        for (int i = 0; i < sgfBytes.length; i++) {
            if (Character.isAlphabetic((char) sgfBytes.length)) {
                // TODO: read property name and value(s)
            } else if (sgfBytes[i] == '(') {
                branch++;
                if (branch > 1)
                    throw new GoliphantException("unsupported branching in SGF file");
            }
        }
    }

    public void parseFile(String path) throws IOException, GoliphantException {
        parse(Files.readAllBytes(Paths.get(path)));
    }

    public String print() {
        StringBuilder stringBuilder = new StringBuilder(1024);
        stringBuilder.append("(;GM[1]SZ[").append(boardColumns);
        if (boardRows != boardColumns)
            stringBuilder.append(':').append(boardRows);
        stringBuilder.append("]KM[").append(String.format("%.1f", komi));
        stringBuilder.append("]HA[").append(handicap);
        stringBuilder.append("]AP[").append(applicationIdentity.getName()).append(':');
        stringBuilder.append(applicationIdentity.getVersion()).append(']');
        // TODO: Add placements and moves
        return stringBuilder.append(')').toString();
    }

    public int getBoardColumns() {
        return boardColumns;
    }

    public void setBoardColumns(int boardColumns) {
        this.boardColumns = boardColumns;
    }

    public int getBoardRows() {
        return boardRows;
    }

    public void setBoardRows(int boardRows) {
        this.boardRows = boardRows;
    }

    public int getHandicap() {
        return handicap;
    }

    public void setHandicap(int handicap) {
        this.handicap = handicap;
    }

    public float getKomi() {
        return komi;
    }

    public void setKomi(float komi) {
        this.komi = komi;
    }

    public List<Move> getMoves() {
        return moves;
    }

    public Collection<Vertex> getPlacements(Color color) {
        return color == Color.Black ? blackPlacements : whitePlacements;
    }

    private Vertex getVertexAt(String sgf, int index) {
        return new Vertex(COORDINATE_LETTERS.indexOf(sgf.charAt(index + 1)),
                COORDINATE_LETTERS.indexOf(sgf.charAt(boardRows - index - 1)));
    }

    private void appendVertex(StringBuilder stringBuilder, Vertex vertex) {
        stringBuilder.append(COORDINATE_LETTERS.charAt(vertex.getColumn()));
        stringBuilder.append(COORDINATE_LETTERS.charAt(boardRows - vertex.getRow() - 1));
    }

    @Override
    public String toString() {
        return print();
    }

}
