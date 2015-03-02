package us.dahc.goliphant.sgf;

import us.dahc.goliphant.go.Color;
import us.dahc.goliphant.go.Move;
import us.dahc.goliphant.go.Vertex;

import javax.annotation.Nullable;
import java.util.HashMap;

public class SgfNode extends HashMap<String, String> {

    public static final String COORDINATE_LETTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private SgfGameTree rootGameTree;

    public SgfNode(SgfGameTree rootGameTree) {
        super();
        this.rootGameTree = rootGameTree;
    }

    public SgfGameTree getRootGameTree() {
        return rootGameTree;
    }

    @Nullable
    public Move getMove() throws SgfException {
        if (containsKey("W"))
            return new Move(Color.White, parseVertex(get("W")));
        else if (containsKey("B"))
            return new Move(Color.Black, parseVertex(get("B")));
        else
            return null;
    }

    int parse(byte[] bytes, int position) throws SgfException {
        while (++position < bytes.length) {
            switch (bytes[position]) {
                case ' ':
                case '\t':
                case '\n':
                case '\r':
                    break;
                case ';':
                case ')':
                case '(':
                    return position - 1;
                default:
                    position = parseProperty(bytes, position);
                    break;
            }
        }
        return position;
    }

    int parseProperty(byte[] bytes, int position) throws SgfException {
        StringBuilder sbp = new StringBuilder(2);
        boolean propertyDone = false;
        while (position < bytes.length && !propertyDone) {
            if (bytes[position] < 0)
                throw new SgfException(String.format("unexpected (non-ASCII) byte 0x%2X", bytes[position]));
            if (Character.isAlphabetic(bytes[position]) && Character.isUpperCase(bytes[position])) {
                sbp.append((char) bytes[position]);
            } else if (bytes[position] == '[') {
                propertyDone = true;
            }
            position++;
        }
        if (!propertyDone)
            throw new SgfException("unended property");
        StringBuilder sbv = new StringBuilder();
        boolean propertyValueDone = false;
        while (position < bytes.length) {
            if (bytes[position] < 0)
                throw new SgfException(String.format("unexpected (non-ASCII) byte 0x%2X", bytes[position]));
            if (bytes[position] == ']' && (position == bytes.length - 1 || bytes[position + 1] != '[')) {
                propertyValueDone = true;
                break;
            } else {
                sbv.append((char) bytes[position]);
            }
            position++;
        }
        if (!propertyValueDone)
            throw new SgfException("unended property value for '" + sbp.toString() + "'");
        put(sbp.toString(), sbv.toString());
        return position;
    }

    Vertex parseVertex(String string) throws SgfException {
        try {
            return rootGameTree.getSetupBoard().getVertexAt(
                    rootGameTree.getSetupBoard().getRows() - COORDINATE_LETTERS.indexOf(string.charAt(0)) - 1,
                    COORDINATE_LETTERS.indexOf(string.charAt(1)));
        } catch (Exception e) {
            throw new SgfException("bad vertex", e);
        }
    }

}
