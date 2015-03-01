package us.dahc.goliphant.go;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PositionalSuperkoWrapper extends AbstractBoardWrapper {

    @Inject
    public PositionalSuperkoWrapper(Board board) {
        super(board);
    }

    @Override
    public Board getCopy() {
        return new PositionalSuperkoWrapper(wrappedBoard.getCopy());
    }

    @Override
    public Board getCopy(Board board) {
        return new PositionalSuperkoWrapper(wrappedBoard.getCopy(board));
    }

    @Override
    public boolean isLegal(Move move) {
        if (wrappedBoard.isLegal(move)) {
            Board test = wrappedBoard.getCopy();
            test.play(move);
            if (test.getPreviousHashes().contains(test.getZobristHash()))
                return false;
            else
                return true;
        } else {
            return false;
        }
    }

    @Override
    public Collection<? extends Vertex> getLegalMoveVertices(Color player) {
        Collection<? extends Vertex> vertices = wrappedBoard.getLegalMoveVertices(player);
        List<? extends Vertex> legalVertices = new ArrayList<>(vertices);
        for (Vertex vertex : vertices)
            if (!isLegal(new Move(player, vertex)))
                legalVertices.remove(vertex);
        return legalVertices;
    }

}