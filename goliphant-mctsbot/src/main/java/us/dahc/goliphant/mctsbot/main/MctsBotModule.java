package us.dahc.goliphant.mctsbot.main;

import com.google.inject.AbstractModule;
import us.dahc.goliphant.core.Board;
import us.dahc.goliphant.core.DefaultBoard;
import us.dahc.goliphant.gtp.GtpHandler;
import us.dahc.goliphant.core.ApplicationIdentity;
import us.dahc.goliphant.mcts.Policy;
import us.dahc.goliphant.mcts.UcbPolicy;
import us.dahc.goliphant.mctsbot.engine.Engine;
import us.dahc.goliphant.mctsbot.engine.MctsEngine;

import java.io.InputStream;
import java.io.PrintStream;

public class MctsBotModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(Engine.class).to(MctsEngine.class);
        bind(Policy.class).toInstance(new UcbPolicy(Double.parseDouble(System.getProperty("exp", "2.0"))));
        bind(GtpHandler.class).to(MctsGtpHandler.class);
        bind(Board.class).to(DefaultBoard.class);
        bind(ApplicationIdentity.class).toInstance(new ApplicationIdentity("Goliphant", "0.1"));
        bind(InputStream.class).toInstance(System.in);
        bind(PrintStream.class).toInstance(System.out);
    }

}
