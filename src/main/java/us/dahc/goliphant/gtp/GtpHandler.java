package us.dahc.goliphant.gtp;

public interface GtpHandler {

    public String handle(String command, String... args) throws GtpException;

}
