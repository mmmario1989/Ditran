package org.mz.ditran.common.exception;

/**
 * @Author: jsonz
 * @Date: 2018-12-13 18:56
 */
public class DitranZKException extends Exception {
    public DitranZKException(Exception cause) {
        super(cause);
    }

    public DitranZKException(String message, Exception cause) {
        super(message, cause);
    }
}
