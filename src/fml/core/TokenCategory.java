/**
 * MIT License
 * <p>
 * Copyright (c) 2025 Carlos Eduardo de Borba Machado
 */

package fml.core;

public enum TokenCategory {

    EOF(0),
    KEY(1),
    NULL(2),
    STRING(3),
    CHAR(4),
    INTEGER(5),
    FLOAT(6),
    BOOL(7),
    OPEN_ARRAY(8),
    CLOSE_ARRAY(9),
    END_OF_DATA(10),
    DATA_SEP(11),
    ARRAY_SEP(12);

    private final int value;

    TokenCategory(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
