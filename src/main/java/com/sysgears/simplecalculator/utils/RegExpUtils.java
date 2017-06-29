package com.sysgears.simplecalculator.utils;

public class RegExpUtils {

    /**
     * Changes the incoming string into the valid RegExp
     *
     * @param s the incoming string
     * @return string contains the valid RegExp
     */
    public static String changeToRegExp(final String s) {
        return s.replaceAll("(?=[]\\[+&|!(){}^\"~*?:\\\\\\/-])", "\\\\");
    }

}
