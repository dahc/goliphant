package us.dahc.goliphant.gtp;

import us.dahc.goliphant.util.GoliphantException;

public interface GtpCommand {
    public String exec(String... args) throws GoliphantException;
}

