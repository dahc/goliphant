package us.dahc.goliphant.gtp;

import org.apache.commons.lang3.StringUtils;
import us.dahc.goliphant.core.Board;
import us.dahc.goliphant.core.Color;
import us.dahc.goliphant.core.FilterList;
import us.dahc.goliphant.core.InvalidSizeException;
import us.dahc.goliphant.core.Move;
import us.dahc.goliphant.core.SuperkoFilter;
import us.dahc.goliphant.core.Vertex;
import us.dahc.goliphant.core.ApplicationIdentity;
import us.dahc.goliphant.util.BoardPrettyPrinter;
import us.dahc.goliphant.util.StarPointHelper;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class StatefulGtpHandler extends BaseGtpHandler {

    protected Board currentBoard;
    protected FilterList filterList;
    protected List<Board> pastBoards;

    @Inject
    public StatefulGtpHandler(ApplicationIdentity clientIdentity, Board board) {
        super(clientIdentity);
        pastBoards = new ArrayList<>();
        currentBoard = board;
        filterList = new FilterList();
        filterList.add(new SuperkoFilter());
        commands.put("boardsize", new BoardsizeCommand());
        commands.put("clear_board", new ClearBoardCommand());
        commands.put("showboard", new ShowBoardCommand());
        commands.put("komi", new KomiCommand());
        commands.put("get_komi", new GetKomiCommand());
        commands.put("play", new PlayCommand());
        commands.put("undo", new UndoCommand());
        commands.put("set_free_handicap", new SetFreeHandicapCommand());
        commands.put("place_free_handicap", new PlaceFreeHandicapCommand());
        commands.put("fixed_handicap", new FixedHandicapCommand());
    }

    protected class BoardsizeCommand implements BaseGtpHandler.Command {
        @Override
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
        @Override
        public String exec(String... args) {
            pastBoards.clear();
            currentBoard.reset();
            return StringUtils.EMPTY;
        }
    }

    protected class ShowBoardCommand implements BaseGtpHandler.Command {
        @Override
        public String exec(String... args) {
            return "\n" + BoardPrettyPrinter.getPrettyString(currentBoard);
        }
    }

    protected class KomiCommand implements BaseGtpHandler.Command {
        @Override
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
        @Override
        public String exec(String... args) throws GtpException {
            return String.format("%.1f", currentBoard.getKomi());
        }
    }

    protected class PlayCommand implements BaseGtpHandler.Command {
        @Override
        public String exec(String... args) throws GtpException {
            Move move;
            try {
                move = Move.get(args[0], args[1]);
            } catch (Exception e) {
                throw new GtpException("invalid color or coordinate");
            }
            if (filterList.apply(currentBoard, move.getColor()).contains(move)) {
                pastBoards.add(currentBoard.getCopy());
                currentBoard.play(move);
            } else {
                throw new GtpException("illegal move");
            }
            return StringUtils.EMPTY;
        }
    }

    protected class UndoCommand implements BaseGtpHandler.Command {
        @Override
        public String exec(String... args) throws GtpException {
            if (pastBoards.size() > 0) {
                currentBoard = pastBoards.remove(pastBoards.size() - 1);
            } else {
                throw new GtpException("cannot undo");
            }
            return StringUtils.EMPTY;
        }
    }

    protected class SetFreeHandicapCommand implements BaseGtpHandler.Command {
        @Override
        public String exec(String... args) throws GtpException {
            if (currentBoard.getZobristHash() != 0)
                throw new GtpException("board not empty");
            if (args.length < 2)
                throw new GtpException("invalid handicap");
            Set<Vertex> stones = new HashSet<>();
            try {
                for (String arg : args)
                    stones.add(Vertex.get(arg));
            } catch (Exception e) {
                throw new GtpException("invalid coordinate");
            }
            if (stones.contains(Vertex.PASS))
                throw new GtpException("invalid coordinate");
            if (stones.size() < args.length)
                throw new GtpException("repeated vertex");
            for (Vertex stone : stones)
                currentBoard.play(Move.get(Color.Black, stone));
            return StringUtils.EMPTY;
        }
    }

    protected class FixedHandicapCommand implements BaseGtpHandler.Command {
        @Override
        public String exec(String... args) throws GtpException {
            if (currentBoard.getZobristHash() != 0)
                throw new GtpException("board not empty");
            int handicap;
            try {
                handicap = Integer.valueOf(args[0]);
            } catch (Exception e) {
                throw new GtpException("handicap not an integer");
            }
            if (handicap < 2 || handicap > 9)
                throw new GtpException("invalid handicap");
            StringBuilder stringBuilder = new StringBuilder();
            for (Vertex stone : StarPointHelper.getHandicapPoints(currentBoard, handicap)) {
                currentBoard.play(Move.get(Color.Black, stone));
                stringBuilder.append(stone.toString()).append(' ');
            }
            return stringBuilder.toString();
        }
    }

    protected class PlaceFreeHandicapCommand extends FixedHandicapCommand {
        @Override
        public String exec(String... args) throws GtpException {
            if (currentBoard.getZobristHash() != 0)
                throw new GtpException("board not empty");
            int handicap;
            try {
                handicap = Integer.valueOf(args[0]);
            } catch (Exception e) {
                throw new GtpException("handicap not an integer");
            }
            if (handicap > 9)
                return super.exec("9");
            else
                return super.exec(args[0]);
        }
    }
}
