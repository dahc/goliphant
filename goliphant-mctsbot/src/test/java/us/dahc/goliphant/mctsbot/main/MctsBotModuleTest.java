package us.dahc.goliphant.mctsbot.main;

import com.google.inject.Guice;
import org.junit.Test;
import us.dahc.goliphant.gtp.GtpClient;

import static org.junit.Assert.assertNotNull;

public class MctsBotModuleTest {

    @Test
    public void testInjection() {
        assertNotNull(Guice.createInjector(new MctsBotModule()).getInstance(GtpClient.class));
    }

}
