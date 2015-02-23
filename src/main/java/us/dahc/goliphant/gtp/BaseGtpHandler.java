package us.dahc.goliphant.gtp;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseGtpHandler implements GtpHandler {

    protected Map<String, Command> commands;

    @Inject
    public BaseGtpHandler(GtpClientIdentity clientIdentity) {
        commands = new HashMap<String, Command>();
        commands.put("name", new StubCommand(clientIdentity.getName()));
        commands.put("version", new StubCommand(clientIdentity.getVersion()));
        commands.put("protocol_version", new StubCommand(PROTOCOL_VERSION));
        commands.put("list_commands", new ListCommands());
        commands.put("known_command", new KnownCommand());
        commands.put("quit", new QuitCommand());
    }

    public String handle(String command, String... args) throws GtpException {
        if (commands.containsKey(command))
            return commands.get(command).exec(args);
        else
            throw new GtpException("unknown command");
    }

    protected interface Command {
        public String exec(String... arguments) throws GtpException;
    }

    protected class StubCommand implements Command {
        private String response;

        StubCommand(String response) {
            this.response = response;
        }

        public String exec(String... args) {
            return response;
        }
    }

    class ListCommands implements Command {
        public String exec(String... args) {
            List<String> commandList = new ArrayList<String>(commands.keySet());
            Collections.sort(commandList);
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < commandList.size() - 1; i++)
                stringBuilder.append(commandList.get(i)).append('\n');
            stringBuilder.append(commandList.get(commandList.size() - 1));
            return stringBuilder.toString();
        }
    }

    class KnownCommand implements Command {
        public String exec(String... args) {
            if (args.length > 0 && commands.containsKey(args[0]))
                return "true";
            else
                return "false";
        }
    }

    class QuitCommand implements Command {
        public String exec(String... args) throws QuitException {
            throw new QuitException();
        }
    }

}
