package com.tempos21.rampload.exception;

/**
 * This is a generic {@link Exception} used to provide an error about an initialization of a service.
 */
public class RampLoadInitServiceException extends RampLoadInitException {
    public RampLoadInitServiceException(String detailMessage) {
        super(detailMessage);
    }
}
