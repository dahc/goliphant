package us.dahc.goliphant.toys.randbot;

import us.dahc.goliphant.core.ApplicationIdentity;
import us.dahc.goliphant.core.Board;
import us.dahc.goliphant.core.Color;
import us.dahc.goliphant.core.Move;
import us.dahc.goliphant.core.filters.EyeLikeFilter;
import us.dahc.goliphant.core.filters.FilterList;
import us.dahc.goliphant.core.filters.SuperkoFilter;
import us.dahc.goliphant.gtp.BaseGtpHandler;
import us.dahc.goliphant.gtp.GtpException;
import us.dahc.goliphant.gtp.StatefulGtpHandler;

import javax.inject.Inject;
import java.util.List;
import java.util.Random;

public final class RandomGtpHandler extends StatefulGtpHandler {

    private Random random;
    private FilterList genMoveFilters;

    @Inject
    public RandomGtpHandler(ApplicationIdentity clientIdentity, Board board, Random random) {
        super(clientIdentity, board);
        this.random = random;
        genMoveFilters = new FilterList();
        genMoveFilters.add(new EyeLikeFilter());
        genMoveFilters.add(new SuperkoFilter());
        commands.put("genmove", new GenMoveCommand());
    }

    private class GenMoveCommand implements BaseGtpHandler.Command {
        @Override
        public String exec(String... args) throws GtpException {
            if (args.length < 1)
                throw new GtpException("invalid color");
            Color player;
            if (args[0].toUpperCase().charAt(0) == 'B')
                player = Color.Black;
            else if (args[0].toUpperCase().charAt(0) == 'W')
                player = Color.White;
            else
                throw new GtpException("invalid color");
            List<Move> options = genMoveFilters.apply(currentBoard, player);
            Move move = options.get(random.nextInt(options.size()));
            currentBoard.play(move);
            return move.getVertex().toString();
        }
    }

}
