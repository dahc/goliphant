package us.dahc.goliphant.gtp;

import us.dahc.goliphant.util.GoliphantException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseGtpHandler implements GtpHandler {

    protected Map<String, GtpCommand> commands;

    public BaseGtpHandler(String name, String version) {
        commands = new HashMap<String, GtpCommand>();
        commands.put("name", new CannedCommand(name));
        commands.put("version", new CannedCommand(version));
        commands.put("list_commands", new ListCommands());
        commands.put("known_command", new KnownCommand());
    }

    public String handle(String command, String... args) throws GoliphantException {
        if (commands.containsKey(command))
            return commands.get(command).exec(args);
        else
            throw new GoliphantException("unknown command");
    }

    class CannedCommand implements GtpCommand {
        private String response;

        CannedCommand(String response) {
            this.response = response;
        }

        public String exec(String... args) {
            return response;
        }
    }

    class ListCommands implements GtpCommand {
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

    class KnownCommand implements GtpCommand {
        public String exec(String... args) {
            if (args.length > 0 && commands.containsKey(args[0]))
                return "true";
            else
                return "false";
        }
    }

}
