package com.sysgears.simplecalculator2.ui;

import java.io.IOException;

public interface UIController {

    String read(String promptString) throws IOException;

    void write(String description, String outputString);
}
