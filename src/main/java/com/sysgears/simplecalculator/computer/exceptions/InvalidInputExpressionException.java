package com.sysgears.simplecalculator.computer.exceptions;

/**
 * Thrown to indicate that the application has attempted to calculate
 * an math expression, but that the expression string does not have the
 * appropriate format.
 */
public class InvalidInputExpressionException extends RuntimeException {
    private String wrongPart;

    public String getWrongPart() {
        return wrongPart;
    }

    /**
     * Constructs an object. Resend message to the super
     * class constructor
     *
     * @param message The string contains an error description
     */
    public InvalidInputExpressionException(final String message) {
        super(message);
    }

    /**
     * Constructs an object. Resend message to the super
     * class constructor
     *
     * @param message The string contains an error description
     */
    public InvalidInputExpressionException(final String message, final String wrongPart) {
        super(message);
        this.wrongPart = wrongPart;
    }
}
