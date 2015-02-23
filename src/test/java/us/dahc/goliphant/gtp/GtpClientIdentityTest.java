package us.dahc.goliphant.gtp;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class GtpClientIdentityTest {

    private GtpClientIdentity clientIdentity;

    @Before
    public void setup() {
        clientIdentity = new GtpClientIdentity("Test Client", "1.0");
    }

    @Test
    public void testName() {
        assertEquals("Test Client", clientIdentity.getName());
    }

    @Test
    public void testVersion() {
        assertEquals("1.0", clientIdentity.getVersion());
    }

}
