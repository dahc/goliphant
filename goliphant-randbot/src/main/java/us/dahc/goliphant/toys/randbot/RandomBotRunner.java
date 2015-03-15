package us.dahc.goliphant.toys.randbot;

import com.google.inject.Guice;
import us.dahc.goliphant.gtp.GtpClient;

public class RandomBotRunner {

    public static void main(String[] args) {
        Guice.createInjector(new RandomBotModule()).getInstance(GtpClient.class).run();
    }

}
