package us.dahc.goliphant.sgf;

import us.dahc.goliphant.util.GoliphantException;

public class SgfException extends GoliphantException {

    public SgfException(String message) {
        super(message);
    }

    public SgfException(String message, Exception e) {
        super(message, e);
    }

}
