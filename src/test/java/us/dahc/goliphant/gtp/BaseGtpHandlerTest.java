package us.dahc.goliphant.gtp;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.number.OrderingComparison.greaterThan;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class BaseGtpHandlerTest {

    private GtpHandler gtpHandler;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setup() {
        gtpHandler = new BaseGtpHandler(new GtpClientIdentity("Test Client", "1.0"));
    }

    @Test
    public void testTrivialCommands() throws GtpException {
        assertEquals("Test Client", gtpHandler.handle("name"));
        assertEquals("1.0", gtpHandler.handle("version"));
        assertEquals(GtpHandler.PROTOCOL_VERSION, gtpHandler.handle("protocol_version"));
    }

    @Test
    public void testUnknownCommand() throws GtpException {
        thrown.expect(GtpException.class);
        thrown.expectMessage("unknown command");
        gtpHandler.handle("nonexistant_command", "with", "some", "args");
    }

    @Test
    public void testKnownAndListCommands() throws GtpException {
        String[] commandList = gtpHandler.handle("list_commands").split("\n");
        assertThat("many commands", commandList.length, is(greaterThan(5)));
        for (String command : commandList)
            assertThat(command + " is known", gtpHandler.handle("known_command", command), is("true"));
        assertThat("fake command", gtpHandler.handle("known_command", "nonexistant_command"), is("false"));
    }

    @Test
    public void testQuitCommand() throws GtpException {
        thrown.expect(QuitException.class);
        gtpHandler.handle("quit");
    }

}
