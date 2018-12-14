package org.mz.ditran.common.exception;

/**
 * @Author: mario
 * @Email: mmmario@foxmail.com
 * @Date: 2018-12-14 1:26 PM
 * @Description:
 */
public class DitransactionException extends Exception {
    public DitransactionException() {
    }

    public DitransactionException(String message) {
        super(message);
    }

    public DitransactionException(String message, Throwable cause) {
        super(message, cause);
    }

    public DitransactionException(Throwable cause) {
        super(cause);
    }

    public DitransactionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
