package us.dahc.goliphant.util;

import org.junit.Before;
import org.junit.Test;
import us.dahc.goliphant.core.ApplicationIdentity;

import static org.junit.Assert.assertEquals;

public class ApplicationIdentityTest {

    private ApplicationIdentity clientIdentity;

    @Before
    public void setup() {
        clientIdentity = new ApplicationIdentity("Test Client", "1.0");
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
