package us.dahc.goliphant.toys.randbot;

import com.google.inject.AbstractModule;
import us.dahc.goliphant.core.Board;
import us.dahc.goliphant.core.DefaultBoard;
import us.dahc.goliphant.gtp.GtpHandler;
import us.dahc.goliphant.core.ApplicationIdentity;

import java.io.InputStream;
import java.io.PrintStream;

public class RandomBotModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(GtpHandler.class).to(RandomGtpHandler.class);
        bind(Board.class).to(DefaultBoard.class);
        bind(ApplicationIdentity.class).toInstance(new ApplicationIdentity("Goliphant Random Bot", "1.0"));
        bind(InputStream.class).toInstance(System.in);
        bind(PrintStream.class).toInstance(System.out);
    }

}
