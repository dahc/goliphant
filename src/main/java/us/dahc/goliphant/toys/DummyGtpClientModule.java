package us.dahc.goliphant.toys;

import com.google.inject.AbstractModule;
import us.dahc.goliphant.core.Board;
import us.dahc.goliphant.core.DefaultBoard;
import us.dahc.goliphant.core.PositionalSuperkoWrapper;
import us.dahc.goliphant.core.SuperkoAware;
import us.dahc.goliphant.gtp.GtpHandler;
import us.dahc.goliphant.gtp.StatefulGtpHandler;
import us.dahc.goliphant.core.ApplicationIdentity;

import java.io.InputStream;
import java.io.PrintStream;

public class DummyGtpClientModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(GtpHandler.class).to(StatefulGtpHandler.class);
        bind(Board.class).annotatedWith(SuperkoAware.class).to(PositionalSuperkoWrapper.class);
        bind(Board.class).to(DefaultBoard.class);
        bind(ApplicationIdentity.class).toInstance(new ApplicationIdentity("Dummy Client", "1.0"));
        bind(InputStream.class).toInstance(System.in);
        bind(PrintStream.class).toInstance(System.out);
    }

}
