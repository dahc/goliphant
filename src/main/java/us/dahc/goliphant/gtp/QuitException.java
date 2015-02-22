package us.dahc.goliphant.gtp;

import org.apache.commons.lang3.StringUtils;

public class QuitException extends GtpException {

    public QuitException() {
        super(StringUtils.EMPTY);
    }

}
