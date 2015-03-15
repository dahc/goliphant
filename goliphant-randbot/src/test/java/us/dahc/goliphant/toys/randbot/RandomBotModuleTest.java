package us.dahc.goliphant.toys.randbot;

import com.google.inject.Guice;
import org.junit.Test;
import us.dahc.goliphant.gtp.GtpClient;

import static org.junit.Assert.assertNotNull;

public class RandomBotModuleTest {

    @Test
    public void testInjection() {
        assertNotNull(Guice.createInjector(new RandomBotModule()).getInstance(GtpClient.class));
    }

}
