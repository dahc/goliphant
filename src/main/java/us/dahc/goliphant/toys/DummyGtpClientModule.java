package us.dahc.goliphant.toys;

import com.google.inject.AbstractModule;
import us.dahc.goliphant.go.Board;
import us.dahc.goliphant.go.DefaultBoard;
import us.dahc.goliphant.gtp.GtpClientIdentity;
import us.dahc.goliphant.gtp.GtpHandler;
import us.dahc.goliphant.gtp.StatefulGtpHandler;

import java.io.InputStream;
import java.io.PrintStream;

public class DummyGtpClientModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(GtpHandler.class).to(StatefulGtpHandler.class);
        bind(Board.class).to(DefaultBoard.class);
        bind(GtpClientIdentity.class).toInstance(new GtpClientIdentity("Dummy Client", "1.0"));
        bind(InputStream.class).toInstance(System.in);
        bind(PrintStream.class).toInstance(System.out);
    }

}
