package us.dahc.goliphant.toys;

import com.google.inject.Guice;
import us.dahc.goliphant.gtp.GtpClient;

public class DummyGtpClientRunner {

    public static void main(String[] args) {
        Guice.createInjector(new DummyGtpClientModule()).getInstance(GtpClient.class).run();
    }

}
