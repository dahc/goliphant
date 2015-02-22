package us.dahc.goliphant.gtp;

import us.dahc.goliphant.util.GoliphantException;

public interface GtpHandler {
    public String handle(String command, String... args) throws GoliphantException;
}
