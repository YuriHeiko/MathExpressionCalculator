package com.sysgears.simplecalculator.ui;

import java.io.IOException;

public interface UIController {

    String read(String promptString) throws IOException;

    void write(String description, String outputString);
}
