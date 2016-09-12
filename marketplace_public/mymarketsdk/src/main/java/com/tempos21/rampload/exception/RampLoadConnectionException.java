package com.tempos21.rampload.exception;

/**
 * This is a generic {@link Exception} used to provide an error about the connection.
 */
public class RampLoadConnectionException extends RampLoadException {
    public RampLoadConnectionException(String detailMessage) {
        super(detailMessage);
    }
}
