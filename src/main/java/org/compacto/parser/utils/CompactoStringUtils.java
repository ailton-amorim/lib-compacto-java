package org.compacto.parser.utils;

public class CompactoStringUtils {
    private CompactoStringUtils() {
    }

    public static boolean isNullOrEmpty(String str) {
        return str == null || str.isEmpty();
    }
}
