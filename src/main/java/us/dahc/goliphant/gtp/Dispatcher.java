package us.dahc.goliphant.gtp;

public interface Dispatcher {
    public String dispatch(String command, String... args) throws Exception;
}
