package us.dahc.goliphant.gtp;

import org.apache.commons.lang3.StringUtils;
import us.dahc.goliphant.go.Board;
import us.dahc.goliphant.go.InvalidSizeException;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class StatefulGtpHandler extends BaseGtpHandler {

    protected Board currentBoard;
    protected List<Board> pastBoards;

    @Inject
    public StatefulGtpHandler(GtpClientIdentity clientIdentity, Board board) {
        super(clientIdentity);
        pastBoards = new ArrayList<Board>();
        currentBoard = board;
        commands.put("boardsize", new BoardsizeCommand());
        commands.put("clear_board", new ClearBoardCommand());
        commands.put("showboard", new ShowBoardCommand());
    }

    class BoardsizeCommand implements BaseGtpHandler.Command {
        public String exec(String... args) throws GtpException {
            try {
                int newSize = Integer.valueOf(args[0]);
                currentBoard.resize(newSize, newSize);
            } catch (InvalidSizeException e) {
                throw new GtpException(e.getMessage());
            } catch (Exception e) {
                throw new GtpException("boardsize not an integer");
            }
            pastBoards.clear();
            return StringUtils.EMPTY;
        }
    }

    class ClearBoardCommand implements BaseGtpHandler.Command {
        public String exec(String... args) {
            pastBoards.clear();
            currentBoard.reset();
            return StringUtils.EMPTY;
        }
    }

    class ShowBoardCommand implements BaseGtpHandler.Command {
        public String exec(String... args) {
            return "\n" + currentBoard.getPrettyString();
        }
    }
}
