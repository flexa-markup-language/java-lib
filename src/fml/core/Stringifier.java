/**
 * MIT License
 * <p>
 * Copyright (c) 2025 Carlos Eduardo de Borba Machado
 */

package fml.core;

import java.util.Map;

public final class Stringifier {

    private final Map<String, Object> data;
    private final StringBuilder plainStringBuilder = new StringBuilder();
    private boolean beautify = false;
    private int tabLevel = 0;

    public Stringifier(Map<String, Object> data) {
        this.data = data;
    }

    public Stringifier(Map<String, Object> data, boolean beautify, int tabLevel) {
        this.data = data;
        this.beautify = beautify;
        this.tabLevel = tabLevel;
    }

    public String stringify() {
        for (var entry : data.entrySet()) {
            plainStringBuilder.append("\t".repeat(tabLevel));
            plainStringBuilder.append(entry.getKey());
            plainStringBuilder.append(":");
            stringifyValue(entry.getValue());
            plainStringBuilder.append(";");
            plainStringBuilder.append(beautify ? "\n" : "");
        }

        return plainStringBuilder.toString();
    }

    private void stringifyValue(Object value) {
        if (value instanceof Map) {
            plainStringBuilder.append(beautify ? "\n" : "");
            var mapValue = (Map<String, Object>) value;
            if (beautify) {
                plainStringBuilder.append(new Stringifier(mapValue, true, ++tabLevel).stringify());
                --tabLevel;
            } else {
                plainStringBuilder.append(new Stringifier(mapValue).stringify());
            }
        } else {
            plainStringBuilder.append(beautify ? " " : "");

            if (value == null) {
                plainStringBuilder.append("null");
            } else if (value.getClass().isArray()) {
                plainStringBuilder.append("{");
                stringifyArray((Object[]) value);
                plainStringBuilder.append(beautify ? "\n" : "");
                plainStringBuilder.append("}");
            } else {
                if (value instanceof String) {
                    plainStringBuilder.append("\"");
                    plainStringBuilder.append(((String) value).replace("\"", "\\\""));
                    plainStringBuilder.append("\"");
                } else if (value instanceof Character) {
                    char c = (Character) value;
                    plainStringBuilder.append("'");
                    plainStringBuilder.append(c == '\'' ? "\\" : "");
                    plainStringBuilder.append(c);
                    plainStringBuilder.append("'");
                } else if (value instanceof Boolean) {
                    plainStringBuilder.append(value.toString().toLowerCase());
                } else if (value instanceof Float || value instanceof Double) {
                    plainStringBuilder.append(value.toString().toLowerCase());
                } else {
                    plainStringBuilder.append(value);
                }
            }
        }
    }

    private void stringifyArray(Object[] arr) {
        if (beautify) {
            ++tabLevel;
        }
        for (var v : arr) {
            plainStringBuilder.append(beautify ? "\n" : "");
            plainStringBuilder.append("\t".repeat(tabLevel));
            stringifyValue(v);
            plainStringBuilder.append(",");
        }
        if (beautify) {
            --tabLevel;
        }
        if (arr.length > 0) {
            plainStringBuilder.deleteCharAt(plainStringBuilder.length() - 1);
        }
    }

}
