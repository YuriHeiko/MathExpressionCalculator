package com.sysgears.simplecalculator.history;

/**
 * Keeps an associated pair and a time stamp when the object
 * was created.
 */
public class ResultPair {
    /**
     * A key
     */
    private String key;

    /**
     * A value
     */
    private String value;

    /**
     * Constructs an object with the only key. It is needed to
     * provide search by the key in the collection of these
     *
     * @param key The key
     */
    public ResultPair(String key) {
        this.key = key;
    }

    /**
     * Constructs an object
     *
     * @param key   The key
     * @param value The value
     */
    public ResultPair(String key, String value) {
        this.key = key;
        this.value = value;
    }

    /**
     * Returns the key
     *
     * @return The key
     */
    public String getKey() {
        return key;
    }

    /**
     * Returns the value
     *
     * @return The value
     */
    public String getValue() {
        return value;
    }

    /**
     * Checks whether the received object is equal to this or not.
     * Takes into account only the value of the key
     *
     * @param o The object to compare
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
     * Returns the string representation of the object
     *
     * @return The string representation of the object
     */
    public String toString() {
        return "\t" + key + (value.isEmpty()?"" : " = ") + value;
    }
}