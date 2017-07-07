package com.sysgears.simplecalculator.history;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Keeps the history which elements stores in {@link ResultPair} class.
 */
public class HistoryHolder {
    /**
     * A {@code List} collection holds {@code ResultPair} objects which
     * keeps history events
     */
    private final List<ResultPair> history;

    /**
     * Constructs an object
     */
    public HistoryHolder() {
        this.history = new LinkedList<>();
    }

    /**
     * Add a new event into history container
     *
     * @param key   The key
     * @param value The value
     */
    public void addEvent(String key, String value) {
        history.add(new ResultPair(key, value));
    }

    /**
     * Returns the value associated with the key
     *
     * @param key The key
     * @return The value associated with the key
     */
    public String getResult(String key) {
        int index = history.indexOf(new ResultPair(key));

        if (index == -1) {
            return "";
        } else {
            return history.get(index).getValue();
        }
    }

    /**
     * Returns string contains the history events without duplicates
     *
     * @return History without duplicates
     */
    public String getUniqueHistory() {
        return buildString(new LinkedHashSet<>(history), false);
    }

    /**
     * Returns the string contains the history events
     *
     * @return The string contains the history events
     */
    @Override
    public String toString() {
        return buildString(history, true);
    }

    /**
     * Builds a string with history events from the received {@code Collection}
     *
     * @param pairs The collection contains history events
     * @return The string contains the history events
     */
    private String buildString(final Collection<ResultPair> pairs, final boolean withTime) {
        return "\t" + pairs.stream().map(e->e.getDescription(withTime)).collect(Collectors.joining(System.lineSeparator() + "\t"));
    }
}