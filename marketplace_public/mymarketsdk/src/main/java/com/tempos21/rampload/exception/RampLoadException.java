package com.tempos21.rampload.exception;

/**
 * This is a generic {@link Exception} used to provide information about an error of the RampLoad library.
 */
public class RampLoadException extends Exception {
    public RampLoadException(String detailMessage) {
        super(detailMessage);
    }
}
