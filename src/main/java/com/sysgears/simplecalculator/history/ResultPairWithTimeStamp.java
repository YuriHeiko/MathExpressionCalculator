package com.sysgears.simplecalculator.history;

import java.text.SimpleDateFormat;

/**
 * Keeps an associated pair and a time stamp when the object
 * was created.
 */
public class ResultPairWithTimeStamp extends ResultPair {
    /**
     * A time stamp of a history event creation
     */
    private long timeStamp;

    /**
     * A Date formatter
     */
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd MMM HH:mm:ss");

    /**
     * Constructs an object with the only key. It is needed to
     * provide search by the key in the collection of these
     *
     * @param key The key
     */
    public ResultPairWithTimeStamp(String key) {
        super(key);
    }

    /**
     * Constructs an object
     *
     * @param key   The key
     * @param value The value
     */
    public ResultPairWithTimeStamp(String key, String value) {
        this(key, value, System.currentTimeMillis());
    }

    /**
     * Constructs an object and store the time of its creation
     *
     * @param key       the key
     * @param value     The value
     * @param timeStamp The time
     */
    private ResultPairWithTimeStamp(String key, String value, long timeStamp) {
        super(key, value);
        this.timeStamp = timeStamp;
    }

    /**
     * Returns the time of event creation
     *
     * @return The time stamp
     */
    public long getTimeStamp() {
        return timeStamp;
    }

    /**
     * Returns the string representation of the object
     *
     * @return The string representation of the object
     */
    @Override
    public String toString() {
        return '[' + sdf.format(timeStamp) + "]\t" + getKey() + (getValue().isEmpty()?"" : " = ") + getValue();
    }
}