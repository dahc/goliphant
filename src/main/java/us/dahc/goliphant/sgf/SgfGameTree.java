package us.dahc.goliphant.sgf;

import us.dahc.goliphant.go.Board;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SgfGameTree {

    private SgfGameTree rootGameTree;
    private Board setupBoard;
    private List<SgfGameTree> children;
    private List<SgfNode> nodes;

    public SgfGameTree(Board setupBoard) {
        this((SgfGameTree) null);
        this.setupBoard = setupBoard;
    }

    public SgfGameTree(SgfGameTree rootGameTree) {
        this.rootGameTree = rootGameTree;
        this.setupBoard = null;
        nodes = new ArrayList<>();
        children = new ArrayList<>();
    }

    public Board getSetupBoard() {
        return getRootGameTree().setupBoard;
    }

    public SgfGameTree getRootGameTree() {
        return rootGameTree == null ? this : rootGameTree;
    }

    int parse(byte[] bytes, int position) throws ParseException {
        int start = position;
        while (++position < bytes.length) {
            switch (bytes[position]) {
                case ')':
                    return position;
                case '(':
                    SgfGameTree gameTree = new SgfGameTree(getRootGameTree());
                    position = gameTree.parse(bytes, position);
                    children.add(gameTree);
                    break;
                case ';':
                    SgfNode node = new SgfNode(getRootGameTree());
                    position = node.parse(bytes, position);
                    nodes.add(node);
                    break;
            }
        }
        throw new ParseException("no matching close paren found ')'", start);
    }

    public Collection<SgfGameTree> getChildGameTrees() {
        return children;
    }

    public Collection<SgfNode> getNodes() {
        return nodes;
    }

}
