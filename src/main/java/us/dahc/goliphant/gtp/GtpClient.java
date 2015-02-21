package us.dahc.goliphant.gtp;

import org.apache.commons.lang3.StringUtils;

import javax.inject.Inject;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

public class GtpClient implements Runnable {

    private Dispatcher dispatcher;

    private InputStream in;

    private PrintStream out;

    @Inject
    public GtpClient(Dispatcher dispatcher, InputStream in, PrintStream out) {
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
            } catch (Exception e) {
                out.println("? " + e.getMessage());
            }
        }
        scanner.close();
    }

    private static String getCommand(String line) {
        return "";
    }

    private static String[] getArguments(String line) {
        String normalized = StringUtils.normalizeSpace(line);
        return new String[] {""};
    }

}
