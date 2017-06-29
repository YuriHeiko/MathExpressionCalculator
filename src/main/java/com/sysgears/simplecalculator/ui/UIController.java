package com.sysgears.simplecalculator.ui;

import java.io.Closeable;
import java.io.IOException;

public interface UIController extends Closeable{

    String readLine(String promptString) throws IOException;

    void printLine(String description, String outputString);

    void printLine(String line);

    void close() throws IOException;
}
