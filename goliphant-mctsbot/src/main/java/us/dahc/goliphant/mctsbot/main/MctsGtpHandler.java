package us.dahc.goliphant.mctsbot.main;

import us.dahc.goliphant.core.ApplicationIdentity;
import us.dahc.goliphant.core.Board;
import us.dahc.goliphant.core.Color;
import us.dahc.goliphant.core.Move;
import us.dahc.goliphant.gtp.BaseGtpHandler;
import us.dahc.goliphant.gtp.GtpException;
import us.dahc.goliphant.gtp.StatefulGtpHandler;
import us.dahc.goliphant.mctsbot.engine.Engine;

import javax.inject.Inject;

public class MctsGtpHandler extends StatefulGtpHandler {

    protected Engine engine;

    @Inject
    public MctsGtpHandler(ApplicationIdentity clientIdentity, Engine engine, Board board) {
        super(clientIdentity, board);
        this.engine = engine;
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
            Move move = engine.getMove(currentBoard, player);
            currentBoard.play(move);
            return move.getVertex().toString();
        }
    }

}
