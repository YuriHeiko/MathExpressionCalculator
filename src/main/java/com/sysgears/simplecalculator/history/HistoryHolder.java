package com.sysgears.simplecalculator.history;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class HistoryHolder {
    private List<ResultPair> history;

    public HistoryHolder() {
        this.history = new LinkedList<>();
    }

    public void addEvent(String expression, String result) {
        history.add(new ResultPair(expression, result));
    }

    public String getResult(String expression) {
        int index = history.indexOf(new ResultPair(expression));

        if (index == -1) {
            return null;
        } else {
            return history.get(index).getResult();
        }
    }

    public String getUniqueHistory() {
        return buildString(new LinkedHashSet<>(history));
    }

    @Override
    public String toString() {
        return buildString(history);
    }

    private String buildString(Collection<ResultPair> pairs) {
        return "\t" + pairs.stream().
                                    map(ResultPair::toString).
                                    collect(Collectors.joining(System.lineSeparator() + "\t"));
    }
}
