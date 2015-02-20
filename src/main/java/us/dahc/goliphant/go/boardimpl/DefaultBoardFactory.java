package us.dahc.goliphant.go.boardimpl;

import javax.inject.Inject;

import us.dahc.goliphant.go.Board;
import us.dahc.goliphant.go.BoardFactory;
import us.dahc.goliphant.go.hashing.ZobristTableSource;

public class DefaultBoardFactory implements BoardFactory {

    private ZobristTableSource zobristTableSource;

    @Inject
    public DefaultBoardFactory(ZobristTableSource zobristTableSource) {
        this.zobristTableSource = zobristTableSource;
    }

    public Board create(int rows, int columns) {
        return new DefaultBoard(zobristTableSource.get(rows, columns));
    }

    public Board copy(Board board) {
        if (board instanceof DefaultBoard)
            return new DefaultBoard((DefaultBoard) board);
        else
            return new DefaultBoard(zobristTableSource.get(board.getRows(), board.getColumns()), board);
    }

}
