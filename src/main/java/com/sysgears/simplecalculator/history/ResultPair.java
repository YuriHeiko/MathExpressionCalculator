package com.sysgears.simplecalculator.history;

import java.text.SimpleDateFormat;

/**
 * Keeps a associated pair and a time stamp when the object was created.
 */
public class ResultPair {
    private String key;
    private String value;
    private long timeStamp;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd MMM HH:mm:ss");

    /**
     * Constructs an object with the only key
     *
     * @param key the key
     */
    public ResultPair(String key) {
        this.key = key;
    }

    /**
     * Constructs an object and store the time of its creation
     *
     * @param key   the key
     * @param value the value
     */
    public ResultPair(String key, String value) {
        this(key, value, System.currentTimeMillis());
    }

    /**
     * Constructs an object
     *
     * @param key       the key
     * @param value     the value
     * @param timeStamp the time
     */
    public ResultPair(String key, String value, long timeStamp) {
        this.key = key;
        this.value = value;
        this.timeStamp = timeStamp;
    }

    /**
     * Returns the key
     *
     * @return the key
     */
    public String getKey() {
        return key;
    }

    /**
     * Returns the value
     *
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * Checks whether the received object is equal to this or not.
     * Takes into account only the value of the key
     *
     * @param o the object to compare
     * @return true if object is equal to this
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ResultPair that = (ResultPair) o;

        return key.equals(that.key);
    }

    /**
     * Returns the hashcode of the key
     *
     * @return the hashcode
     */
    @Override
    public int hashCode() {
        return key.hashCode();
    }

    /**
     * Returns the string depiction of the object
     *
     * @return the string depiction of the object
     */
    @Override
    public String toString() {
        return "Expression = '" + key + '\'' +
                ", Result = " + value +
                ", Time = " + sdf.format(timeStamp);
    }
}