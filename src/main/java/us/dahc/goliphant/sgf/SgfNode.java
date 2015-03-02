package us.dahc.goliphant.sgf;

import us.dahc.goliphant.go.Color;
import us.dahc.goliphant.go.Move;
import us.dahc.goliphant.go.Vertex;

import javax.annotation.Nullable;
import java.text.ParseException;
import java.util.HashMap;

public class SgfNode extends HashMap<String, Object> {

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
    public Move getMove() {
        if (containsKey("W"))
            return new Move(Color.White, (Vertex) get("W"));
        else if (containsKey("B"))
            return new Move(Color.Black, (Vertex) get("B"));
        else
            return null;
    }

    public Object put(String key, String value, int position) throws ParseException {
        switch (key) {
            case "W":
            case "B":
                return super.put(key, parseVertex(value, position));
            default:
                return super.put(key, value);
        }
    }

    int parse(byte[] bytes, int position) throws ParseException {
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
                    return position;
                default:
                    position = parseProperty(bytes, position);
                    break;
            }
        }
        return position;
    }

    int parseProperty(byte[] bytes, int position) throws ParseException {
        StringBuilder sbp = new StringBuilder(2);
        boolean propertyDone = false;
        while (position < bytes.length && !propertyDone) {
            if (bytes[position] < 0)
                throw new ParseException(String.format("unexpected (non-ASCII) byte 0x%2X", bytes[position]), position);
            if (Character.isAlphabetic(bytes[position]) && Character.isUpperCase(bytes[position])) {
                sbp.append((char) bytes[position]);
            } else if (bytes[position] == '[') {
                propertyDone = true;
            }
            position++;
        }
        if (!propertyDone)
            throw new ParseException("unended property", position);
        StringBuilder sbv = new StringBuilder();
        boolean propertyValueDone = false;
        while (position < bytes.length && !propertyValueDone) {
            if (bytes[position] < 0)
                throw new ParseException(String.format("unexpected (non-ASCII) byte 0x%2X", bytes[position]), position);
            if (bytes[position] == ']') {
                propertyValueDone = true;
                break;
            } else {
                sbv.append((char) bytes[position]);
            }
            position++;
        }
        if (!propertyValueDone)
            throw new ParseException("unended property value for '" + sbp.toString() + "'", position);
        put(sbp.toString(), sbv.toString(), position);
        return position;
    }

    Vertex parseVertex(String string, int position) throws ParseException {
        try {
            return rootGameTree.getSetupBoard().getVertexAt(
                    rootGameTree.getSetupBoard().getRows() - COORDINATE_LETTERS.indexOf(string.charAt(0)) - 1,
                    COORDINATE_LETTERS.indexOf(string.charAt(1)));
        } catch (Exception e) {
            throw new ParseException("bad vertex: " + e, position);
        }
    }

}
