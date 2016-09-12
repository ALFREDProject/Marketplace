package com.tempos21.rampload.exception;

/**
 * This is a generic {@link Exception} used to provide an error about an initialization of values.
 */
public class RampLoadInitValuesException extends RampLoadInitException {
    public RampLoadInitValuesException(String detailMessage) {
        super(detailMessage);
    }
}
