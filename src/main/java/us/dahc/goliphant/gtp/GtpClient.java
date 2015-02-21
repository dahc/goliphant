package us.dahc.goliphant.gtp;

import org.apache.commons.lang3.StringUtils;
import us.dahc.goliphant.util.GoliphantException;

import javax.inject.Inject;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

public class GtpClient implements Runnable {

    private GtpDispatcher dispatcher;

    private InputStream in;

    private PrintStream out;

    @Inject
    public GtpClient(GtpDispatcher dispatcher, InputStream in, PrintStream out) {
        this.dispatcher = dispatcher;
        this.in = in;
        this.out = out;
    }

    public void run() {
        Scanner scanner = new Scanner(in);
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            try {
                out.println("= " + dispatcher.dispatch(getCommand(line), getArguments(line)));
            } catch (IgnorableLineException e) {
                // do nothing
            } catch (GoliphantException e) {
                out.println("? " + e.getMessage());
            }
        }
        scanner.close();
    }

    private static String getCommand(String line) throws IgnorableLineException {
        line = StringUtils.removePattern(line, "#.*");
        try {
            return StringUtils.normalizeSpace(line).split(" ")[0];
        } catch (Exception e) {
            throw new IgnorableLineException();
        }
    }

    private static String[] getArguments(String line) {
        line = StringUtils.removePattern(line, "#.*");
        String tokens[] = StringUtils.normalizeSpace(line).split(" ");
        String arguments[] = new String[tokens.length - 1];
        for (int i = 0; i < arguments.length; i++)
            arguments[i] = tokens[i + 1];
        return arguments;
    }

    private static class IgnorableLineException extends GoliphantException {}

}
