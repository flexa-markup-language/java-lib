/**
 * MIT License
 * <p>
 * Copyright (c) 2025 Carlos Eduardo de Borba Machado
 */

package fml.core;

import java.util.ArrayList;
import java.util.List;

public final class Lexer {

    private static List<Token> tokens;

    // Control variables
    private static String input;
    private static char curChar;
    private static int curIndex;
    private static int curLine;
    private static int curColumn;

    private static void init() {
        tokens = new ArrayList<>();
        curIndex = 0;
        curLine = 1;
        curColumn = 1;
    }

    public static List<Token> tokenize(String input) {
        init();
        Lexer.input = input;

        nextChar();
        while (notEndOfInput()) {
            // Skip whitespace and newlines
            if (Symbols.isSkip(curChar)) {
                if (curChar == Symbols.NEWLINE) {
                    nextLine();
                }
                nextChar();
                continue;
            }

            // Skip comments
            if (curChar == Symbols.HASH) {
                while (notEndOfInput() && curChar != Symbols.NEWLINE) {
                    nextChar();
                }
                nextLine();
                nextChar();
            }
            // Key, boolean, or null
            else if (Character.isLetter(curChar) || curChar == Symbols.UNDERSCORE) {
                StringBuilder lexeme = new StringBuilder(String.valueOf(curChar));
                var initCol = curColumn;
                nextChar();

                // Loop through the key
                while (notEndOfInput() && curChar != Symbols.COLON && (curChar == Symbols.UNDERSCORE || Character.isLetterOrDigit(curChar))) {
                    lexeme.append(curChar);
                    nextChar();
                }

                // Boolean
                if (lexeme.toString().equals("true") || lexeme.toString().equals("false")) {
                    tokens.add(new Token(TokenCategory.BOOL, lexeme.toString(), curLine, initCol));
                }
                // Null
                else if (lexeme.toString().equals("null")) {
                    tokens.add(new Token(TokenCategory.NULL, lexeme.toString(), curLine, initCol));
                }
                // Key
                else {
                    tokens.add(new Token(TokenCategory.KEY, lexeme.toString(), curLine, initCol));
                }
            }
            // Open array
            else if (curChar == Symbols.LEFT_BRACKETS) {
                tokens.add(new Token(TokenCategory.OPEN_ARRAY, String.valueOf(curChar), curLine, curColumn));
                nextChar();
            }
            // Close array
            else if (curChar == Symbols.RIGHT_BRACKETS) {
                tokens.add(new Token(TokenCategory.CLOSE_ARRAY, String.valueOf(curChar), curLine, curColumn));
                nextChar();
            }
            // End of data
            else if (curChar == Symbols.SEMICOLON) {
                tokens.add(new Token(TokenCategory.END_OF_DATA, String.valueOf(curChar), curLine, curColumn));
                nextChar();
            }
            // Array separator
            else if (curChar == Symbols.COMMA) {
                tokens.add(new Token(TokenCategory.ARRAY_SEP, String.valueOf(curChar), curLine, curColumn));
                nextChar();
            }
            // Data separator
            else if (curChar == Symbols.COLON) {
                tokens.add(new Token(TokenCategory.DATA_SEP, String.valueOf(curChar), curLine, curColumn));
                nextChar();
            }
            // String
            else if (curChar == Symbols.D_QUOTE) {
                StringBuilder lexeme = new StringBuilder(String.valueOf(curChar));
                var initCol = curColumn;
                var beforeChar = curChar;
                nextChar();
                while (notEndOfInput() && (curChar != Symbols.D_QUOTE || beforeChar == '\\')) {
                    if (curChar != '\\' || beforeChar == '\\') {
                        lexeme.append(curChar);
                    }
                    beforeChar = curChar;
                    nextChar();
                }
                if (curChar != Symbols.D_QUOTE) {
                    throw new RuntimeException("String was not closed at line " + curLine + " and column " + curColumn + ".");
                }
                lexeme.append(curChar);
                tokens.add(new Token(TokenCategory.STRING, lexeme.toString(), curLine, initCol));
                nextChar();
            }
            // Char
            else if (curChar == Symbols.QUOTE) {
                var lexeme = String.valueOf(curChar);
                var initCol = curColumn;
                nextChar();
                if (curChar == '\\') {
                    lexeme += curChar;
                    nextChar();
                }
                lexeme += curChar;
                nextChar();
                if (curChar != Symbols.QUOTE) {
                    throw new RuntimeException("Char was not closed at line " + curLine + " and column " + curColumn + ".");
                }
                lexeme += curChar;
                tokens.add(new Token(TokenCategory.CHAR, lexeme, curLine, initCol));
                nextChar();
            }
            // Numeric
            else if (Character.isDigit(curChar) || curChar == Symbols.DOT || curChar == Symbols.MINUS) {
                StringBuilder lexeme = new StringBuilder(String.valueOf(curChar));
                var initCol = curColumn;
                var dotted = curChar == Symbols.DOT;
                nextChar();
                while (notEndOfInput() && (Character.isDigit(curChar) || curChar == Symbols.DOT)) {
                    if (curChar == Symbols.DOT) {
                        if (dotted) {
                            throw new RuntimeException("Invalid character '" + curChar + "' encountered at line " + curLine + " and column " + curColumn + ".");
                        } else {
                            dotted = true;
                        }
                    }
                    lexeme.append(curChar);
                    nextChar();
                }
                if (Character.toLowerCase(curChar) == 'f') {
                    lexeme.append(curChar);
                    nextChar();
                }
                // Float or integer
                if (lexeme.toString().contains(String.valueOf(Symbols.DOT)) || lexeme.toString().toLowerCase().contains("f")) {
                    tokens.add(new Token(TokenCategory.FLOAT, lexeme.toString(), curLine, initCol));
                } else {
                    tokens.add(new Token(TokenCategory.INTEGER, lexeme.toString(), curLine, initCol));
                }
            } else {
                throw new RuntimeException("Invalid character '" + curChar + "' encountered at line " + curLine + " and column " + curColumn + ".");
            }
        }

        tokens.add(new Token(TokenCategory.EOF, null, -1, -1));

        return tokens;
    }

    private static boolean notEndOfInput() {
        return curIndex <= input.length();
    }

    private static void nextLine() {
        curLine++;
        curColumn = 0;
    }

    private static void nextChar() {
        if (curIndex < input.length()) {
            curChar = input.charAt(curIndex);
            curColumn++;
        }
        curIndex++;
    }

    // avoid instantiation
    private Lexer() {
        throw new UnsupportedOperationException("Utility class");
    }
}
