package com.basicit.html2pdf.json;

/**
 * The JSONException is thrown by the JSON.org classes when things are amiss.
 * @author JSON.org
 * @version 2
 */
/*
 * Changes Copyright (c) 2006,2007 John Snyders under the same license terms.
 *
 * Extend from RuntimeException rather than Exception so that exceptions don't
 * need to be checked.
 */
public class JSONException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private Throwable cause;

    /**
     * Constructs a JSONException with an explanatory message.
     * @param message Detail about the reason for the exception.
     */
    public JSONException(String message) {
        super(message);
    }

    public JSONException(Throwable t) {
        super(t.getMessage());
        this.cause = t;
    }

    public Throwable getCause() {
        return this.cause;
    }
}
