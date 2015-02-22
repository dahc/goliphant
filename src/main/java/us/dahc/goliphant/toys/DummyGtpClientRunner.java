package us.dahc.goliphant.toys;

import dagger.ObjectGraph;
import us.dahc.goliphant.gtp.GtpClient;

public class DummyGtpClientRunner {

    public static void main(String[] args) {
        ObjectGraph objectGraph = ObjectGraph.create(new DummyGtpClientModule());
        GtpClient gtpClient = objectGraph.get(GtpClient.class);
        gtpClient.run();
    }

}
