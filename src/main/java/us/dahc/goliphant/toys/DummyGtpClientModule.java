package us.dahc.goliphant.toys;

import dagger.Module;
import dagger.Provides;
import us.dahc.goliphant.gtp.BaseGtpHandler;
import us.dahc.goliphant.gtp.GtpClient;
import us.dahc.goliphant.gtp.GtpHandler;

import java.io.InputStream;
import java.io.PrintStream;

@Module(injects = GtpClient.class)
public class DummyGtpClientModule {

    @Provides
    InputStream provideInputStream() {
        return System.in;
    }

    @Provides
    PrintStream providePrintStream() {
        return System.out;
    }

    @Provides
    GtpHandler provideGtpHandler() {
        return new BaseGtpHandler("Dummy Client", "1.0");
    }
}
