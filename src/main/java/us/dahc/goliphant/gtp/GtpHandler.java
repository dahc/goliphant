package us.dahc.goliphant.gtp;

public interface GtpHandler {

    public static final String PROTOCOL_VERSION = "2";

    public String handle(String command, String... args) throws GtpException;

}
