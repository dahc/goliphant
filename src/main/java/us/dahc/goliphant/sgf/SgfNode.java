package us.dahc.goliphant.sgf;

import us.dahc.goliphant.go.Color;
import us.dahc.goliphant.go.Move;
import us.dahc.goliphant.go.Vertex;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.HashMap;

public class SgfNode {

    public static final String COORDINATE_LETTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private SgfGameTree rootGameTree;
    private HashMap<String, String> textMap;

    public SgfNode(SgfGameTree rootGameTree) {
        textMap = new HashMap<>();
        this.rootGameTree = rootGameTree;
    }

    public SgfGameTree getRootGameTree() {
        return rootGameTree;
    }

    public Collection<String> getProperties() {
        return textMap.keySet();
    }

    @Nullable
    public String getTextProperty(String property) {
        return textMap.get(property);
    }

    @Nullable
    public Move getMove() throws SgfException {
        if (textMap.containsKey("W"))
            return new Move(Color.White, parseVertex(textMap.get("W")));
        else if (textMap.containsKey("B"))
            return new Move(Color.Black, parseVertex(textMap.get("B")));
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
        textMap.put(sbp.toString(), sbv.toString());
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
