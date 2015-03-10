package us.dahc.goliphant.core;

public class ApplicationIdentity {

    private String name;
    private String version;

    public ApplicationIdentity(String name, String version) {
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
