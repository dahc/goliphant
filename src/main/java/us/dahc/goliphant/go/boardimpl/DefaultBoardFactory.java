package us.dahc.goliphant.go.boardimpl;

import javax.inject.Inject;

import us.dahc.goliphant.go.Board;
import us.dahc.goliphant.go.BoardFactory;
import us.dahc.goliphant.go.hashing.ZobristTableSource;

public class DefaultBoardFactory implements BoardFactory {

    @Inject
    private ZobristTableSource zobristTableSource;

    public Board create(int rows, int columns) {
        return (Board) new DefaultBoard(zobristTableSource.get(rows, columns));
    }

    public Board copy(Board board) {
        // TODO Auto-generated method stub
        return null;
    }

}
