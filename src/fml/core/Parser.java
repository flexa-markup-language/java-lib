/**
 * MIT License
 * <p>
 * Copyright (c) 2025 Carlos Eduardo de Borba Machado
 */

package fml.core;

import java.util.*;

public final class Parser {

    private String data;

    private final Map<String, Object> parsedData = new HashMap<>();

    // Control variables
    private List<Token> tokens;
    private Token curToken;

    private int curIndex = -1;

    private String key;
    private Object value;
    private final Stack<List<Object>> arrStack = new Stack<>();

    private final int CONTEXT_KEY = 0;
    private final int CONTEXT_ARRAY = 1;
    private int context = CONTEXT_KEY;

    private boolean isSubValue = false;

    public Parser(String data) {
        this.data = data;
    }

    public Parser(List<Token> tokens, int curIndex) {
        this.tokens = tokens;
        this.curIndex = curIndex;
        isSubValue = true;
    }

    public Map<String, Object> parse() {
        if (!isSubValue) {
            tokens = Lexer.tokenize(data);
        }
        start();
        return parsedData;
    }

    private void start() {
        nextToken();
        statement();
        if (!isSubValue) {
            consumeToken(TokenCategory.EOF);
        }
    }

    private void statement() {
        if (curToken.category() == TokenCategory.KEY) {
            key();
        }
    }

    private void key() {
        key = curToken.image();
        nextToken();
        consumeToken(TokenCategory.DATA_SEP);
        value();
        consumeToken(TokenCategory.END_OF_DATA);
        statement();
    }

    private void value() {
        switch (curToken.category()) {
            case OPEN_ARRAY -> array();
            case STRING -> stringValue();
            case CHAR -> charValue();
            case INTEGER -> integerValue();
            case FLOAT -> floatValue();
            case BOOL -> boolValue();
            case NULL -> nullValue();
            case KEY -> subValue();
            default -> throw new RuntimeException("Invalid token '" + curToken.image() + "' encountered at line "
                    + curToken.line() + " and column " + curToken.column() + ". Expected a value or array.");
        }

        if (context == CONTEXT_ARRAY) {
            arraySelector();
        }
    }

    private void subValue() {
        var subValueParser = new Parser(tokens, curIndex - 1);
        value = subValueParser.parse();
        curIndex = subValueParser.getCurIndex() - 1;
        setValue();
    }

    private void arraySelector() {
        switch (curToken.category()) {
            case ARRAY_SEP -> {
                nextToken();
                value();
            }
            case CLOSE_ARRAY -> {
                closeArray();
                nextToken();
                arraySelector();
            }
            case END_OF_DATA, EOF -> {
            }
            default -> throw new RuntimeException("Invalid token '" + curToken.image() + "' encountered at line "
                    + curToken.line() + " and column " + curToken.column() + ". Expected ',', ']' or ';'.");
        }
    }

    private void array() {
        openArray();
        nextToken();
        value();
    }

    private void stringValue() {
        value = curToken.image().substring(1, curToken.image().length() - 1);
        setValue();
    }

    private void charValue() {
        String stringValue = curToken.image().substring(1, curToken.image().length() - 1);
        stringValue = stringValue.startsWith("\\") ? stringValue.substring(1) : stringValue;
        value = stringValue.charAt(0);
        setValue();
    }

    private void integerValue() {
        value = Long.parseLong(curToken.image());
        setValue();
    }

    private void floatValue() {
        String strValue = curToken.image().toLowerCase().endsWith("f") ?
                curToken.image().substring(0, curToken.image().length() - 1) : curToken.image();
        value = Double.parseDouble(strValue);
        setValue();
    }

    private void boolValue() {
        value = Boolean.parseBoolean(curToken.image());
        setValue();
    }

    private void nullValue() {
        value = null;
        setValue();
    }

    private void setValue() {
        if (context == CONTEXT_ARRAY) {
            arrStack.peek().add(value);
        } else {
            parsedData.put(key, value);
        }
        nextToken();
    }

    private void openArray() {
        if (arrStack.isEmpty()) {
            context = CONTEXT_ARRAY;
            arrStack.push(new ArrayList<>());
        } else {
            List<Object> newDimension = new ArrayList<>();
            arrStack.peek().add(newDimension);
            arrStack.push(newDimension);
        }
    }

    private Object[] parseArrayRecursively(List<Object> list) {
        Object[] array = new Object[list.size()];
        for (int i = 0; i < list.size(); ++i) {
            if (list.get(i) instanceof List) {
                array[i] = parseArrayRecursively((List<Object>) list.get(i));
            } else {
                array[i] = list.get(i);
            }
        }
        return array;
    }

    private void closeArray() {
        if (arrStack.size() > 1) {
            arrStack.pop();
        } else {
            context = CONTEXT_KEY;
            Object[] array = parseArrayRecursively(arrStack.pop());
            parsedData.put(key, array);
        }
    }

    // Parser controls

    private void nextToken() {
        if (++curIndex < tokens.size()) {
            curToken = tokens.get(curIndex);
        }
    }

    private void consumeToken(TokenCategory category) {
        if (curToken.category() != category) {
            throw new RuntimeException("Invalid token '" + curToken.image() + "' encountered at line "
                    + curToken.line() + " and column " + curToken.column()
                    + ". Expected " + TokenImages.TOKEN_IMAGE[category.ordinal()] + ".");
        }
        nextToken();
    }

    public int getCurIndex() {
        return curIndex;
    }

}
