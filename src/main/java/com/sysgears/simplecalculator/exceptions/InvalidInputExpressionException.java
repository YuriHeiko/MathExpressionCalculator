package com.sysgears.simplecalculator.exceptions;

/**
 * Thrown to indicate that the application has attempted to calculate
 * an math expression, but that the expression string does not have the
 * appropriate format.
 */
public class InvalidInputExpressionException extends RuntimeException {

    /**
     * Constructs an object. Resend message to the super
     * class constructor
     *
     * @param message String contains an error description
     */
    public InvalidInputExpressionException(String message) {
        super(message);
    }
}
