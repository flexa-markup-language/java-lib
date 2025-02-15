/**
 * MIT License
 * <p>
 * Copyright (c) 2025 Carlos Eduardo de Borba Machado
 */

package fml.core;

public final class TokenImages {

    public static final String[] TOKEN_IMAGE = {
            "EOF",
            "key",
            "null constant",
            "string constant",
            "char constant",
            "integer constant",
            "float constant",
            "bool constant",
            "{",
            "}",
            ";",
            ":",
            ","
    };

    // avoid instantiation
    private TokenImages() {
        throw new UnsupportedOperationException("Utility class");
    }
}
