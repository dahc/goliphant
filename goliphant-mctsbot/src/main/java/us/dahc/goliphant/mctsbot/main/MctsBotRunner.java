package us.dahc.goliphant.mctsbot.main;

import com.google.inject.Guice;
import us.dahc.goliphant.gtp.GtpClient;

public class MctsBotRunner {

    public static void main(String[] args) {
        Guice.createInjector(new MctsBotModule()).getInstance(GtpClient.class).run();
    }

}
