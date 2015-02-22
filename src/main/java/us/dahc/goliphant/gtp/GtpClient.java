package us.dahc.goliphant.gtp;

import org.apache.commons.lang3.StringUtils;

import javax.inject.Inject;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

public class GtpClient implements Runnable {

    private GtpHandler handler;
    private InputStream in;
    private PrintStream out;

    @Inject
    public GtpClient(GtpHandler handler, InputStream in, PrintStream out) {
        this.handler = handler;
        this.in = in;
        this.out = out;
    }

    public void run() {
        Scanner scanner = new Scanner(in);
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            line = StringUtils.removePattern(line, "#.*");
            line = StringUtils.normalizeSpace(line);
            if (StringUtils.isNotEmpty(line)) {
                try {
                    out.println("= " + handler.handle(getCommand(line), getArguments(line)) + "\n");
                } catch (QuitException e) {
                    out.println("= \n");
                    break;
                } catch (GtpException e) {
                    out.println("? " + e.getMessage() + "\n");
                }
            }
        }
        scanner.close();
    }

    private static String getCommand(String line) {
        return StringUtils.normalizeSpace(line).split(" ")[0];
    }

    private static String[] getArguments(String line) {
        String tokens[] = line.split(" ");
        String arguments[] = new String[tokens.length - 1];
        for (int i = 0; i < arguments.length; i++)
            arguments[i] = tokens[i + 1];
        return arguments;
    }

}
