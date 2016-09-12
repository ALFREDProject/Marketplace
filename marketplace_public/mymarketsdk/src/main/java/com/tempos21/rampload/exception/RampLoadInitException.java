package com.tempos21.rampload.exception;

/**
 * This is a generic {@link Exception} used to provide an error about an initialization.
 */
public class RampLoadInitException extends RampLoadException {
    public RampLoadInitException(String detailMessage) {
        super(detailMessage);
    }
}
