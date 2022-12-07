package org.advent.utils;

public class StringUtils {
    // todo: make this more efficient w/ char or bool array maybe?
    public static boolean isUniqueSubstring(final String s, final int lo, final int hi) {
        for (int i = lo; i <= hi; i++) {
            for (int j = lo; j <= hi; j++) {
                if (i != j && s.charAt(i) == s.charAt(j)) {
                    return false;
                }
            }
        }

        return true;
    }
}
