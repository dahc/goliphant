package us.dahc.goliphant.core;

@SuppressWarnings("serial")
public class GoliphantException extends Exception {

    public GoliphantException() {}

    public GoliphantException(Throwable cause) {
        super(cause);
    }

    public GoliphantException(String message) {
        super(message);
    }

    public GoliphantException(String message, Throwable cause) {
        super(message, cause);
    }
}
