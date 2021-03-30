package com.solactive.challenge.solactivetickvalues.exceptionhandler;

public class ExceptionResponse {
    private String error;
    private String url;

    public String getError() {
        return error;
    }

    public void setError(final String error) {
        this.error = error;
    }

    public String getUrl() {
        return url;
    }

    public void callerURL(final String requestedURI) {
        this.url = requestedURI;
    }
}
