package us.dahc.goliphant.toys;

import com.google.inject.Guice;
import org.junit.Test;
import us.dahc.goliphant.gtp.GtpClient;

import static org.junit.Assert.assertNotNull;

public class DummyGtpClientModuleTest {

    @Test
    public void testInjection() {
        assertNotNull(Guice.createInjector(new DummyGtpClientModule()).getInstance(GtpClient.class));
    }

}
