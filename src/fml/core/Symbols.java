/**
 * MIT License
 * <p>
 * Copyright (c) 2025 Carlos Eduardo de Borba Machado
 */

package fml.core;

import java.util.Set;

public final class Symbols {

    public static final char HASH = '#';
    public static final char LEFT_BRACKETS = '{';
    public static final char RIGHT_BRACKETS = '}';
    public static final char DOT = '.';
    public static final char COMMA = ',';
    public static final char COLON = ':';
    public static final char SEMICOLON = ';';
    public static final char MINUS = '-';
    public static final char UNDERSCORE = '_';
    public static final char D_QUOTE = '"';
    public static final char SPACE = ' ';
    public static final char QUOTE = '\'';
    public static final char NEWLINE = '\n';
    public static final char RETURN = '\r';
    public static final char TAB = '\t';

    private static final Set<Character> SKIP = Set.of(SPACE, TAB, NEWLINE, RETURN);

    public static boolean isSkip(char c) {
        return SKIP.contains(c);
    }

    // avoid instantiation
    private Symbols() {
        throw new UnsupportedOperationException("Utility class");
    }
}