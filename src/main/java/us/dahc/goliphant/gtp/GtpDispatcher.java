package us.dahc.goliphant.gtp;

import us.dahc.goliphant.util.GoliphantException;

public interface GtpDispatcher {
    public String dispatch(String command, String... args) throws GoliphantException;
}
