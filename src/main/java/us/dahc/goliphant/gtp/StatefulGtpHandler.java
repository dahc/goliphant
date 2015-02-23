package us.dahc.goliphant.gtp;

import org.apache.commons.lang3.StringUtils;
import us.dahc.goliphant.go.Board;
import us.dahc.goliphant.go.InvalidSizeException;
import us.dahc.goliphant.go.Move;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class StatefulGtpHandler extends BaseGtpHandler {

    protected Board currentBoard;
    protected List<Board> pastBoards;

    @Inject
    public StatefulGtpHandler(GtpClientIdentity clientIdentity, Board board) {
        super(clientIdentity);
        pastBoards = new ArrayList<>();
        currentBoard = board;
        commands.put("boardsize", new BoardsizeCommand());
        commands.put("clear_board", new ClearBoardCommand());
        commands.put("showboard", new ShowBoardCommand());
        commands.put("komi", new KomiCommand());
        commands.put("get_komi", new GetKomiCommand());
        commands.put("play", new PlayCommand());
    }

    protected class BoardsizeCommand implements BaseGtpHandler.Command {
        public String exec(String... args) throws GtpException {
            try {
                int size = Integer.valueOf(args[0]);
                currentBoard.resize(size, size);
            } catch (InvalidSizeException e) {
                throw new GtpException(e.getMessage());
            } catch (Exception e) {
                throw new GtpException("boardsize not an integer");
            }
            pastBoards.clear();
            return StringUtils.EMPTY;
        }
    }

    protected class ClearBoardCommand implements BaseGtpHandler.Command {
        public String exec(String... args) {
            pastBoards.clear();
            currentBoard.reset();
            return StringUtils.EMPTY;
        }
    }

    protected class ShowBoardCommand implements BaseGtpHandler.Command {
        public String exec(String... args) {
            return "\n" + currentBoard.getPrettyString();
        }
    }

    protected class KomiCommand implements BaseGtpHandler.Command {
        public String exec(String... args) throws GtpException {
            try {
                currentBoard.setKomi(Float.valueOf(args[0]));
            } catch (Exception e) {
                throw new GtpException("komi not a float");
            }
            return StringUtils.EMPTY;
        }
    }

    protected class GetKomiCommand implements BaseGtpHandler.Command {
        public String exec(String... args) throws GtpException {
            return String.format("%.1f", currentBoard.getKomi());
        }
    }

    protected class PlayCommand implements BaseGtpHandler.Command {
        public String exec(String... args) throws GtpException {
            Move move;
            try {
                move = new Move(args[0], args[1]);
            } catch (Exception e) {
                throw new GtpException("invalid color or coordinate");
            }
            if (currentBoard.getLegalMoves(move.getColor()).contains(move)) {
                pastBoards.add(currentBoard.getCopy());
                currentBoard.play(move);
            } else {
                throw new GtpException("illegal move");
            }
            return StringUtils.EMPTY;
        }
    }

}
