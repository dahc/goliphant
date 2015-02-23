package us.dahc.goliphant.gtp;

public class GtpClientIdentity {

    private String name;
    private String version;

    public GtpClientIdentity(String name, String version) {
        this.name = name;
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

}
