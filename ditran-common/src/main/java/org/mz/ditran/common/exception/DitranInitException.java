package org.mz.ditran.common.exception;

/**
 * @Author: jsonz
 * @Date: 2018-12-13 18:56
 */
public class DitranInitException extends RuntimeException {
    public DitranInitException(Exception cause) {
        super(cause);
    }

    public DitranInitException(String message) {
        super(message);
    }

    public DitranInitException(String message, Exception cause) {
        super(message, cause);
    }
}
