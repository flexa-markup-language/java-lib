/**
 * MIT License
 * <p>
 * Copyright (c) 2025 Carlos Eduardo de Borba Machado
 */

package fml.core;

import java.util.ArrayList;
import java.util.List;

public final class Lexer {
    private static final List<Token> tokens = new ArrayList<>();

    // Control variables
    private final String input;
    private char prevChar;
    private char currChar;
    private char nextChar;
    private int prevIndex = -2;
    private int currIndex = -1;
    private int nextIndex = 0;
    private int currLine = 1;
    private int currColumn = 1;

    public Lexer(String input) {
        this.input = input;
    }

    public List<Token> tokenize() {
        nextChar();
        while (notEndOfInput()) {
            // Skip whitespace and newlines
            if (Symbols.isSkip(currChar)) {
                if (currChar == Symbols.NEWLINE) {
                    nextLine();
                }
                nextChar();
                continue;
            }

            // Skip comments
            if (currChar == Symbols.HASH) {
                while (notEndOfInput() && currChar != Symbols.NEWLINE) {
                    nextChar();
                }
                nextLine();
                nextChar();
            }
            // Key, boolean, or null
            else if (Character.isLetter(currChar) || currChar == Symbols.UNDERSCORE) {
                StringBuilder lexeme = new StringBuilder(String.valueOf(currChar));
                var initCol = currColumn;
                nextChar();

                // Loop through the current lexeme
                while (notEndOfInput()
                        && ((currChar != Symbols.COLON && currChar != Symbols.SEMICOLON) || prevChar == '\\')
                        && !((lexeme.toString().equals("true") || lexeme.toString().equals("false")
                            || lexeme.toString().equals("null") && currChar == Symbols.COMMA))) {
                    lexeme.append(currChar);
                    nextChar();
                }

                // Boolean
                if (lexeme.toString().equals("true") || lexeme.toString().equals("false")) {
                    tokens.add(new Token(TokenCategory.BOOL, lexeme.toString(), currLine, initCol));
                }
                // Null
                else if (lexeme.toString().equals("null")) {
                    tokens.add(new Token(TokenCategory.NULL, lexeme.toString(), currLine, initCol));
                }
                // Key
                else {
                    tokens.add(new Token(TokenCategory.KEY, lexeme.toString(), currLine, initCol));
                }
            }
            // Open array
            else if (currChar == Symbols.LEFT_BRACKETS) {
                tokens.add(new Token(TokenCategory.OPEN_ARRAY, String.valueOf(currChar), currLine, currColumn));
                nextChar();
            }
            // Close array
            else if (currChar == Symbols.RIGHT_BRACKETS) {
                tokens.add(new Token(TokenCategory.CLOSE_ARRAY, String.valueOf(currChar), currLine, currColumn));
                nextChar();
            }
            // End of data
            else if (currChar == Symbols.SEMICOLON) {
                tokens.add(new Token(TokenCategory.END_OF_DATA, String.valueOf(currChar), currLine, currColumn));
                nextChar();
            }
            // Array separator
            else if (currChar == Symbols.COMMA) {
                tokens.add(new Token(TokenCategory.ARRAY_SEP, String.valueOf(currChar), currLine, currColumn));
                nextChar();
            }
            // Data separator
            else if (currChar == Symbols.COLON) {
                tokens.add(new Token(TokenCategory.DATA_SEP, String.valueOf(currChar), currLine, currColumn));
                nextChar();
            }
            // String
            else if (currChar == Symbols.D_QUOTE) {
                StringBuilder lexeme = new StringBuilder(String.valueOf(currChar));
                var initCol = currColumn;
                nextChar();
                while (notEndOfInput() && (currChar != Symbols.D_QUOTE || prevChar == '\\')) {
                    if (currChar != '\\' || prevChar == '\\') {
                        lexeme.append(currChar);
                    }
                    nextChar();
                }
                if (currChar != Symbols.D_QUOTE) {
                    throw new RuntimeException("String was not closed at line " + currLine + " and column " + currColumn + ".");
                }
                lexeme.append(currChar);
                tokens.add(new Token(TokenCategory.STRING, lexeme.toString(), currLine, initCol));
                nextChar();
            }
            // Char
            else if (currChar == Symbols.QUOTE) {
                var lexeme = String.valueOf(currChar);
                var initCol = currColumn;
                nextChar();
                if (currChar == '\\') {
                    lexeme += currChar;
                    nextChar();
                }
                lexeme += currChar;
                nextChar();
                if (currChar != Symbols.QUOTE) {
                    throw new RuntimeException("Char was not closed at line " + currLine + " and column " + currColumn + ".");
                }
                lexeme += currChar;
                tokens.add(new Token(TokenCategory.CHAR, lexeme, currLine, initCol));
                nextChar();
            }
            // Numeric
            else if (Character.isDigit(currChar) || currChar == Symbols.DOT || currChar == Symbols.MINUS) {
                StringBuilder lexeme = new StringBuilder(String.valueOf(currChar));
                var initCol = currColumn;
                var dotted = currChar == Symbols.DOT;
                nextChar();
                while (notEndOfInput() && (Character.isDigit(currChar) || currChar == Symbols.DOT)) {
                    if (currChar == Symbols.DOT) {
                        if (dotted) {
                            throw new RuntimeException("Invalid character '" + currChar + "' encountered at line " + currLine + " and column " + currColumn + ".");
                        } else {
                            dotted = true;
                        }
                    }
                    lexeme.append(currChar);
                    nextChar();
                }
                if (Character.toLowerCase(currChar) == 'f') {
                    lexeme.append(currChar);
                    nextChar();
                }
                // Float or integer
                if (lexeme.toString().contains(String.valueOf(Symbols.DOT)) || lexeme.toString().toLowerCase().contains("f")) {
                    tokens.add(new Token(TokenCategory.FLOAT, lexeme.toString(), currLine, initCol));
                } else {
                    tokens.add(new Token(TokenCategory.INTEGER, lexeme.toString(), currLine, initCol));
                }
            } else {
                throw new RuntimeException("Invalid character '" + currChar + "' encountered at line " + currLine + " and column " + currColumn + ".");
            }
        }

        tokens.add(new Token(TokenCategory.EOF, null, -1, -1));

        return tokens;
    }

    private boolean notEndOfInput() {
        return currIndex < input.length();
    }

    private void nextLine() {
        ++currLine;
        currColumn = 0;
    }

    private void nextChar() {
        ++prevIndex;
        ++currIndex;
        ++nextIndex;
        if (currIndex < input.length()) {
            prevChar = prevIndex >= 0 ? input.charAt(prevIndex) : '\0';
            currChar = input.charAt(currIndex);
            nextChar = nextIndex < input.length() ? input.charAt(nextIndex) : '\0';
            currColumn++;
        }
    }

}
