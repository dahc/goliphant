package us.dahc.goliphant.toys;

import dagger.Module;
import dagger.Provides;
import us.dahc.goliphant.go.Board;
import us.dahc.goliphant.go.DefaultBoard;
import us.dahc.goliphant.gtp.*;
import us.dahc.goliphant.util.hashing.ZobristTable;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Random;

@Module(injects = GtpClient.class)
public class DummyGtpClientModule {

    @Provides GtpHandler provideGtpHandler(GtpClientIdentity clientIdentity, Board board) {
        return new StatefulGtpHandler(clientIdentity, board);
    }

    @Provides GtpClientIdentity provideGtpClientIdentity() {
        return new GtpClientIdentity("Dummy Client", "1.0");
    }

    @Provides Board provideBoard(ZobristTable zobristTable) {
        return new DefaultBoard(zobristTable);
    }

    @Provides ZobristTable provideZobristTable() {
        return new ZobristTable(new Random());
    }

    @Provides InputStream provideInputStream() {
        return System.in;
    }

    @Provides PrintStream providePrintStream() {
        return System.out;
    }

}
